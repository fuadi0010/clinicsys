import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterModule],
  template: `
    <footer class="bg-slate-900 text-slate-300 py-16 px-6 border-t border-slate-800">
      <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-4 gap-12 pb-12 border-b border-slate-800">
        <div class="space-y-4">
          <div class="flex items-center gap-3 text-white">
            <i class="bi bi-shield-plus text-3xl text-blue-500"></i>
            <span class="font-extrabold text-xl tracking-tight">CLINICSYS</span>
          </div>
          <p class="text-xs text-slate-400 leading-relaxed">Layanan portal kesehatan modern untuk konsultasi medis dan pengelolaan rekam medis digital Anda secara aman.</p>
        </div>
        <div>
          <h5 class="font-extrabold text-white text-sm mb-4">Quick Links</h5>
          <ul class="space-y-2.5 text-xs text-slate-400">
            <li><a routerLink="/" class="hover:text-blue-500 transition-all">Beranda</a></li>
            <li><a routerLink="/about" class="hover:text-blue-500 transition-all">Tentang Klinik</a></li>
            <li><a routerLink="/services" class="hover:text-blue-500 transition-all">Layanan</a></li>
            <li><a routerLink="/doctors" class="hover:text-blue-500 transition-all">Cari Dokter</a></li>
          </ul>
        </div>
        <div>
          <h5 class="font-extrabold text-white text-sm mb-4">Layanan Medis</h5>
          <ul class="space-y-2.5 text-xs text-slate-400">
            <li>Poli Umum</li>
            <li>Spesialis Penyakit Dalam</li>
            <li>Spesialis Anak (Pediatrik)</li>
            <li>Laboratorium & Diagnosis</li>
          </ul>
        </div>
        <div>
          <h5 class="font-extrabold text-white text-sm mb-4">Jam Layanan</h5>
          <p class="text-xs text-slate-400 leading-relaxed">
            Senin - Sabtu: 08:00 - 20:00 WIB<br>
            Minggu: Tutup (Hanya Layanan UGD)<br>
            UGD 24 Jam Aktif
          </p>
        </div>
      </div>
      <div class="max-w-7xl mx-auto pt-8 flex flex-col md:flex-row justify-between items-center text-xs text-slate-500">
        <p>&copy; 2026 Clinic Management System. Seluruh hak cipta dilindungi undang-undang.</p>
        <div class="flex gap-4 mt-4 md:mt-0">
          <a href="#" class="hover:text-blue-500"><i class="bi bi-facebook text-lg"></i></a>
          <a href="#" class="hover:text-blue-500"><i class="bi bi-twitter-x text-lg"></i></a>
          <a href="#" class="hover:text-blue-500"><i class="bi bi-instagram text-lg"></i></a>
        </div>
      </div>
    </footer>
  `
})
export class FooterComponent {}
