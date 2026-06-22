import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { MedicalRecordResponse } from '../../../core/models/medical-record/medical-record-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state.component';

@Component({
  selector: 'app-doctor-medical-records',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent, EmptyStateComponent],
  templateUrl: './doctor-medical-records.component.html'
})
export class DoctorMedicalRecordsComponent implements OnInit {
  private service = inject(MedicalRecordService);

  records: MedicalRecordResponse[] = [];

  ngOnInit(): void {
    this.service.getDoctorMedicalRecords().subscribe({
      next: records => this.records = records
    });
  }
}
