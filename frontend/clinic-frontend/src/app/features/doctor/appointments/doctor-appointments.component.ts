import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentService } from '../../../core/services/appointment.service';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { AppointmentResponse } from '../../../core/models/appointment/appointment-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';
import { SkeletonComponent } from '../../../shared/components/skeleton.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state.component';
import { ToastService } from '../../../shared/components/toast.service';
import { forkJoin } from 'rxjs';

import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-doctor-appointments',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent, SkeletonComponent, EmptyStateComponent],
  templateUrl: './doctor-appointments.component.html'
})
export class DoctorAppointmentsComponent implements OnInit {
  private appointmentService = inject(AppointmentService);
  private medicalRecordService = inject(MedicalRecordService);
  private toast = inject(ToastService);

  appointments: AppointmentResponse[] = [];
  filteredAppointments: AppointmentResponse[] = [];
  loading = false;
  activeTab = 'all';

  tabs = [
    { key: 'all', label: 'Semua' },
    { key: 'PAID', label: 'Perlu Penanganan' },
    { key: 'COMPLETED', label: 'Selesai' }
  ];

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.loading = true;
    forkJoin({
      appointments: this.appointmentService.getDoctorAppointments(),
      medicalRecords: this.medicalRecordService.getDoctorMedicalRecords()
    }).subscribe({
      next: (res) => {
        const completedApptIds = new Set(res.medicalRecords.map(r => r.appointmentId));
        this.appointments = res.appointments.map(a => ({
          ...a,
          hasMedicalRecord: completedApptIds.has(a.id) || a.status === 'COMPLETED'
        }));
        this.filterAppointments();
        this.loading = false;
      },
      error: () => {
        this.appointmentService.getDoctorAppointments().subscribe({
          next: appointments => {
            this.appointments = appointments.map(a => ({
              ...a,
              hasMedicalRecord: a.status === 'COMPLETED'
            }));
            this.filterAppointments();
            this.loading = false;
          },
          error: () => this.loading = false
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

  approveAppointment(id: number): void {
    this.appointmentService.updateStatus(id, { status: 'APPROVED' }).subscribe({
      next: () => {
        this.toast.success('Janji temu disetujui');
        this.loadAppointments();
      },
      error: () => this.toast.error('Gagal menyetujui janji temu')
    });
  }

  cancelAppointment(id: number): void {
    this.appointmentService.updateStatus(id, { status: 'CANCELLED' }).subscribe({
      next: () => {
        this.toast.warning('Janji temu ditolak');
        this.loadAppointments();
      },
      error: () => this.toast.error('Gagal menolak janji temu')
    });
  }

  completeAppointment(id: number): void {
    this.appointmentService.updateStatus(id, { status: 'COMPLETED' }).subscribe({
      next: () => {
        this.toast.success('Janji temu selesai');
        this.loadAppointments();
      },
      error: () => this.toast.error('Gagal menyelesaikan janji temu')
    });
  }

  statusClass(status: string): string {
    switch (status) {
      case 'PAID': return 'bg-blue-50 text-blue-600';
      case 'COMPLETED': return 'bg-emerald-50 text-emerald-600';
      default: return 'bg-slate-50 text-slate-600';
    }
  }
}
