import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { DoctorService } from '../../core/services/doctor.service';
import { DoctorDetailResponse } from '../../core/models/doctor/doctor-detail-response.model';
import { NavbarComponent } from '../../shared/layout/navbar.component';
import { FooterComponent } from '../../shared/layout/footer.component';

@Component({
  selector: 'app-doctor-detail-page',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, FooterComponent],
  template: `
    <app-navbar />

    <!-- Header -->
    <div class="bg-gradient-to-br from-blue-900 to-blue-700 text-white py-12 px-6">
      <div class="max-w-4xl mx-auto flex items-center gap-4 text-xs">
        <a routerLink="/doctors" class="text-blue-100 hover:underline">Direktori Dokter</a>
        <span class="text-blue-300">/</span>
        <span class="text-white font-bold">{{ doctor?.fullName || 'Detail Dokter' }}</span>
      </div>
    </div>

    <div class="max-w-4xl mx-auto py-12 px-6">
      <div *ngIf="loading" class="text-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
        <p class="text-xs text-slate-500 mt-4">Memuat detail profil...</p>
      </div>

      <div *ngIf="!loading && doctor" class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <!-- Foto Kiri -->
        <div class="space-y-6">
          <div class="h-80 bg-slate-100 rounded-2xl overflow-hidden border border-slate-100 shadow-sm">
            <img [src]="getDoctorImageUrl(doctor.profileImageUrl)" class="w-full h-full object-cover">
          </div>
          <div class="bg-blue-50 p-6 rounded-xl border border-blue-100 text-center space-y-4">
            <div>
              <p class="text-xs text-slate-500 font-bold uppercase">Tarif Konsultasi</p>
              <h3 class="text-2xl font-black text-blue-600 mt-1">Rp. {{ doctor.consultationFee | number }}</h3>
            </div>
            <a routerLink="/register" class="block w-full py-3 bg-blue-600 text-white font-bold rounded-lg shadow-md hover:bg-blue-700 transition-all">Pesan Jadwal Temu</a>
          </div>
        </div>

        <!-- Detail Kanan -->
        <div class="md:col-span-2 space-y-8">
          <div>
            <h2 class="text-3xl font-black text-slate-900">{{ doctor.fullName }}</h2>
            <p class="text-sm text-blue-600 font-bold mt-1">{{ doctor.specialization }}</p>
          </div>

          <div class="grid grid-cols-2 gap-4 border-t border-b border-slate-100 py-4 text-xs font-semibold text-slate-500">
            <div>
              <p class="text-slate-400">Nomor SIP / STR</p>
              <p class="text-slate-900 font-mono mt-1">{{ doctor.strNumber }}</p>
            </div>
            <div>
              <p class="text-slate-400">Pengalaman Praktek</p>
              <p class="text-slate-900 mt-1">{{ doctor.experienceYears || '5+' }} Tahun</p>
            </div>
          </div>

          <div class="space-y-3">
            <h3 class="font-extrabold text-lg text-slate-900">Alumnus / Riwayat Pendidikan</h3>
            <p class="text-sm text-slate-600 leading-relaxed">Lulusan Fakultas Kedokteran Universitas Indonesia spesialisasi terkait, dengan sertifikasi profesi tambahan nasional maupun internasional.</p>
          </div>

          <div class="space-y-3">
            <h3 class="font-extrabold text-lg text-slate-900">Jadwal Praktek Klinik</h3>
            <div class="p-4 bg-slate-50 border border-slate-100 rounded-lg text-xs space-y-2 text-slate-700">
              <div class="flex justify-between">
                <span>Senin - Rabu</span>
                <span class="font-bold">09:00 - 12:00 WIB</span>
              </div>
              <div class="flex justify-between">
                <span>Kamis - Jumat</span>
                <span class="font-bold">14:00 - 17:00 WIB</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <app-footer />
  `
})
export class DoctorDetailPageComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private doctorService = inject(DoctorService);

  doctor: DoctorDetailResponse | null = null;
  loading = false;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadDoctor(id);
    }
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

  loadDoctor(id: number): void {
    this.loading = true;
    this.doctorService.getDoctorById(id).subscribe({
      next: (data) => {
        this.doctor = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load doctor details', err);
        this.loading = false;
      }
    });
  }
}
