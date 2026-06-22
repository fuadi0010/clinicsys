import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../../core/services/appointment.service';
import { PatientService } from '../../../core/services/patient.service';
import { DoctorService } from '../../../core/services/doctor.service';
import { ToastService } from '../../../shared/components/toast.service';
import { AppointmentResponse } from '../../../core/models/appointment/appointment-response.model';
import { DoctorListResponse } from '../../../core/models/doctor/doctor-list-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state.component';

@Component({
  selector: 'app-my-appointments',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, DashboardLayoutComponent, EmptyStateComponent],
  templateUrl: './my-appointments.component.html'
})
export class MyAppointmentsComponent implements OnInit {
  private appointmentService = inject(AppointmentService);
  private patientService = inject(PatientService);
  private doctorService = inject(DoctorService);
  private toast = inject(ToastService);

  appointments: AppointmentResponse[] = [];
  filteredAppointments: AppointmentResponse[] = [];
  activeTab = 'all';

  // Rating States
  showRatingModal = false;
  ratingValue = 0;
  reviewText = '';
  selectedAppointment: AppointmentResponse | null = null;
  submittingRating = false;
  ratedAppointmentsMap: { [appointmentId: number]: number } = {};

  // E-Tiket States
  showTicketModal = false;
  selectedTicket: AppointmentResponse | null = null;
  ticketBarcode = '';
  doctors: DoctorListResponse[] = [];
  doctorMap: { [key: number]: DoctorListResponse } = {};

  tabs = [
    { key: 'all', label: 'Semua' },
    { key: 'UNPAID', label: 'Belum Bayar' },
    { key: 'COMPLETED', label: 'Selesai' }
  ];

  ngOnInit(): void {
    this.loadDoctors();
    this.loadAppointments();
    this.loadRatings();
  }

  private loadDoctors(): void {
    this.doctorService.getAllDoctors().subscribe({
      next: docs => {
        this.doctors = docs;
        this.doctorMap = {};
        docs.forEach(d => {
          this.doctorMap[d.id] = d;
        });
      }
    });
  }

  private loadAppointments(): void {
    this.appointmentService.getMyAppointments().subscribe({
      next: appointments => {
        this.appointments = appointments;
        this.filterAppointments();
      }
    });
  }

  private loadRatings(): void {
    this.patientService.getMyRatings().subscribe({
      next: ratings => {
        this.ratedAppointmentsMap = {};
        ratings.forEach(r => {
          this.ratedAppointmentsMap[r.appointmentId] = r.rating;
        });
      }
    });
  }

  filterAppointments(): void {
    if (this.activeTab === 'all') {
      this.filteredAppointments = this.appointments;
    } else {
      this.filteredAppointments = this.appointments.filter(a => a.status === this.activeTab);
    }
  }

  statusClass(status: string): string {
    switch (status) {
      case 'UNPAID': return 'bg-amber-50 text-amber-600 border border-amber-200';
      case 'SCHEDULED': return 'bg-blue-50 text-blue-600';
      case 'COMPLETED': return 'bg-emerald-50 text-emerald-600';
      case 'CANCELLED': return 'bg-red-50 text-red-600';
      default: return 'bg-slate-50 text-slate-600';
    }
  }

  hasBeenRated(appointmentId: number): boolean {
    return this.ratedAppointmentsMap[appointmentId] !== undefined;
  }

  getRatingValue(appointmentId: number): number {
    return this.ratedAppointmentsMap[appointmentId] || 0;
  }

  getDoctorName(doctorId: number): string {
    return this.doctorMap[doctorId]?.fullName ?? `Dokter #${doctorId}`;
  }

  getDoctorSpecialization(doctorId: number): string {
    return this.doctorMap[doctorId]?.specialization ?? 'Umum';
  }

  viewTicket(appointment: AppointmentResponse): void {
    this.selectedTicket = appointment;
    this.ticketBarcode = 'TKT-' + appointment.id + '-' + ((appointment.id * 17) % 100000);
    this.showTicketModal = true;
  }

  closeTicketModal(): void {
    this.showTicketModal = false;
    this.selectedTicket = null;
  }

  openRatingModal(a: AppointmentResponse): void {
    this.selectedAppointment = a;
    this.ratingValue = 0;
    this.reviewText = '';
    this.showRatingModal = true;
  }

  closeRatingModal(): void {
    this.showRatingModal = false;
    this.selectedAppointment = null;
  }

  selectRatingValue(val: number): void {
    this.ratingValue = val;
  }

  submitRating(): void {
    if (!this.selectedAppointment || this.ratingValue === 0) return;
    this.submittingRating = true;

    const payload = {
      appointmentId: this.selectedAppointment.id,
      doctorId: this.selectedAppointment.doctorId,
      rating: this.ratingValue,
      review: this.reviewText.trim()
    };

    this.patientService.submitRating(payload).subscribe({
      next: () => {
        this.toast.success('Ulasan & rating berhasil dikirim. Terima kasih!');
        this.submittingRating = false;
        this.closeRatingModal();
        this.loadRatings();
      },
      error: (err) => {
        this.submittingRating = false;
        this.toast.error(err.error?.message || 'Gagal mengirim rating');
      }
    });
  }
}
