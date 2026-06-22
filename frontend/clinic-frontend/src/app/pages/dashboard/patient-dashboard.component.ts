import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../core/services/auth.service';
import { PatientService } from '../../core/services/patient.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { MedicalRecordService } from '../../core/services/medical-record.service';
import { DoctorService } from '../../core/services/doctor.service';
import { ToastService } from '../../shared/components/toast.service';
import { DashboardLayoutComponent } from '../../shared/layout/dashboard-layout.component';
import { AppointmentResponse } from '../../core/models/appointment/appointment-response.model';
import { DoctorListResponse } from '../../core/models/doctor/doctor-list-response.model';

@Component({
  selector: 'app-patient-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent],
  template: `
    <app-dashboard-layout title="Patient Dashboard" [profileImageUrl]="profileImageUrl">
      <!-- Warning Banner if Profile is Incomplete -->
      <div *ngIf="!hasProfile" class="bg-amber-50 border border-amber-200 text-amber-800 rounded-card-lg p-6 mb-8 flex flex-col sm:flex-row sm:items-center justify-between gap-4 shadow-sm border-l-4 border-l-amber-500 animate-pulse">
        <div class="flex items-start sm:items-center gap-4">
          <div class="w-12 h-12 bg-amber-100 text-amber-600 flex items-center justify-center rounded-xl flex-shrink-0">
            <i class="bi bi-exclamation-triangle text-2xl"></i>
          </div>
          <div>
            <h4 class="font-extrabold text-sm text-slate-900">Profil Anda Belum Lengkap!</h4>
            <p class="text-xs text-slate-600 mt-0.5">Anda harus melengkapi data diri terlebih dahulu untuk dapat menggunakan seluruh layanan klinik, termasuk membuat janji temu.</p>
          </div>
        </div>
        <a routerLink="/patient/profile" class="px-5 py-2.5 bg-amber-600 hover:bg-amber-700 text-white text-xs font-bold rounded-lg transition-all shadow-md flex-shrink-0 text-center">
          Lengkapi Profil Saya
        </a>
      </div>

      <!-- Welcome Card -->
      <div class="bg-gradient-to-r from-blue-600 via-blue-500 to-sky-500 text-white rounded-card-lg p-8 mb-8 shadow-lg shadow-blue-500/10 flex flex-col md:flex-row justify-between items-center gap-6">
        <div class="space-y-2">
          <h2 class="text-2xl lg:text-3xl font-black tracking-tight">Selamat Datang, {{ username }}!</h2>
          <p class="text-blue-100 text-sm leading-relaxed max-w-md">Kelola kesehatan Anda dengan mudah, pantau janji temu aktif, dan akses riwayat rekam medis digital secara aman.</p>
        </div>
        <div class="flex gap-3">
          <button (click)="goToCreateAppointment()" class="px-5 py-3 bg-white text-blue-600 font-bold text-xs rounded-xl shadow-md hover:bg-blue-50 transition-all flex items-center gap-1.5">
            <i class="bi bi-plus-lg"></i> Buat Janji Temu
          </button>
        </div>
      </div>

      <!-- Stats Grid -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all duration-300">
          <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-calendar-check text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ activeAppointmentsCount }}</h3>
          <p class="text-xs text-slate-500 font-bold mt-1">Janji Temu Aktif</p>
        </div>
        
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all duration-300">
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-clock-history text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ pastAppointmentsCount }}</h3>
          <p class="text-xs text-slate-500 font-bold mt-1">Riwayat Janji Temu</p>
        </div>

        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all duration-300">
          <div class="w-12 h-12 bg-amber-50 text-amber-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-file-earmark-medical text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ medicalRecordsCount }}</h3>
          <p class="text-xs text-slate-500 font-bold mt-1">Rekam Medis Anda</p>
        </div>

        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all duration-300">
          <div class="w-12 h-12 bg-purple-50 text-purple-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-people text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ totalDoctorsCount }}</h3>
          <p class="text-xs text-slate-500 font-bold mt-1">Dokter Tersedia</p>
        </div>
      </div>

      <!-- Upcoming Appointments Section -->
      <div class="bg-white border border-slate-100 rounded-card-lg p-6 mb-8 shadow-card">
        <div class="flex items-center justify-between pb-4 border-b border-slate-100 mb-6">
          <h3 class="font-extrabold text-lg text-slate-900">Janji Temu Terdekat</h3>
          <a routerLink="/patient/appointments" class="text-xs font-bold text-blue-600 hover:underline">Lihat Semua</a>
        </div>

        <!-- Loading -->
        <div *ngIf="loading" class="space-y-4">
          <div class="h-20 skeleton w-full"></div>
          <div class="h-20 skeleton w-full"></div>
        </div>

        <!-- Empty State -->
        <div *ngIf="!loading && upcomingAppointments.length === 0" class="text-center py-12">
          <div class="w-16 h-16 bg-slate-50 rounded-full flex items-center justify-center mx-auto mb-4 text-slate-400">
            <i class="bi bi-calendar-x text-2xl"></i>
          </div>
          <p class="font-bold text-slate-800 text-sm">Tidak ada janji temu aktif</p>
          <p class="text-xs text-slate-500 mt-1 mb-4">Silakan buat janji temu jika ingin berkonsultasi dengan dokter.</p>
          <button (click)="goToCreateAppointment()" class="inline-flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded-lg transition-all shadow-md">
            <i class="bi bi-plus"></i> Buat Janji Temu
          </button>
        </div>

        <!-- Appointments List -->
        <div *ngIf="!loading && upcomingAppointments.length > 0" class="space-y-4">
          <div *ngFor="let app of upcomingAppointments" class="bg-slate-50 border border-slate-100 rounded-xl p-5 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 hover:border-blue-200 transition-all">
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 bg-blue-100 text-blue-600 flex items-center justify-center rounded-xl flex-shrink-0 font-bold text-lg">
                <i class="bi bi-person-workspace"></i>
              </div>
              <div>
                <h4 class="font-extrabold text-slate-900 text-sm">{{ getDoctorName(app.doctorId) }}</h4>
                <p class="text-xs text-blue-600 font-medium">{{ getDoctorSpecialization(app.doctorId) }}</p>
                <div class="flex flex-wrap gap-x-4 gap-y-1 mt-2 text-xs text-slate-500">
                  <span><i class="bi bi-calendar-event text-slate-400 mr-1"></i> {{ app.appointmentDate }}</span>
                  <span><i class="bi bi-clock text-slate-400 mr-1"></i> {{ app.appointmentTime }} WIB</span>
                </div>
              </div>
            </div>

            <!-- Status Badge & Action -->
            <div class="flex items-center gap-3 w-full md:w-auto justify-between md:justify-end border-t md:border-t-0 pt-3 md:pt-0 mt-3 md:mt-0">
              <span class="px-3 py-1.5 rounded-lg text-[10px] font-bold uppercase tracking-wider" 
                    [ngClass]="{
                      'bg-emerald-50 text-emerald-700 border border-emerald-200': app.status !== 'UNPAID' && app.status !== 'CANCELLED',
                      'bg-amber-50 text-amber-700 border border-amber-200': app.status === 'UNPAID',
                      'bg-slate-50 text-slate-500 border border-slate-200': app.status === 'CANCELLED'
                    }">
                {{ app.status === 'UNPAID' ? 'QRIS: UNPAID' : (app.status === 'CANCELLED' ? 'QRIS: -' : 'QRIS: PAID') }}
              </span>
              <span class="px-3 py-1.5 rounded-lg text-[10px] font-bold uppercase tracking-wider" 
                    [ngClass]="{
                      'bg-blue-50 text-blue-600 border border-blue-100': app.status === 'SCHEDULED',
                      'bg-emerald-50 text-emerald-600 border border-emerald-100': app.status === 'CONFIRMED' || app.status === 'PAID',
                      'bg-amber-50 text-amber-600 border border-amber-100': app.status === 'PENDING' || app.status === 'UNPAID',
                      'bg-red-50 text-red-600 border border-red-100': app.status === 'CANCELLED'
                    }">
                {{ app.status === 'UNPAID' ? 'BELUM BAYAR' : app.status }}
              </span>
              <a *ngIf="app.status === 'UNPAID'" 
                 [routerLink]="['/patient/appointments/create']" 
                 [queryParams]="{ resumeId: app.id }" 
                 class="px-4 py-2 bg-amber-500 hover:bg-amber-600 text-white font-bold text-xs rounded-lg hover:scale-[1.02] active:scale-[0.98] transition-all">
                Bayar
              </a>
              <!-- Lihat E-Tiket Button (Only if status is APPROVED or PAID) -->
              <button *ngIf="app.status === 'APPROVED' || app.status === 'PAID'"
                      (click)="viewTicket(app)"
                      class="px-3 py-1.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs rounded-lg active:scale-[0.98] transition-all flex items-center gap-1 cursor-pointer border border-slate-200 shadow-sm"
                      title="Lihat E-Tiket">
                <i class="bi bi-eye"></i>
                <span>Lihat E-Tiket</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <h3 class="font-extrabold text-lg text-slate-900 mb-4">Aksi Cepat</h3>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
        <div (click)="goToCreateAppointment()" class="cursor-pointer bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl group-hover:bg-blue-600 group-hover:text-white transition-all">
            <i class="bi bi-plus-lg text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Buat Janji Temu</h4>
            <p class="text-xs text-slate-500">Booking jadwal dengan dokter</p>
          </div>
        </div>
        <a routerLink="/patient/doctors" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 flex items-center justify-center rounded-xl group-hover:bg-emerald-600 group-hover:text-white transition-all">
            <i class="bi bi-search text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Cari Dokter</h4>
            <p class="text-xs text-slate-500">Temukan dokter spesialis</p>
          </div>
        </a>
        <a routerLink="/patient/medical-records" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-amber-50 text-amber-600 flex items-center justify-center rounded-xl group-hover:bg-amber-600 group-hover:text-white transition-all">
            <i class="bi bi-file-earmark-medical text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Lihat Rekam Medis</h4>
            <p class="text-xs text-slate-500">Akses riwayat kesehatan</p>
          </div>
        </a>
      </div>

      <!-- MODAL DIALOG E-TIKET -->
      <div *ngIf="showTicketModal && selectedTicket" class="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
        <div class="bg-white rounded-3xl border border-slate-100 shadow-2xl max-w-md w-full overflow-hidden animate-in fade-in zoom-in-95 duration-200">
          
          <!-- Bagian Atas Tiket -->
          <div class="bg-gradient-to-br from-blue-700 to-blue-600 text-white p-8 text-center space-y-3 relative">
            <div class="w-16 h-16 bg-white/20 rounded-full flex items-center justify-center mx-auto shadow-inner text-white text-3xl">
              <i class="bi bi-ticket-perforated"></i>
            </div>
            <div>
              <h4 class="text-lg font-black tracking-tight">E-TIKET RESERVASI</h4>
              <p class="text-blue-100 text-xs font-bold uppercase tracking-wider">Status: TERBIT (APPROVED)</p>
            </div>
            <button (click)="closeTicketModal()" class="absolute right-6 top-6 text-white/80 hover:text-white transition-colors">
              <i class="bi bi-x-lg text-lg"></i>
            </button>
          </div>

          <!-- Tiket Connector (Garis sobek) -->
          <div class="flex justify-between items-center px-6 bg-slate-50 relative h-6 my-[-3px]">
            <div class="w-6 h-6 rounded-full bg-slate-900 absolute left-[-12px]"></div>
            <div class="w-full border-t-2 border-dashed border-slate-300"></div>
            <div class="w-6 h-6 rounded-full bg-slate-900 absolute right-[-12px]"></div>
          </div>

          <!-- Detail Tiket -->
          <div class="p-6 bg-slate-50 text-center space-y-6">
            <div class="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm text-left space-y-4">
              <div>
                <span class="block text-[10px] text-slate-400 font-extrabold uppercase">Nomor Tiket</span>
                <span class="text-sm font-extrabold text-slate-900">{{ ticketBarcode }}</span>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <span class="block text-[10px] text-slate-400 font-extrabold uppercase">Dokter</span>
                  <span class="text-xs font-extrabold text-slate-900">{{ getDoctorName(selectedTicket.doctorId) }}</span>
                </div>
                <div>
                  <span class="block text-[10px] text-slate-400 font-extrabold uppercase">Spesialisasi</span>
                  <span class="text-xs font-bold text-blue-600">{{ getDoctorSpecialization(selectedTicket.doctorId) }}</span>
                </div>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <span class="block text-[10px] text-slate-400 font-extrabold uppercase">Tanggal</span>
                  <span class="text-xs font-extrabold text-slate-900">{{ selectedTicket.appointmentDate }}</span>
                </div>
                <div>
                  <span class="block text-[10px] text-slate-400 font-extrabold uppercase">Jam Kunjungan</span>
                  <span class="text-xs font-extrabold text-slate-900">{{ selectedTicket.appointmentTime.substring(0, 5) }}</span>
                </div>
              </div>
              <div *ngIf="selectedTicket.notes" class="pt-2 border-t border-slate-100">
                <span class="block text-[10px] text-slate-400 font-extrabold uppercase">Catatan Keluhan</span>
                <p class="text-xs text-slate-600 font-semibold italic">"{{ selectedTicket.notes }}"</p>
              </div>
            </div>

            <!-- Barcode Simulasi -->
            <div class="space-y-2">
              <div class="bg-white p-4 rounded-2xl border border-slate-100 inline-block shadow-sm">
                <!-- Representasi Barcode CSS Premium -->
                <div class="flex items-center justify-center gap-1.5 h-12 w-48">
                  <span class="h-full bg-slate-950 w-1"></span>
                  <span class="h-full bg-slate-950 w-2"></span>
                  <span class="h-full bg-slate-950 w-0.5"></span>
                  <span class="h-full bg-slate-950 w-1.5"></span>
                  <span class="h-full bg-slate-950 w-2.5"></span>
                  <span class="h-full bg-slate-950 w-0.5"></span>
                  <span class="h-full bg-slate-950 w-1.5"></span>
                  <span class="h-full bg-slate-950 w-3"></span>
                  <span class="h-full bg-slate-950 w-0.5"></span>
                  <span class="h-full bg-slate-950 w-1"></span>
                  <span class="h-full bg-slate-950 w-2"></span>
                </div>
                <p class="text-[9px] text-slate-400 font-black tracking-widest mt-1">{{ ticketBarcode }}</p>
              </div>
              <p class="text-[10px] text-slate-400 font-semibold max-w-xs mx-auto">Tunjukkan barcode di atas kepada petugas resepsionis klinik saat kedatangan.</p>
            </div>

            <!-- Action Button -->
            <button type="button" 
                    (click)="closeTicketModal()" 
                    class="w-full py-3.5 bg-slate-900 hover:bg-slate-800 text-white font-extrabold rounded-xl transition-all flex items-center justify-center gap-2">
              <span>Tutup Tiket</span>
            </button>
          </div>
        </div>
      </div>
    </app-dashboard-layout>
  `
})
export class PatientDashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private patientService = inject(PatientService);
  private appointmentService = inject(AppointmentService);
  private medicalRecordService = inject(MedicalRecordService);
  private doctorService = inject(DoctorService);
  private toast = inject(ToastService);
  private router = inject(Router);

  hasProfile = true;
  loading = false;
  profileImageUrl: string | null = null;
  
  // E-Tiket States
  showTicketModal = false;
  selectedTicket: AppointmentResponse | null = null;
  ticketBarcode = '';
  
  // Stats
  activeAppointmentsCount = 0;
  pastAppointmentsCount = 0;
  medicalRecordsCount = 0;
  totalDoctorsCount = 0;

  upcomingAppointments: AppointmentResponse[] = [];
  doctors: DoctorListResponse[] = [];
  doctorMap: { [key: number]: DoctorListResponse } = {};

  ngOnInit(): void {
    this.checkProfile();
    this.loadDashboardData();
  }

  checkProfile(): void {
    this.patientService.getMyProfile().subscribe({
      next: (profile) => {
        this.hasProfile = true;
        this.profileImageUrl = this.getPatientImageUrl(profile.profileImageUrl);
      },
      error: () => {
        this.hasProfile = false;
        this.toast.warning('Mohon lengkapi data pribadi Anda terlebih dahulu untuk mengakses semua fitur!');
      }
    });
  }

  getPatientImageUrl(path: string | undefined | null): string | null {
    if (!path) return null;
    if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('data:')) {
      return path;
    }
    if (path.startsWith('/api/patients')) {
      return `http://localhost:8080${path}`;
    }
    return `http://localhost:8080/api/patients${path}`;
  }

  loadDashboardData(): void {
    this.loading = true;
    forkJoin({
      appointments: this.appointmentService.getMyAppointments(),
      records: this.medicalRecordService.getPatientMedicalRecords(),
      doctors: this.doctorService.getAllDoctors()
    }).subscribe({
      next: (res) => {
        this.doctors = res.doctors;
        this.totalDoctorsCount = res.doctors.length;
        
        // Build Doctor Map
        this.doctorMap = {};
        res.doctors.forEach(doc => {
          this.doctorMap[doc.id] = doc;
        });

        // Parse Appointments
        const appointments = res.appointments;
        this.activeAppointmentsCount = appointments.filter(a => a.status === 'SCHEDULED' || a.status === 'CONFIRMED' || a.status === 'PENDING' || a.status === 'UNPAID' || a.status === 'PAID').length;
        this.pastAppointmentsCount = appointments.filter(a => a.status === 'COMPLETED' || a.status === 'CANCELLED').length;
        
        // Upcoming list
        this.upcomingAppointments = appointments
          .filter(a => a.status === 'SCHEDULED' || a.status === 'CONFIRMED' || a.status === 'PENDING' || a.status === 'UNPAID' || a.status === 'PAID')
          .sort((a, b) => new Date(a.appointmentDate).getTime() - new Date(b.appointmentDate).getTime())
          .slice(0, 3);

        // Parse Medical Records
        this.medicalRecordsCount = res.records.length;
        this.loading = false;
      },
      error: () => {
        this.toast.error('Gagal memuat data dashboard.');
        this.loading = false;
      }
    });
  }

  getDoctorName(doctorId: number): string {
    return this.doctorMap[doctorId]?.fullName ?? 'Dokter Spesialis';
  }

  getDoctorSpecialization(doctorId: number): string {
    return this.doctorMap[doctorId]?.specialization ?? 'Klinik Kesehatan';
  }

  goToCreateAppointment(): void {
    if (!this.hasProfile) {
      this.toast.warning('Mohon lengkapi data pribadi Anda terlebih dahulu sebelum membuat janji temu.');
      setTimeout(() => {
        this.router.navigate(['/patient/profile']);
      }, 1500);
    } else {
      this.router.navigate(['/patient/appointments/create']);
    }
  }

  cancelAppointment(appointmentId: number): void {
    if (confirm('Apakah Anda yakin ingin membatalkan janji temu ini?')) {
      this.appointmentService.updateStatus(appointmentId, { status: 'CANCELLED' }).subscribe({
        next: () => {
          this.toast.success('Janji temu berhasil dibatalkan.');
          this.loadDashboardData();
        },
        error: () => {
          this.toast.error('Gagal membatalkan janji temu.');
        }
      });
    }
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

  get username(): string {
    return this.authService.getPayload()?.sub ?? 'Pasien';
  }
}