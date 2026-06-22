import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DoctorService } from '../../../core/services/doctor.service';
import { DoctorDetailResponse } from '../../../core/models/doctor/doctor-detail-response.model';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';

@Component({
  selector: 'app-doctor-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardLayoutComponent],
  templateUrl: './doctor-detail.component.html'
})
export class DoctorDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private doctorService = inject(DoctorService);

  doctor: DoctorDetailResponse | null = null;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (!idParam) return;
    this.doctorService.getDoctorById(Number(idParam)).subscribe({
      next: doctor => this.doctor = doctor
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

  createAppointment(): void {
    if (!this.doctor) return;
    this.router.navigate(['/patient/appointments/create'], {
      queryParams: { doctorId: this.doctor.id }
    });
  }
}
