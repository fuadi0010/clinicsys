import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientService } from '../../core/services/patient.service';
import { PatientResponse } from '../../core/models/patient/patient-response.model';
import { DashboardLayoutComponent } from '../../shared/layout/dashboard-layout.component';
import { ModalComponent } from '../../shared/components/modal.component';
import { ToastService } from '../../shared/components/toast.service';
import { SkeletonComponent } from '../../shared/components/skeleton.component';
import { EmptyStateComponent } from '../../shared/components/empty-state.component';

@Component({
  selector: 'app-admin-patients',
  standalone: true,
  imports: [CommonModule, DashboardLayoutComponent, ModalComponent, SkeletonComponent, EmptyStateComponent],
  template: `
    <app-dashboard-layout title="Data Pasien">
      <app-skeleton *ngIf="loading" [rows]="4" />

      <app-empty-state *ngIf="!loading && patients.length === 0"
        icon="bi-people"
        title="Tidak Ada Data Pasien"
        message="Belum ada pasien yang terdaftar di sistem."
      />

      <div *ngIf="!loading && patients.length > 0" class="bg-white border border-slate-100 rounded-card-lg shadow-card overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead>
              <tr class="border-b border-slate-100 bg-slate-50">
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">ID</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Nama</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Gender</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Telepon</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Goldar</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Tgl Lahir</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Status</th>
                <th class="text-left px-6 py-4 font-bold text-slate-700 text-xs uppercase tracking-wider">Aksi</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let p of patients" class="border-b border-slate-50 hover:bg-slate-50/50 transition-all">
                <td class="px-6 py-4 font-medium text-slate-900">{{ p.id }}</td>
                <td class="px-6 py-4 text-slate-600">{{ p.fullName }}</td>
                <td class="px-6 py-4">
                  <span class="inline-flex px-3 py-1 text-xs font-bold rounded-full"
                        [class]="p.gender === 'MALE' ? 'bg-blue-50 text-blue-600' : 'bg-pink-50 text-pink-600'">
                    {{ p.gender === 'MALE' ? 'Laki-laki' : 'Perempuan' }}
                  </span>
                </td>
                <td class="px-6 py-4 text-slate-600">{{ p.phoneNumber || '-' }}</td>
                <td class="px-6 py-4 text-slate-600">{{ p.bloodType || '-' }}</td>
                <td class="px-6 py-4 text-slate-600 whitespace-nowrap">{{ p.birthDate || '-' }}</td>
                <td class="px-6 py-4">
                  <span *ngIf="p.active" class="inline-flex px-3 py-1 text-xs font-bold rounded-full bg-emerald-50 text-emerald-600">
                    Aktif
                  </span>
                  <span *ngIf="!p.active" class="inline-flex px-3 py-1 text-xs font-bold rounded-full bg-red-50 text-red-600">
                    Banned
                  </span>
                </td>
                <td class="px-6 py-4">
                  <button *ngIf="p.active" (click)="openDelete(p)" class="px-3 py-1.5 text-xs font-bold text-white bg-red-500 rounded-lg hover:bg-red-600 transition-all">
                    <i class="bi bi-slash-circle mr-1"></i> Ban Akun
                  </button>
                  <span *ngIf="!p.active" class="text-xs text-slate-400 font-semibold italic">No Action</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <app-modal [open]="showModal" 
                 title="Ban Akun Pasien"
                 body="Akun login pasien ini akan dinonaktifkan secara permanen. Yakin ingin melanjutkan?"
                 icon="bi-exclamation-triangle"
                 iconBg="bg-red-50 text-red-500"
                 confirmText="Ban Akun"
                 confirmBg="bg-red-500 hover:bg-red-600"
                 cancelText="Batal"
                 (confirmed)="confirmDelete()"
                 (dismissed)="showModal = false" />

    </app-dashboard-layout>
  `
})
export class AdminPatientsComponent implements OnInit {
  private patientService = inject(PatientService);
  private toast = inject(ToastService);

  patients: PatientResponse[] = [];
  loading = false;
  showModal = false;
  private deleteTarget: PatientResponse | null = null;

  ngOnInit(): void {
    this.loadPatients();
  }

  private loadPatients(): void {
    this.loading = true;
    this.patientService.getAllPatients().subscribe({
      next: patients => { this.patients = patients; this.loading = false; },
      error: () => this.loading = false
    });
  }

  openDelete(p: PatientResponse): void {
    this.deleteTarget = p;
    this.showModal = true;
  }

  confirmDelete(): void {
    if (!this.deleteTarget) return;
    this.patientService.deletePatient(this.deleteTarget.id).subscribe({
      next: () => {
        this.toast.success('Akun pasien berhasil dinonaktifkan (banned).');
        this.showModal = false;
        this.deleteTarget = null;
        this.loadPatients();
      },
      error: () => this.toast.error('Gagal menonaktifkan pasien')
    });
  }
}
