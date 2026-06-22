import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../shared/layout/navbar.component';
import { FooterComponent } from '../../shared/layout/footer.component';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, FooterComponent],
  template: `
    <app-navbar />

    <section class="bg-gradient-hero text-white py-24 px-6 text-center relative overflow-hidden">
      <div class="absolute inset-0 bg-[url('https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?auto=format&fit=crop&w=1200&q=80')] opacity-10 bg-cover bg-center"></div>
      <div class="max-w-4xl mx-auto relative space-y-6">
        <span class="inline-flex px-3.5 py-1.5 bg-white/15 text-white font-bold text-xs rounded-full uppercase backdrop-blur-sm">Tentang Klinik Kami</span>
        <h1 class="text-4xl lg:text-5xl font-black leading-tight">Mengenal Lebih Dekat<br>Klinik Kesehatan Terpercaya</h1>
        <p class="text-blue-100 text-sm max-w-2xl mx-auto">Visi, misi, dan komitmen kami dalam memberikan pelayanan medis terbaik untuk masyarakat Indonesia.</p>
      </div>
    </section>

    <section class="py-20 px-6 bg-white">
      <div class="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-16 items-center">
        <div class="space-y-8">
          <div class="space-y-4">
            <h2 class="text-3xl font-extrabold text-slate-900">Visi Kami</h2>
            <p class="text-slate-600 text-sm leading-relaxed">Menjadi penyedia layanan kesehatan digital dan fisik terintegrasi yang terkemuka di Indonesia, memberikan diagnosis presisi dengan kepedulian yang tulus.</p>
          </div>
          <div class="space-y-4">
            <h2 class="text-3xl font-extrabold text-slate-900">Misi Kami</h2>
            <ul class="space-y-4 text-sm text-slate-600">
              <li class="flex items-start gap-3">
                <span class="w-6 h-6 bg-blue-100 text-blue-600 flex items-center justify-center rounded-full flex-shrink-0 mt-0.5"><i class="bi bi-check text-sm"></i></span>
                <span>Menyediakan akses mudah bagi pasien untuk berkonsultasi secara instan dengan dokter terakreditasi resmi.</span>
              </li>
              <li class="flex items-start gap-3">
                <span class="w-6 h-6 bg-blue-100 text-blue-600 flex items-center justify-center rounded-full flex-shrink-0 mt-0.5"><i class="bi bi-check text-sm"></i></span>
                <span>Mengintegrasikan rekam medis digital (EHR) secara aman guna menunjang keputusan klinis yang cepat dan tepat.</span>
              </li>
              <li class="flex items-start gap-3">
                <span class="w-6 h-6 bg-blue-100 text-blue-600 flex items-center justify-center rounded-full flex-shrink-0 mt-0.5"><i class="bi bi-check text-sm"></i></span>
                <span>Meningkatkan efisiensi administrasi klinik untuk mengurangi waktu tunggu pasien.</span>
              </li>
            </ul>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <div class="bg-slate-100 rounded-card-lg h-56 overflow-hidden shadow-card">
            <img src="https://images.unsplash.com/photo-1584515901367-f1c27b74b7c8?auto=format&fit=crop&w=600&q=80" alt="Clinic" class="w-full h-full object-cover">
          </div>
          <div class="bg-slate-100 rounded-card-lg h-56 overflow-hidden shadow-card mt-8">
            <img src="https://images.unsplash.com/photo-1559839734-2b71ea197ec2?auto=format&fit=crop&w=600&q=80" alt="Doctor" class="w-full h-full object-cover">
          </div>
          <div class="bg-slate-100 rounded-card-lg h-56 overflow-hidden shadow-card -mt-8">
            <img src="https://images.unsplash.com/photo-1629909613654-28e377c37b09?auto=format&fit=crop&w=600&q=80" alt="Facility" class="w-full h-full object-cover">
          </div>
          <div class="bg-slate-100 rounded-card-lg h-56 overflow-hidden shadow-card">
            <img src="https://images.unsplash.com/photo-1579154261294-a101a24d0e93?auto=format&fit=crop&w=600&q=80" alt="Lab" class="w-full h-full object-cover">
          </div>
        </div>
      </div>
    </section>

    <section class="py-20 px-6 bg-slate-50">
      <div class="max-w-7xl mx-auto text-center space-y-12">
        <div class="space-y-4">
          <span class="px-3.5 py-1.5 bg-blue-50 text-blue-600 font-bold text-xs rounded-full uppercase">Nilai-Nilai Kami</span>
          <h2 class="text-3xl font-extrabold text-slate-900">Prinsip Yang Kami Pegang Teguh</h2>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover transition-all">
            <div class="w-14 h-14 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mx-auto mb-5">
              <i class="bi bi-heart text-2xl"></i>
            </div>
            <h3 class="font-bold text-slate-900 mb-3">Empati & Kepedulian</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Setiap pasien adalah prioritas utama. Kami mendengarkan dan memberikan perawatan dengan penuh empati.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover transition-all">
            <div class="w-14 h-14 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mx-auto mb-5">
              <i class="bi bi-award text-2xl"></i>
            </div>
            <h3 class="font-bold text-slate-900 mb-3">Profesionalisme</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Standar medis tertinggi dengan tenaga kesehatan berlisensi dan berpengalaman.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover transition-all">
            <div class="w-14 h-14 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mx-auto mb-5">
              <i class="bi bi-shield-check text-2xl"></i>
            </div>
            <h3 class="font-bold text-slate-900 mb-3">Transparansi</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Biaya jelas, diagnosis terbuka, dan rekam medis dapat diakses kapan saja oleh pasien.</p>
          </div>
        </div>
      </div>
    </section>

    <app-footer />
  `
})
export class AboutComponent {}
