import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../shared/layout/navbar.component';
import { FooterComponent } from '../../shared/layout/footer.component';

@Component({
  selector: 'app-services-page',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, FooterComponent],
  template: `
    <app-navbar />

    <section class="bg-gradient-hero text-white py-24 px-6 text-center relative overflow-hidden">
      <div class="max-w-4xl mx-auto space-y-6">
        <span class="inline-flex px-3.5 py-1.5 bg-white/15 text-white font-bold text-xs rounded-full uppercase backdrop-blur-sm">Layanan Medis</span>
        <h1 class="text-4xl lg:text-5xl font-black leading-tight">Solusi Kesehatan Lengkap<br>Untuk Seluruh Keluarga</h1>
        <p class="text-blue-100 text-sm max-w-2xl mx-auto">Berbagai layanan medis bersertifikat dengan tenaga ahli berdedikasi tinggi untuk kesembuhan optimal Anda.</p>
      </div>
    </section>

    <section class="py-20 px-6 bg-white">
      <div class="max-w-7xl mx-auto space-y-12">
        <div class="text-center max-w-2xl mx-auto space-y-4">
          <h2 class="text-3xl font-extrabold text-slate-900">Layanan Unggulan Kami</h2>
          <p class="text-slate-500 text-sm">Kami menyediakan berbagai layanan medis dengan standar operasional tinggi.</p>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all group">
            <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-5 group-hover:bg-blue-600 group-hover:text-white transition-all">
              <i class="bi bi-activity text-2xl"></i>
            </div>
            <h3 class="font-bold text-lg text-slate-900 mb-2">Poli Umum</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Pemeriksaan dan diagnosis gangguan kesehatan umum, konsultasi medis rutin, rujukan medis, serta resep pengobatan dasar.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all group">
            <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-5 group-hover:bg-blue-600 group-hover:text-white transition-all">
              <i class="bi bi-person-fill text-2xl"></i>
            </div>
            <h3 class="font-bold text-lg text-slate-900 mb-2">Spesialis Penyakit Dalam</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Penanganan penyakit kronis non-bedah seperti diabetes, hipertensi, gangguan pencernaan akut, dan gangguan pernapasan.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all group">
            <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-5 group-hover:bg-blue-600 group-hover:text-white transition-all">
              <i class="bi bi-emoji-smile text-2xl"></i>
            </div>
            <h3 class="font-bold text-lg text-slate-900 mb-2">Spesialis Anak (Pediatri)</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Pemantauan kesehatan bayi dan anak, tumbuh kembang, imunisasi wajib, nutrisi gizi, serta pengobatan demam anak.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all group">
            <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-5 group-hover:bg-blue-600 group-hover:text-white transition-all">
              <i class="bi bi-heart-pulse text-2xl"></i>
            </div>
            <h3 class="font-bold text-lg text-slate-900 mb-2">Poli Kardiologi</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Skrining kesehatan jantung, EKG, konsultasi penyakit jantung koroner, hipertensi kronis, dan perawatan kardiovaskular.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all group">
            <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-5 group-hover:bg-blue-600 group-hover:text-white transition-all">
              <i class="bi bi-droplet-half text-2xl"></i>
            </div>
            <h3 class="font-bold text-lg text-slate-900 mb-2">Laboratorium Klinik</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Uji darah rutin, tes urin, skrining kadar kolesterol dan gula darah, serta pemeriksaan laboratorium pendukung lainnya.</p>
          </div>
          <div class="bg-white border border-slate-100 p-8 rounded-card shadow-card hover:shadow-card-hover hover:border-blue-200 transition-all group">
            <div class="w-12 h-12 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl mb-5 group-hover:bg-blue-600 group-hover:text-white transition-all">
              <i class="bi bi-prescription2 text-2xl"></i>
            </div>
            <h3 class="font-bold text-lg text-slate-900 mb-2">Apotek & Farmasi</h3>
            <p class="text-xs text-slate-500 leading-relaxed">Penyediaan resep obat terstandar, obat generik dan paten, suplemen multivitamin, serta konsultasi kefarmasian.</p>
          </div>
        </div>
      </div>
    </section>

    <app-footer />
  `
})
export class ServicesComponent {}
