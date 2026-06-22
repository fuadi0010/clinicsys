import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AppointmentService } from '../../../core/services/appointment.service';
import { DoctorService } from '../../../core/services/doctor.service';
import { PatientService } from '../../../core/services/patient.service';
import { ToastService } from '../../../shared/components/toast.service';
import { DoctorListResponse } from '../../../core/models/doctor/doctor-list-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';

@Component({
  selector: 'app-create-appointment',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule, DashboardLayoutComponent],
  templateUrl: './create-appointment.component.html'
})
export class CreateAppointmentComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private appointmentService = inject(AppointmentService);
  private doctorService = inject(DoctorService);
  private patientService = inject(PatientService);
  private toast = inject(ToastService);
  private sanitizer = inject(DomSanitizer);

  doctors: DoctorListResponse[] = [];
  filteredDoctors: DoctorListResponse[] = [];
  specializations: string[] = [];
  selectedSpecialization = '';
  loading = false;

  // Date Picker Dropdowns
  years: number[] = [];
  months = [
    { value: 1, label: 'Januari' },
    { value: 2, label: 'Februari' },
    { value: 3, label: 'Maret' },
    { value: 4, label: 'April' },
    { value: 5, label: 'Mei' },
    { value: 6, label: 'Juni' },
    { value: 7, label: 'Juli' },
    { value: 8, label: 'Agustus' },
    { value: 9, label: 'September' },
    { value: 10, label: 'Oktober' },
    { value: 11, label: 'November' },
    { value: 12, label: 'Desember' }
  ];
  days: number[] = [];

  selectedYear = new Date().getFullYear();
  selectedMonth = new Date().getMonth() + 1;
  selectedDay = new Date().getDate();

  // Time Slots
  availableSlots: { time: string; available: boolean; label: string }[] = [];
  selectedTimeSlot: string | null = null;
  slotsLoading = false;

  // Modal / Payment / Ticket
  showPaymentModal = false;
  showTicketModal = false;
  showQrModal = false;
  qrCodeUrl = '';
  pollingIntervalId: any = null;
  createdAppointment: any = null;
  paymentProcessing = false;
  ticketBarcode = '';

  isProfileComplete = true;

  appointmentForm = this.fb.group({
    doctorId: [null as number | null, Validators.required],
    appointmentDate: ['', Validators.required],
    appointmentTime: ['', Validators.required],
    notes: ['']
  });

  ngOnInit(): void {
    this.checkProfile();
    this.loadDoctors();
    this.initDateDropdowns();

    const resumeIdParam = this.route.snapshot.queryParamMap.get('resumeId');
    if (resumeIdParam) {
      const resumeId = Number(resumeIdParam);
      this.loading = true;
      this.appointmentService.getAppointmentForPatient(resumeId).subscribe({
        next: (app) => {
          console.log('[DEBUG] Resume Appointment detail:', app);
          console.log('[DEBUG] paymentQrUrl from backend:', app.paymentQrUrl);
          this.createdAppointment = app;
          this.qrCodeUrl = app.paymentQrUrl || '';

          this.appointmentForm.patchValue({
            doctorId: app.doctorId,
            appointmentDate: app.appointmentDate,
            appointmentTime: app.appointmentTime,
            notes: app.notes
          });
          this.appointmentForm.disable();

          this.showQrModal = true;
          this.startPaymentPolling(resumeId);
          this.loading = false;
        },
        error: (err) => {
          this.toast.error('Gagal mengambil detail janji temu untuk melanjutkan pembayaran.');
          this.router.navigate(['/patient/appointments']);
          this.loading = false;
        }
      });
    }
  }

  private checkProfile(): void {
    this.patientService.getMyProfile().subscribe({
      next: (profile) => {
        if (!profile || !profile.identityNumber || !profile.fullName) {
          this.isProfileComplete = false;
        } else {
          this.isProfileComplete = true;
        }
      },
      error: () => {
        this.isProfileComplete = false;
      }
    });
  }

  private loadDoctors(): void {
    this.doctorService.getAllDoctors().subscribe({
      next: doctors => {
        this.doctors = doctors;
        this.specializations = [...new Set(doctors.map(d => d.specialization).filter(Boolean))];
        this.filterDoctors();

        const doctorIdParam = this.route.snapshot.queryParamMap.get('doctorId');
        if (doctorIdParam) {
          const docId = Number(doctorIdParam);
          const foundDoc = this.doctors.find(d => d.id === docId);
          if (foundDoc) {
            this.selectedSpecialization = foundDoc.specialization;
            this.filterDoctors();
            this.appointmentForm.patchValue({ doctorId: docId });
            this.fetchAvailableSlots();
          }
        }
      }
    });
  }

  filterDoctors(): void {
    if (this.selectedSpecialization) {
      this.filteredDoctors = this.doctors.filter(d => d.specialization === this.selectedSpecialization);
    } else {
      this.filteredDoctors = this.doctors;
    }

    const currentDoctorId = this.appointmentForm.get('doctorId')?.value;
    if (currentDoctorId && !this.filteredDoctors.some(d => d.id === Number(currentDoctorId))) {
      this.appointmentForm.patchValue({ doctorId: null });
    }
    this.fetchAvailableSlots();
  }

  initDateDropdowns(): void {
    const currentYear = new Date().getFullYear();
    this.years = Array.from({ length: 10 }, (_, i) => currentYear + i);
    this.updateDaysList();
    this.updateFormattedDate();
  }

  updateDaysList(): void {
    const daysInMonth = new Date(this.selectedYear, this.selectedMonth, 0).getDate();
    this.days = Array.from({ length: daysInMonth }, (_, i) => i + 1);
    if (this.selectedDay > daysInMonth) {
      this.selectedDay = daysInMonth;
    }
  }

  onDateChange(): void {
    this.updateDaysList();
    this.updateFormattedDate();
  }

  updateFormattedDate(): void {
    const pad = (n: number) => n.toString().padStart(2, '0');
    const dateStr = `${this.selectedYear}-${pad(this.selectedMonth)}-${pad(this.selectedDay)}`;

    // Cegah tanggal di masa lalu
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const selected = new Date(this.selectedYear, this.selectedMonth - 1, this.selectedDay);
    if (selected < today) {
      this.toast.warning('Tanggal janji temu tidak boleh di masa lalu.');
      const current = new Date();
      this.selectedYear = current.getFullYear();
      this.selectedMonth = current.getMonth() + 1;
      this.selectedDay = current.getDate();
      this.updateDaysList();
      this.updateFormattedDate();
      return;
    }

    this.appointmentForm.patchValue({ appointmentDate: dateStr });
    this.fetchAvailableSlots();
  }

  fetchAvailableSlots(): void {
    const doctorId = this.appointmentForm.get('doctorId')?.value;
    const date = this.appointmentForm.get('appointmentDate')?.value;

    if (!doctorId || !date) {
      this.availableSlots = [];
      return;
    }

    this.slotsLoading = true;
    this.appointmentService.getAvailableSlots(Number(doctorId), date).subscribe({
      next: (slots) => {
        this.availableSlots = slots.map(s => {
          const timeShort = s.time.substring(0, 5);
          let isAvailable = s.available;

          const today = new Date();
          const pad = (n: number) => n.toString().padStart(2, '0');
          const todayStr = `${today.getFullYear()}-${pad(today.getMonth() + 1)}-${pad(today.getDate())}`;

          if (date === todayStr) {
            const [slotHour, slotMinute] = s.time.split(':').map(Number);
            const currentHour = today.getHours();
            const currentMinute = today.getMinutes();
            if (slotHour < currentHour || (slotHour === currentHour && slotMinute <= currentMinute)) {
              isAvailable = false;
            }
          }

          return {
            time: s.time,
            label: timeShort,
            available: isAvailable
          };
        });

        const currentSelectedTime = this.appointmentForm.get('appointmentTime')?.value;
        if (currentSelectedTime && !this.availableSlots.some(s => s.time === currentSelectedTime && s.available)) {
          this.selectTimeSlot(null);
        }
        this.slotsLoading = false;
      },
      error: () => {
        this.toast.error('Gagal mengambil ketersediaan slot jam dokter.');
        this.slotsLoading = false;
      }
    });
  }

  selectTimeSlot(slot: string | null): void {
    this.selectedTimeSlot = slot;
    this.appointmentForm.patchValue({ appointmentTime: slot || '' });
  }

  getDoctorName(): string {
    const docId = this.appointmentForm.get('doctorId')?.value;
    const doc = this.doctors.find(d => d.id === Number(docId));
    return doc ? doc.fullName : '';
  }

  getDoctorSpecialization(): string {
    const docId = this.appointmentForm.get('doctorId')?.value;
    const doc = this.doctors.find(d => d.id === Number(docId));
    return doc ? doc.specialization : '';
  }

  submit(): void {
    if (!this.isProfileComplete) {
      this.toast.warning('Silakan lengkapi data pribadi Anda terlebih dahulu di menu Profil sebelum membuat janji temu.');
      return;
    }
    if (this.appointmentForm.invalid) {
      this.appointmentForm.markAllAsTouched();
      return;
    }
    // Buka modal proses pembayaran
    this.showPaymentModal = true;
  }

  processPayment(): void {
    this.paymentProcessing = true;
    const formValue = this.appointmentForm.getRawValue();
    const request = {
      doctorId: Number(formValue.doctorId),
      appointmentDate: formValue.appointmentDate!,
      appointmentTime: formValue.appointmentTime!,
      notes: formValue.notes ?? ''
    };

    this.appointmentService.createAppointment(request).subscribe({
      next: (res) => {
        console.log('[DEBUG] Created Appointment response:', res);
        console.log('[DEBUG] paymentQrUrl from backend:', res?.paymentQrUrl);
        this.createdAppointment = res;
        this.qrCodeUrl = res?.paymentQrUrl || '';
        this.showPaymentModal = false;
        this.showQrModal = true;
        this.paymentProcessing = false;
        if (res?.id) {
          this.startPaymentPolling(res.id);
        } else {
          this.toast.error('Gagal mengambil ID janji temu untuk polling pembayaran.');
        }
      },
      error: (err) => {
        this.showPaymentModal = false;
        this.paymentProcessing = false;
        let msg = err.error?.message || 'Gagal memproses pembayaran. Silakan coba lagi.';
        if (msg === 'Time slot already booked for this doctor') {
          msg = 'Jadwal dokter pada waktu tersebut sudah penuh/dipesan oleh pasien lain.';
        }
        this.toast.error(msg);
        this.fetchAvailableSlots();
      }
    });
  }

  startPaymentPolling(appointmentId: number): void {
    this.stopPaymentPolling();
    this.pollingIntervalId = setInterval(() => {
      this.appointmentService.getAppointmentStatus(appointmentId).subscribe({
        next: (res) => {
          if (res && res.status === 'PAID') {
            this.stopPaymentPolling();
            this.showQrModal = false;
            this.ticketBarcode = 'TKT-' + appointmentId + '-' + Math.floor(Math.random() * 100000);
            this.showTicketModal = true;
            this.toast.success('Pembayaran terdeteksi! E-tiket telah diterbitkan secara otomatis.');
          }
        },
        error: () => { }
      });
    }, 2500);
  }

  stopPaymentPolling(): void {
    if (this.pollingIntervalId) {
      clearInterval(this.pollingIntervalId);
      this.pollingIntervalId = null;
    }
  }

  ngOnDestroy(): void {
    this.stopPaymentPolling();
  }

  getSafeQrCodeUrl(url: string): SafeUrl {
    if (!url) return '';
    if (url.startsWith('data:image')) {
      return this.sanitizer.bypassSecurityTrustUrl(url);
    }
    const fullUrl = 'https://api.qrserver.com/v1/create-qr-code/?size=220x220&data=' + encodeURIComponent(url);
    return this.sanitizer.bypassSecurityTrustUrl(fullUrl);
  }

  goToAppointments(): void {
    this.showTicketModal = false;
    this.router.navigate(['/patient/appointments']);
  }
}
