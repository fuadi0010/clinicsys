import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { AppointmentService } from '../../../core/services/appointment.service';
import { PatientService } from '../../../core/services/patient.service';
import { DoctorService } from '../../../core/services/doctor.service';
import { ToastService } from '../../../shared/components/toast.service';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state.component';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-create-medical-record',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, DashboardLayoutComponent, EmptyStateComponent],
  templateUrl: './create-medical-record.component.html'
})
export class CreateMedicalRecordComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private medicalRecordService = inject(MedicalRecordService);
  private appointmentService = inject(AppointmentService);
  private patientService = inject(PatientService);
  private doctorService = inject(DoctorService);
  private toast = inject(ToastService);

  loading = false;
  detailsLoading = false;
  noAppointmentSelected = false;

  appointmentId!: number;
  patientName = '';
  doctorName = '';
  appointmentDate = '';
  appointmentTime = '';

  form = this.fb.nonNullable.group({
    appointmentId: [0, Validators.required],
    complaint: ['', Validators.required],
    diagnosis: ['', Validators.required],
    treatment: [''],
    notes: ['']
  });

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const apptId = params['appointmentId'];
      if (apptId) {
        this.appointmentId = Number(apptId);
        this.form.patchValue({ appointmentId: this.appointmentId });
        this.loadAppointmentDetails(this.appointmentId);
        this.noAppointmentSelected = false;
      } else {
        this.noAppointmentSelected = true;
      }
    });
  }

  loadAppointmentDetails(id: number): void {
    this.detailsLoading = true;
    this.appointmentService.getAppointmentById(id).subscribe({
      next: (appointment) => {
        this.appointmentDate = appointment.appointmentDate;
        this.appointmentTime = appointment.appointmentTime;

        forkJoin({
          patient: this.patientService.getPatientById(appointment.patientId),
          doctor: this.doctorService.getDoctorById(appointment.doctorId)
        }).subscribe({
          next: (res) => {
            this.patientName = res.patient.fullName;
            this.doctorName = res.doctor.fullName;
            this.detailsLoading = false;
          },
          error: (err) => {
            this.toast.error('Janji temu belum ada atau kosong');
            this.detailsLoading = false;
          }
        });
      },
      error: (err) => {
        this.toast.error('Janji temu belum ada atau kosong');
        this.detailsLoading = false;
        this.router.navigate(['/doctor/appointments']);
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.medicalRecordService.createMedicalRecord(this.form.getRawValue()).subscribe({
      next: () => {
        this.toast.success('Rekam medis berhasil disimpan dan janji temu selesai');
        this.router.navigate(['/doctor/medical-records']);
      },
      error: (err) => {
        this.toast.error(err.error?.message || 'Gagal menyimpan rekam medis');
        this.loading = false;
      },
      complete: () => this.loading = false
    });
  }
}
