import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { PatientService } from '../../../core/services/patient.service';
import { DoctorService } from '../../../core/services/doctor.service';
import { ToastService } from '../../../shared/components/toast.service';
import { MedicalRecordResponse } from '../../../core/models/medical-record/medical-record-response.model';
import { DoctorListResponse } from '../../../core/models/doctor/doctor-list-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';

@Component({
  selector: 'app-patient-medical-records',
  standalone: true,
  imports: [CommonModule, FormsModule, DashboardLayoutComponent],
  templateUrl: './patient-medical-records.component.html'
})
export class PatientMedicalRecordsComponent implements OnInit {
  private service = inject(MedicalRecordService);
  private patientService = inject(PatientService);
  private doctorService = inject(DoctorService);
  private toast = inject(ToastService);

  records: MedicalRecordResponse[] = [];
  doctors: DoctorListResponse[] = [];
  doctorMap: { [key: number]: DoctorListResponse } = {};
  
  // Rating States
  showRatingModal = false;
  ratingValue = 0;
  reviewText = '';
  selectedRecord: MedicalRecordResponse | null = null;
  submittingRating = false;
  ratedAppointmentsMap: { [appointmentId: number]: number } = {};

  ngOnInit(): void {
    this.loadDoctors();
    this.loadRecords();
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

  private loadRecords(): void {
    this.service.getPatientMedicalRecords().subscribe({
      next: records => this.records = records
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

  hasBeenRated(appointmentId: number): boolean {
    return this.ratedAppointmentsMap[appointmentId] !== undefined;
  }

  getRatingValue(appointmentId: number): number {
    return this.ratedAppointmentsMap[appointmentId] || 0;
  }

  getDoctorName(doctorId: number): string {
    return this.doctorMap[doctorId]?.fullName ?? `Dokter #${doctorId}`;
  }

  openRatingModal(record: MedicalRecordResponse): void {
    this.selectedRecord = record;
    this.ratingValue = 0;
    this.reviewText = '';
    this.showRatingModal = true;
  }

  closeRatingModal(): void {
    this.showRatingModal = false;
    this.selectedRecord = null;
  }

  selectRatingValue(val: number): void {
    this.ratingValue = val;
  }

  submitRating(): void {
    if (!this.selectedRecord || this.ratingValue === 0) return;
    this.submittingRating = true;

    const payload = {
      appointmentId: this.selectedRecord.appointmentId,
      doctorId: this.selectedRecord.doctorId,
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
