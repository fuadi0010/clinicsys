import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../shared/layout/navbar.component';
import { FooterComponent } from '../../shared/layout/footer.component';

@Component({
  selector: 'app-contact-page',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, FooterComponent],
  template: `
    <app-navbar />

    <section class="bg-gradient-hero text-white py-24 px-6 text-center relative overflow-hidden">
      <div class="max-w-4xl mx-auto space-y-6">
        <span class="inline-flex px-3.5 py-1.5 bg-white/15 text-white font-bold text-xs rounded-full uppercase backdrop-blur-sm">Hubungi Kami</span>
        <h1 class="text-4xl lg:text-5xl font-black leading-tight">Kami Siap Mendengarkan<br>Kebutuhan Kesehatan Anda</h1>
        <p class="text-blue-100 text-sm max-w-2xl mx-auto">Ada pertanyaan atau kendala? Hubungi tim admin kami secara langsung.</p>
      </div>
    </section>

    <section class="py-20 px-6 bg-white">
      <div class="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-2 gap-16">
        <div class="space-y-8">
          <div class="space-y-3">
            <h2 class="text-3xl font-extrabold text-slate-900">Saluran Komunikasi Resmi</h2>
            <p class="text-slate-500 text-sm">Berikut adalah alamat klinik fisik dan saluran telekomunikasi aktif kami.</p>
          </div>
          <div class="space-y-6">
            <div class="flex gap-4">
              <div class="w-14 h-14 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl flex-shrink-0">
                <i class="bi bi-geo-alt text-xl"></i>
              </div>
              <div>
                <h4 class="font-bold text-slate-900">Alamat Fisik</h4>
                <p class="text-xs text-slate-500 mt-1">Jl. Kesehatan No. 45, Blok A, Jakarta Selatan, Indonesia</p>
              </div>
            </div>
            <div class="flex gap-4">
              <div class="w-14 h-14 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl flex-shrink-0">
                <i class="bi bi-telephone text-xl"></i>
              </div>
              <div>
                <h4 class="font-bold text-slate-900">Telepon & WhatsApp</h4>
                <p class="text-xs text-slate-500 mt-1">+62-21-555-8899 / WhatsApp: +62-812-3456-7890</p>
              </div>
            </div>
            <div class="flex gap-4">
              <div class="w-14 h-14 bg-blue-50 text-blue-600 flex items-center justify-center rounded-xl flex-shrink-0">
                <i class="bi bi-envelope text-xl"></i>
              </div>
              <div>
                <h4 class="font-bold text-slate-900">Email Korespondensi</h4>
                <p class="text-xs text-slate-500 mt-1">kontak&#64;kliniksys.com / info&#64;kliniksys.com</p>
              </div>
            </div>
          </div>
        </div>
        <div class="bg-slate-50 p-8 rounded-card-lg border border-slate-200/60 shadow-premium">
          <h3 class="font-extrabold text-xl text-slate-900 mb-6">Kirim Pesan Langsung</h3>
          <form class="space-y-5">
            <div>
              <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-2">Nama Lengkap</label>
              <input type="text" class="w-full px-4 py-3 bg-white border border-slate-200 rounded-input text-sm focus:ring-4 focus:ring-blue-100 focus:border-blue-600 transition-all" placeholder="Masukkan nama lengkap">
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-2">Alamat Email</label>
              <input type="email" class="w-full px-4 py-3 bg-white border border-slate-200 rounded-input text-sm focus:ring-4 focus:ring-blue-100 focus:border-blue-600 transition-all" placeholder="Masukkan email">
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-2">Pesan Anda</label>
              <textarea rows="4" class="w-full px-4 py-3 bg-white border border-slate-200 rounded-input text-sm focus:ring-4 focus:ring-blue-100 focus:border-blue-600 transition-all" placeholder="Tulis pesan Anda di sini..."></textarea>
            </div>
            <button type="submit" class="w-full py-3.5 bg-blue-600 text-white font-bold rounded-button shadow-md shadow-blue-600/20 hover:bg-blue-700 hover:scale-[1.01] active:scale-[0.99] transition-all">
              <i class="bi bi-send mr-2"></i> Kirim Pesan
            </button>
          </form>
        </div>
      </div>
    </section>

    <app-footer />
  `
})
export class ContactComponent {}
