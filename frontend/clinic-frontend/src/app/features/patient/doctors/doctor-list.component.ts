import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DoctorService } from '../../../core/services/doctor.service';
import { DoctorListResponse } from '../../../core/models/doctor/doctor-list-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';

@Component({
  selector: 'app-doctor-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, DashboardLayoutComponent],
  templateUrl: './doctor-list.component.html'
})
export class DoctorListComponent implements OnInit {
  private doctorService = inject(DoctorService);
  private router = inject(Router);

  doctors: DoctorListResponse[] = [];
  filteredDoctors: DoctorListResponse[] = [];
  loading = false;
  searchTerm = '';
  selectedSpecialization = '';
  specializations: string[] = [];

  ngOnInit(): void {
    this.loadDoctors();
  }

  private loadDoctors(): void {
    this.loading = true;
    this.doctorService.getAllDoctors().subscribe({
      next: doctors => {
        this.doctors = doctors;
        this.filteredDoctors = doctors;
        this.specializations = [...new Set(doctors.map(d => d.specialization).filter(Boolean))];
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  filterDoctors(): void {
    this.filteredDoctors = this.doctors.filter(doc => {
      const matchSearch = !this.searchTerm ||
        doc.fullName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        doc.specialization.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchSpec = !this.selectedSpecialization || doc.specialization === this.selectedSpecialization;
      return matchSearch && matchSpec;
    });
  }

  getDoctorImageUrl(path: string | undefined | null): string {
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

  viewDoctor(doctorId: number): void {
    this.router.navigate(['/patient/doctors', doctorId]);
  }
}
