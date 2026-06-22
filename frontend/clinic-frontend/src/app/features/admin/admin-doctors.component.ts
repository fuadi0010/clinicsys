import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DoctorService } from '../../core/services/doctor.service';
import { DoctorListResponse } from '../../core/models/doctor/doctor-list-response.model';
import { DashboardLayoutComponent } from '../../shared/layout/dashboard-layout.component';
import { SkeletonComponent } from '../../shared/components/skeleton.component';
import { EmptyStateComponent } from '../../shared/components/empty-state.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-doctors',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent, SkeletonComponent, EmptyStateComponent],
  template: `
    <app-dashboard-layout title="Data Dokter">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl font-bold text-slate-800">Daftar Dokter Spesialis</h2>
        <a routerLink="/admin/doctors/create" class="px-4 py-2 text-sm font-bold text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-all flex items-center gap-2">
          <i class="bi bi-plus-lg"></i> Tambah Dokter
        </a>
      </div>

      <app-skeleton *ngIf="loading" [rows]="4" />

      <app-empty-state *ngIf="!loading && doctors.length === 0"
        icon="bi-person-badge"
        title="Tidak Ada Data Dokter"
        message="Belum ada dokter yang terdaftar di sistem."
      />

      <div *ngIf="!loading && doctors.length > 0" class="bg-white border border-slate-100 rounded-card-lg shadow-card overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead>
              <tr class="border-b border-slate-100 bg-slate-50">
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">ID</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Nama</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Spesialisasi</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Pengalaman</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Biaya</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Rating</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let d of doctors" class="border-b border-slate-50 hover:bg-slate-50/50 transition-all">
                <td class="px-6 py-4 font-medium text-slate-900">{{ d.id }}</td>
                <td class="px-6 py-4 text-slate-600">{{ d.fullName }}</td>
                <td class="px-6 py-4">
                  <span class="inline-flex px-3 py-1 bg-blue-50 text-blue-600 text-xs font-bold rounded-full">{{ d.specialization }}</span>
                </td>
                <td class="px-6 py-4 text-slate-600">{{ d.experienceYears || '-' }} thn</td>
                <td class="px-6 py-4 font-medium text-slate-900">Rp{{ (d.consultationFee || 0) | number }}</td>
                <td class="px-6 py-4 text-slate-600">
                  <div class="flex items-center gap-1.5">
                    <i class="bi bi-star-fill text-amber-500"></i>
                    <span class="font-bold">{{ d.averageRating ? (d.averageRating | number:'1.1-1') : '0.0' }}</span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </app-dashboard-layout>
  `
})
export class AdminDoctorsComponent implements OnInit {
  private doctorService = inject(DoctorService);

  doctors: DoctorListResponse[] = [];
  loading = false;

  ngOnInit(): void {
    this.loading = true;
    this.doctorService.getAllDoctors().subscribe({
      next: doctors => { this.doctors = doctors; this.loading = false; },
      error: () => this.loading = false
    });
  }
}
