import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { MedicalRecordService } from '../../core/services/medical-record.service';
import { DoctorService } from '../../core/services/doctor.service';
import { DashboardLayoutComponent } from '../../shared/layout/dashboard-layout.component';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-doctor-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent],
  template: `
    <app-dashboard-layout title="Doctor Dashboard" [profileImageUrl]="profileImageUrl">
      <div class="bg-gradient-to-br from-emerald-600 to-emerald-800 text-white rounded-card-lg p-8 mb-8 flex flex-col sm:flex-row items-center gap-6 shadow-md shadow-emerald-600/10 animate-fade-in">
        <div class="w-20 h-20 rounded-full overflow-hidden border-2 border-white/20 flex-shrink-0 bg-emerald-700 flex items-center justify-center">
          <img *ngIf="profileImageUrl" [src]="profileImageUrl" class="w-full h-full object-cover">
          <span *ngIf="!profileImageUrl" class="text-2xl font-extrabold">{{ displayInitial }}</span>
        </div>
        <div class="text-center sm:text-left">
          <h2 class="text-2xl font-extrabold mb-2">Selamat Datang, Dr. {{ fullName || username }}!</h2>
          <p class="text-emerald-100 text-sm font-medium">Kelola jadwal praktik dan rekam medis pasien Anda.</p>
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-calendar-check text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ todayAppointmentsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Janji Temu Hari Ini</p>
        </div>
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-people text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ totalPatientsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Total Pasien</p>
        </div>
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-amber-50 text-amber-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-file-earmark-medical text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ medicalRecordsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Rekam Medis</p>
        </div>
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-purple-50 text-purple-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-star text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ averageRating ? (averageRating | number:'1.1-1') : '0.0' }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Rating</p>
        </div>
      </div>

      <h3 class="font-extrabold text-lg text-slate-900 mb-4">Aksi Cepat</h3>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <a routerLink="/doctor/appointments" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-emerald-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 flex items-center justify-center rounded-xl group-hover:bg-emerald-600 group-hover:text-white transition-all">
            <i class="bi bi-list-check text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Kelola Janji Temu</h4>
            <p class="text-xs text-slate-500">Lihat & kelola jadwal pasien</p>
          </div>
        </a>
        <a routerLink="/doctor/medical-records/create" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-emerald-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-amber-50 text-amber-600 flex items-center justify-center rounded-xl group-hover:bg-amber-600 group-hover:text-white transition-all">
            <i class="bi bi-plus-circle text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Buat Rekam Medis</h4>
            <p class="text-xs text-slate-500">Catat diagnosis & tindakan</p>
          </div>
        </a>
        <a routerLink="/doctor/medical-records" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-emerald-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl group-hover:bg-blue-600 group-hover:text-white transition-all">
            <i class="bi bi-file-earmark-text text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Riwayat Rekam Medis</h4>
            <p class="text-xs text-slate-500">Lihat semua catatan pasien</p>
          </div>
        </a>
      </div>
    </app-dashboard-layout>
  `
})
export class DoctorDashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private appointmentService = inject(AppointmentService);
  private medicalRecordService = inject(MedicalRecordService);
  private doctorService = inject(DoctorService);

  todayAppointmentsCount = 0;
  totalPatientsCount = 0;
  medicalRecordsCount = 0;
  profileImageUrl: string | null = null;
  fullName = '';
  averageRating = 0.0;

  ngOnInit(): void {
    this.loadData();
    this.loadDoctorProfile();
  }

  loadDoctorProfile(): void {
    this.doctorService.getMyProfile().subscribe({
      next: (profile) => {
        this.fullName = profile.fullName;
        this.profileImageUrl = this.getDoctorImageUrl(profile.profileImageUrl);
        this.averageRating = profile.averageRating || 0.0;
      },
      error: (err) => {
        console.error('Gagal memuat profil dokter', err);
      }
    });
  }

  getDoctorImageUrl(path: string | undefined | null): string | null {
    if (!path || path === 'null' || path === 'undefined' || path.endsWith('/null') || path.endsWith('/undefined')) {
      return 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&w=600&q=80';
    }
    if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('data:')) {
      return path;
    }
    if (path.startsWith('/api/doctors')) {
      return `http://localhost:8080${path}`;
    }
    return `http://localhost:8080/api/doctors${path}`;
  }

  loadData(): void {
    forkJoin({
      appointments: this.appointmentService.getDoctorAppointments(),
      records: this.medicalRecordService.getDoctorMedicalRecords()
    }).subscribe({
      next: (res) => {
        // Format tanggal hari ini local (YYYY-MM-DD)
        const d = new Date();
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const date = String(d.getDate()).padStart(2, '0');
        const todayStr = `${year}-${month}-${date}`;

        // Janji Temu Hari Ini
        this.todayAppointmentsCount = res.appointments.filter(a => 
          a.appointmentDate === todayStr && 
          (a.status === 'PAID' || a.status === 'APPROVED')
        ).length;

        // Total Pasien Unik
        const patientIds = res.appointments.map(a => a.patientId);
        this.totalPatientsCount = new Set(patientIds).size;

        // Total Rekam Medis
        this.medicalRecordsCount = res.records.length;
      },
      error: () => {
        console.error('Gagal memuat data dashboard dokter');
      }
    });
  }

  get username(): string {
    return this.authService.getPayload()?.sub ?? 'Dokter';
  }

  get displayInitial(): string {
    return (this.fullName || this.username).charAt(0).toUpperCase();
  }
}