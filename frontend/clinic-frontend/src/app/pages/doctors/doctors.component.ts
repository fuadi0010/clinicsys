import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DoctorService } from '../../core/services/doctor.service';
import { DoctorListResponse } from '../../core/models/doctor/doctor-list-response.model';
import { NavbarComponent } from '../../shared/layout/navbar.component';
import { FooterComponent } from '../../shared/layout/footer.component';

@Component({
  selector: 'app-doctors-directory',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, NavbarComponent, FooterComponent],
  template: `
    <app-navbar />

    <!-- Header -->
    <div class="bg-gradient-to-br from-blue-900 to-blue-700 text-white py-16 px-6 text-center">
      <div class="max-w-4xl mx-auto space-y-4">
        <h1 class="text-4xl font-black">Direktori Dokter Spesialis</h1>
        <p class="text-blue-100 text-sm max-w-2xl mx-auto">Cari dan temukan jadwal praktik dokter spesialis berlisensi resmi untuk berkonsultasi secara aman.</p>
        <div class="pt-4">
          <a routerLink="/" class="text-white hover:underline text-xs"><i class="bi bi-arrow-left"></i> Kembali ke Beranda</a>
        </div>
      </div>
    </div>

    <!-- Search and Filter Panel -->
    <div class="max-w-7xl mx-auto py-12 px-6 space-y-8">
      <div class="bg-white p-6 rounded-xl border border-slate-100 shadow-sm flex flex-wrap gap-4 items-center justify-between">
        <div class="relative w-full md:w-80">
          <i class="bi bi-search absolute left-4 top-3.5 text-slate-400"></i>
          <input type="text" [(ngModel)]="searchQuery" (input)="filterDoctors()" class="w-full pl-10 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:ring-4 focus:ring-blue-100 focus:border-blue-600 transition-all" placeholder="Cari nama dokter...">
        </div>
        
        <div class="flex gap-4 w-full md:w-auto">
          <select [(ngModel)]="selectedSpecialization" (change)="filterDoctors()" class="flex-1 md:flex-none px-4 py-3 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:ring-4 focus:ring-blue-100 focus:border-blue-600 transition-all">
            <option value="">Semua Spesialisasi</option>
            <option value="DENTIST">Spesialis Gigi</option>
            <option value="CARDIOLOGIST">Spesialis Jantung</option>
            <option value="PEDIATRICIAN">Spesialis Anak</option>
            <option value="GENERAL">Dokter Umum</option>
          </select>
        </div>
      </div>

      <!-- Doctors Grid -->
      <div *ngIf="loading" class="text-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
        <p class="text-xs text-slate-500 mt-4">Memuat data dokter...</p>
      </div>

      <div *ngIf="!loading && filteredDoctors.length === 0" class="text-center py-12 bg-white rounded-xl border border-slate-100">
        <i class="bi bi-person-slash text-4xl text-slate-300"></i>
        <h4 class="font-bold text-slate-900 mt-4">Dokter Tidak Ditemukan</h4>
        <p class="text-xs text-slate-500 mt-1">Coba gunakan kata kunci pencarian atau spesialisasi yang lain.</p>
      </div>

      <div *ngIf="!loading && filteredDoctors.length > 0" class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div *ngFor="let doc of filteredDoctors" class="bg-white rounded-premium border border-slate-100 overflow-hidden shadow-sm hover:shadow-md transition-all flex flex-col justify-between">
          <div class="h-64 bg-slate-100 overflow-hidden">
            <img [src]="getDoctorImageUrl(doc.profileImageUrl)" alt="Doctor Image" class="w-full h-full object-cover">
          </div>
          <div class="p-6 space-y-4">
            <div>
              <h4 class="font-extrabold text-lg text-slate-900">{{ doc.fullName }}</h4>
              <p class="text-xs text-blue-600 font-bold mt-1">{{ doc.specialization }}</p>
              <div class="flex items-center gap-0.5 mt-2">
                <i *ngFor="let star of [1,2,3,4,5]" class="bi" 
                   [class.bi-star-fill]="star <= (doc.averageRating || 0)" 
                   [class.bi-star]="star > (doc.averageRating || 0)" 
                   class="text-xs text-amber-500"></i>
              </div>
            </div>
            <div class="flex items-center gap-6 text-xs text-slate-500 font-medium border-t border-b border-slate-100 py-3">
              <span><i class="bi bi-briefcase text-blue-600 mr-1.5"></i> {{ doc.experienceYears || '5+' }} Thn Pengalaman</span>
              <span><i class="bi bi-star-fill text-amber-500 mr-1.5"></i> {{ doc.averageRating ? (doc.averageRating | number:'1.1-1') : '0.0' }} Rating</span>
            </div>
            <div class="flex items-center justify-between gap-4 pt-1">
              <a [routerLink]="['/doctors', doc.id]" class="flex-1 py-2.5 text-center text-xs font-bold text-slate-700 border border-slate-200 rounded-lg hover:bg-slate-50 transition-all">Lihat Profil</a>
              <a routerLink="/register" class="flex-1 py-2.5 text-center text-xs font-bold text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-all">Booking</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <app-footer />
  `
})
export class DoctorsComponent implements OnInit {
  private doctorService = inject(DoctorService);

  doctors: DoctorListResponse[] = [];
  filteredDoctors: DoctorListResponse[] = [];
  searchQuery = '';
  selectedSpecialization = '';
  loading = false;

  ngOnInit(): void {
    this.loadDoctors();
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

  loadDoctors(): void {
    this.loading = true;
    this.doctorService.getAllDoctors().subscribe({
      next: (data) => {
        this.doctors = data;
        this.filteredDoctors = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load doctors', err);
        this.loading = false;
      }
    });
  }

  filterDoctors(): void {
    this.filteredDoctors = this.doctors.filter(doc => {
      const matchesSearch = doc.fullName.toLowerCase().includes(this.searchQuery.toLowerCase());
      const matchesSpec = this.selectedSpecialization ? doc.specialization === this.selectedSpecialization : true;
      return matchesSearch && matchesSpec;
    });
  }
}
