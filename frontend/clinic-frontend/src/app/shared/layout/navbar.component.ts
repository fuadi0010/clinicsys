import { Component, inject, HostListener, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="sticky top-0 z-50 bg-white/95 backdrop-blur-md border-b border-slate-100 shadow-sm transition-all duration-300">
      <div class="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
        <!-- Logo -->
        <a routerLink="/" class="flex items-center gap-3">
          <div class="bg-blue-600 text-white p-2 rounded-xl shadow-lg shadow-blue-500/30">
            <i class="bi bi-shield-plus text-2xl"></i>
          </div>
          <span class="font-extrabold text-xl text-slate-900 tracking-tight">CLINIC<span class="text-blue-600">SYS</span></span>
        </a>

        <!-- Desktop Navigation -->
        <div class="hidden lg:flex items-center gap-8 font-semibold text-slate-600">
          <a routerLink="/" routerLinkActive="text-blue-600" [routerLinkActiveOptions]="{exact: true}" class="hover:text-blue-600 transition-colors">Beranda</a>
          <a routerLink="/about" routerLinkActive="text-blue-600" class="hover:text-blue-600 transition-colors">Tentang Kami</a>
          <a routerLink="/services" routerLinkActive="text-blue-600" class="hover:text-blue-600 transition-colors">Layanan</a>
          <a routerLink="/doctors" routerLinkActive="text-blue-600" class="hover:text-blue-600 transition-colors">Dokter</a>
          <a routerLink="/contact" routerLinkActive="text-blue-600" class="hover:text-blue-600 transition-colors">Kontak</a>
        </div>

        <!-- Right Side: User Profile / Auth Actions & Hamburger -->
        <div class="flex items-center gap-3">
          <!-- Auth Actions -->
          <div class="flex items-center gap-3" *ngIf="!isLoggedIn">
            <a routerLink="/login" class="px-4 py-2.5 font-bold text-slate-700 hover:text-blue-600 transition-all">Masuk</a>
            <a routerLink="/register" class="hidden sm:flex px-5 py-2.5 bg-blue-600 text-white font-bold rounded-xl shadow-md shadow-blue-600/20 hover:bg-blue-700 hover:scale-[1.02] active:scale-[0.98] transition-all">Daftar Akun</a>
          </div>

          <!-- User Dropdown (Authenticated) -->
          <div *ngIf="isLoggedIn" class="relative">
            <button (click)="toggleDropdown($event)" class="flex items-center gap-2.5 px-2.5 py-1.5 rounded-xl hover:bg-slate-100 transition-all focus:outline-none">
              <!-- Avatar -->
              <div class="w-9 h-9 bg-gradient-to-br from-blue-500 to-blue-600 text-white flex items-center justify-center rounded-xl font-bold text-sm shadow-md shadow-blue-500/20">
                {{ displayInitial }}
              </div>
              <!-- User Info -->
              <div class="hidden md:block text-left">
                <p class="text-xs font-bold text-slate-900 leading-tight">{{ username }}</p>
                <p class="text-[9px] text-slate-500 font-semibold uppercase tracking-wider mt-0.5">{{ userRole }}</p>
              </div>
              <i class="bi bi-chevron-down text-slate-400 text-[10px] transition-transform duration-200" [class.rotate-180]="dropdownOpen()"></i>
            </button>

            <!-- Dropdown Menu -->
            <div *ngIf="dropdownOpen()" class="absolute right-0 mt-2 w-56 bg-white rounded-2xl border border-slate-100 shadow-xl py-2 z-50 animate-in fade-in slide-in-from-top-3 duration-200">
              <div class="px-4 py-2 border-b border-slate-50">
                <p class="text-[10px] text-slate-400">Masuk sebagai</p>
                <p class="text-xs font-bold text-slate-800 truncate">{{ username }}</p>
              </div>
              <a [routerLink]="profileUrl" (click)="closeDropdown()" class="flex items-center gap-3 px-4 py-2.5 text-xs font-medium text-slate-700 hover:bg-slate-50 hover:text-blue-600 transition-all">
                <i class="bi bi-person text-base text-slate-400"></i>
                <span>Profil Saya</span>
              </a>
              <a [routerLink]="appointmentsUrl" (click)="closeDropdown()" class="flex items-center gap-3 px-4 py-2.5 text-xs font-medium text-slate-700 hover:bg-slate-50 hover:text-blue-600 transition-all">
                <i class="bi bi-calendar-check text-base text-slate-400"></i>
                <span>Janji Temu Saya</span>
              </a>
              <div class="h-px bg-slate-100 my-1"></div>
              <button (click)="logout()" class="w-full flex items-center gap-3 px-4 py-2.5 text-xs font-semibold text-red-600 hover:bg-red-50 transition-all text-left">
                <i class="bi bi-box-arrow-right text-base"></i>
                <span>Keluar</span>
              </button>
            </div>
          </div>

          <!-- Hamburger Button (Mobile Only) -->
          <button (click)="toggleMobileMenu($event)" class="lg:hidden w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 transition-all focus:outline-none" aria-label="Toggle Menu">
            <i class="bi text-xl text-slate-700" [class.bi-list]="!mobileMenuOpen()" [class.bi-x-lg]="mobileMenuOpen()"></i>
          </button>
        </div>
      </div>

      <!-- Mobile Navigation Drawer -->
      <div *ngIf="mobileMenuOpen()" (click)="$event.stopPropagation()" class="lg:hidden border-t border-slate-100 bg-white/95 backdrop-blur-md px-6 py-4 space-y-3 shadow-inner z-40 transition-all duration-300">
        <div class="flex flex-col space-y-3 font-semibold text-slate-600">
          <a routerLink="/" routerLinkActive="text-blue-600 text-slate-900 bg-blue-50/50" [routerLinkActiveOptions]="{exact: true}" (click)="closeMobileMenu()" class="px-4 py-2.5 rounded-xl hover:bg-slate-50 hover:text-blue-600 transition-all">Beranda</a>
          <a routerLink="/about" routerLinkActive="text-blue-600 text-slate-900 bg-blue-50/50" (click)="closeMobileMenu()" class="px-4 py-2.5 rounded-xl hover:bg-slate-50 hover:text-blue-600 transition-all">Tentang Kami</a>
          <a routerLink="/services" routerLinkActive="text-blue-600 text-slate-900 bg-blue-50/50" (click)="closeMobileMenu()" class="px-4 py-2.5 rounded-xl hover:bg-slate-50 hover:text-blue-600 transition-all">Layanan</a>
          <a routerLink="/doctors" routerLinkActive="text-blue-600 text-slate-900 bg-blue-50/50" (click)="closeMobileMenu()" class="px-4 py-2.5 rounded-xl hover:bg-slate-50 hover:text-blue-600 transition-all">Dokter</a>
          <a routerLink="/contact" routerLinkActive="text-blue-600 text-slate-900 bg-blue-50/50" (click)="closeMobileMenu()" class="px-4 py-2.5 rounded-xl hover:bg-slate-50 hover:text-blue-600 transition-all">Kontak</a>
        </div>
        
        <!-- Auth Actions (Mobile Only under drawer) -->
        <div class="pt-4 border-t border-slate-100 flex flex-col gap-2.5" *ngIf="!isLoggedIn">
          <a routerLink="/login" (click)="closeMobileMenu()" class="w-full py-3 text-center font-bold text-slate-700 border border-slate-200 rounded-xl hover:bg-slate-50 transition-all">Masuk</a>
          <a routerLink="/register" (click)="closeMobileMenu()" class="w-full py-3 text-center bg-blue-600 text-white font-bold rounded-xl shadow-md hover:bg-blue-700 transition-all">Daftar Akun</a>
        </div>
      </div>
    </nav>
  `
})
export class NavbarComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  dropdownOpen = signal(false);
  mobileMenuOpen = signal(false);

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get username(): string {
    return this.authService.getPayload()?.sub ?? 'User';
  }

  get userRole(): string {
    return this.authService.getRole() ?? 'USER';
  }

  get displayInitial(): string {
    return this.username.charAt(0).toUpperCase();
  }

  get profileUrl(): string {
    const role = this.userRole.toLowerCase();
    if (role === 'patient') return '/patient/profile';
    if (role === 'doctor') return '/dashboard/doctor';
    if (role === 'admin') return '/dashboard/admin';
    return '/';
  }

  get appointmentsUrl(): string {
    const role = this.userRole.toLowerCase();
    if (role === 'patient') return '/patient/appointments';
    if (role === 'doctor') return '/doctor/appointments';
    if (role === 'admin') return '/dashboard/admin';
    return '/';
  }

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.dropdownOpen.update(v => !v);
    this.mobileMenuOpen.set(false);
  }

  closeDropdown(): void {
    this.dropdownOpen.set(false);
  }

  toggleMobileMenu(event: Event): void {
    event.stopPropagation();
    this.mobileMenuOpen.update(v => !v);
    this.dropdownOpen.set(false);
  }

  closeMobileMenu(): void {
    this.mobileMenuOpen.set(false);
  }

  @HostListener('document:click')
  onDocumentClick(): void {
    this.closeDropdown();
    this.closeMobileMenu();
  }

  logout(): void {
    if (confirm('Apakah Anda yakin ingin keluar?')) {
      this.authService.logout();
      this.closeDropdown();
      this.closeMobileMenu();
      this.router.navigate(['/login']);
    }
  }
}
