import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { PatientService } from '../../core/services/patient.service';
import { DoctorService } from '../../core/services/doctor.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { DashboardLayoutComponent } from '../../shared/layout/dashboard-layout.component';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent],
  template: `
    <app-dashboard-layout title="Admin Dashboard">
      <div class="bg-gradient-to-br from-purple-600 to-purple-800 text-white rounded-card-lg p-8 mb-8">
        <h2 class="text-2xl font-extrabold mb-2">Selamat Datang, {{ username }}!</h2>
        <p class="text-purple-100 text-sm">Panel administrasi sistem manajemen klinik.</p>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-purple-50 text-purple-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-people text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ totalPatientsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Total Pasien</p>
        </div>
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-person-badge text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ totalDoctorsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Total Dokter</p>
        </div>
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-emerald-50 text-emerald-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-calendar-check text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ activeAppointmentsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Janji Temu Aktif</p>
        </div>
        <div class="bg-white border border-slate-100 rounded-card p-6 shadow-card hover:shadow-card-hover transition-all">
          <div class="w-12 h-12 bg-amber-50 text-amber-600 flex items-center justify-center rounded-xl mb-4">
            <i class="bi bi-shield-check text-xl"></i>
          </div>
          <h3 class="text-2xl font-black text-slate-900">{{ totalAdminsCount }}</h3>
          <p class="text-xs text-slate-500 font-medium mt-1">Total Admin</p>
        </div>
      </div>

      <h3 class="font-extrabold text-lg text-slate-900 mb-4">Menu Admin</h3>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <a routerLink="/admin/patients" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-purple-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-purple-50 text-purple-600 flex items-center justify-center rounded-xl group-hover:bg-purple-600 group-hover:text-white transition-all">
            <i class="bi bi-people text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Data Pasien</h4>
            <p class="text-xs text-slate-500">Kelola data pasien</p>
          </div>
        </a>
        <a routerLink="/admin/doctors" class="bg-white border border-slate-100 rounded-card p-5 shadow-card hover:shadow-card-hover hover:border-purple-200 transition-all flex items-center gap-4 group">
          <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl group-hover:bg-blue-600 group-hover:text-white transition-all">
            <i class="bi bi-person-badge text-xl"></i>
          </div>
          <div>
            <h4 class="font-bold text-slate-900">Data Dokter</h4>
            <p class="text-xs text-slate-500">Kelola data dokter</p>
          </div>
        </a>
      </div>
    </app-dashboard-layout>
  `
})
export class AdminDashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private patientService = inject(PatientService);
  private doctorService = inject(DoctorService);
  private appointmentService = inject(AppointmentService);

  totalPatientsCount = 0;
  totalDoctorsCount = 0;
  activeAppointmentsCount = 0;
  totalAdminsCount = 0;

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    forkJoin({
      patientsCount: this.patientService.getPatientsCount(),
      doctorsCount: this.doctorService.getDoctorsCount(),
      adminsCount: this.authService.getAdminsCount(),
      appointmentsCount: this.appointmentService.getActiveAppointmentsCount()
    }).subscribe({
      next: (res) => {
        this.totalPatientsCount = res.patientsCount;
        this.totalDoctorsCount = res.doctorsCount;
        this.activeAppointmentsCount = res.appointmentsCount;
        this.totalAdminsCount = res.adminsCount;
      },
      error: (err) => {
        console.error('Gagal memuat data dashboard admin', err);
      }
    });
  }

  get username(): string {
    return this.authService.getPayload()?.sub ?? 'Admin';
  }
}
