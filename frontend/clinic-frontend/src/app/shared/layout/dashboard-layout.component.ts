import { Component, input, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="min-h-screen bg-slate-50 flex">
      <!-- Mobile Overlay -->
      <div *ngIf="sidebarOpen()" (click)="sidebarOpen.set(false)" class="fixed inset-0 bg-black/50 z-40 lg:hidden"></div>

      <!-- Sidebar -->
      <aside class="fixed lg:static inset-y-0 left-0 z-50 w-64 bg-slate-900 text-slate-300 flex flex-col transition-transform duration-300 lg:translate-x-0" [class.-translate-x-full]="!sidebarOpen()" [class.translate-x-0]="sidebarOpen()">
        <!-- Logo -->
        <div class="h-20 flex items-center gap-3 px-6 border-b border-slate-800">
          <div class="bg-blue-600 text-white p-2 rounded-xl shadow-lg shadow-blue-500/20 flex-shrink-0">
            <i class="bi bi-shield-plus text-xl"></i>
          </div>
          <span class="font-extrabold text-lg text-white tracking-tight whitespace-nowrap">CLINIC<span class="text-blue-400">SYS</span></span>
        </div>

        <!-- Nav Items -->
        <nav class="flex-1 overflow-y-auto py-6 px-4 space-y-1.5">
          <a *ngFor="let item of navItems" [routerLink]="item.route" routerLinkActive="bg-blue-600/20 text-blue-400 border-l-blue-500" (click)="sidebarOpen.set(false)" class="flex items-center gap-3.5 px-4 py-3 rounded-xl text-sm font-medium border-l-2 border-transparent hover:bg-slate-800/60 hover:text-white transition-all">
            <i [class]="item.icon + ' text-lg w-5 text-center'"></i>
            <span>{{ item.label }}</span>
          </a>
        </nav>

        <!-- Collapse Toggle (Desktop) -->
        <button (click)="sidebarOpen.set(!sidebarOpen())" class="hidden lg:flex items-center justify-center h-12 border-t border-slate-800 text-slate-500 hover:text-white hover:bg-slate-800/40 transition-all">
          <i class="bi" [class.bi-chevron-left]="sidebarOpen()" [class.bi-chevron-right]="!sidebarOpen()"></i>
        </button>
      </aside>

      <!-- Main Content Area -->
      <div class="flex-1 flex flex-col min-h-screen">
        <!-- Topbar -->
        <header class="h-20 bg-white border-b border-slate-100 flex items-center justify-between px-6 shadow-sm">
          <div class="flex items-center gap-4">
            <button (click)="sidebarOpen.set(!sidebarOpen())" class="lg:hidden w-10 h-10 flex items-center justify-center rounded-xl hover:bg-slate-100 transition-all">
              <i class="bi bi-list text-xl text-slate-700"></i>
            </button>
            <a *ngIf="!isDashboard" [routerLink]="dashboardUrl" class="w-10 h-10 flex items-center justify-center rounded-xl bg-slate-50 hover:bg-blue-50 text-slate-500 hover:text-blue-600 border border-slate-100 hover:border-blue-200 transition-all" title="Kembali ke Halaman Utama">
              <i class="bi bi-arrow-left text-lg"></i>
            </a>
            <h1 class="text-lg font-extrabold text-slate-900">{{ title() }}</h1>
          </div>
          <div class="flex items-center gap-4">
            <div class="flex items-center gap-3">
              <img *ngIf="profileImageUrl()" [src]="profileImageUrl()" class="w-10 h-10 rounded-full object-cover border border-slate-200 shadow-sm">
              <div *ngIf="!profileImageUrl()" class="w-10 h-10 bg-blue-100 text-blue-600 flex items-center justify-center rounded-full font-bold text-sm">
                {{ displayInitial }}
              </div>
              <div class="hidden sm:block">
                <p class="text-sm font-bold text-slate-900">{{ username }}</p>
                <p class="text-xs text-slate-500 capitalize">{{ role }}</p>
              </div>
            </div>
            <button (click)="onLogout()" class="w-10 h-10 flex items-center justify-center rounded-xl text-slate-400 hover:text-red-500 hover:bg-red-50 transition-all" title="Logout">
              <i class="bi bi-box-arrow-right text-lg"></i>
            </button>
          </div>
        </header>

        <!-- Page Content -->
        <main class="flex-1 p-6 overflow-y-auto">
          <ng-content />
        </main>
      </div>
    </div>
  `
})
export class DashboardLayoutComponent {
  title = input<string>('Dashboard');
  profileImageUrl = input<string | null>(null);

  private authService = inject(AuthService);
  private router = inject(Router);

  sidebarOpen = signal(true);

  get dashboardUrl(): string {
    const role = this.role;
    if (role === 'patient') return '/dashboard/patient';
    if (role === 'doctor') return '/dashboard/doctor';
    if (role === 'admin') return '/dashboard/admin';
    return '/';
  }

  get isDashboard(): boolean {
    const currentUrl = this.router.url.split('?')[0];
    return currentUrl === '/dashboard/patient' || 
           currentUrl === '/dashboard/doctor' || 
           currentUrl === '/dashboard/admin';
  }

  get username(): string {
    return this.authService.getPayload()?.sub ?? 'User';
  }

  get role(): string {
    return this.authService.getRole()?.toLowerCase() ?? 'user';
  }

  get displayInitial(): string {
    return this.username.charAt(0).toUpperCase();
  }

  get navItems() {
    const role = this.role;
    if (role === 'patient') return this.patientNav;
    if (role === 'doctor') return this.doctorNav;
    if (role === 'admin') return this.adminNav;
    return [];
  }

  private patientNav = [
    { route: '/dashboard/patient', label: 'Dashboard', icon: 'bi bi-grid-1x2' },
    { route: '/patient/profile', label: 'Profile Saya', icon: 'bi bi-person' },
    { route: '/patient/doctors', label: 'Cari Dokter', icon: 'bi bi-search' },
    { route: '/patient/appointments', label: 'Janji Temu Saya', icon: 'bi bi-calendar-check' },
    { route: '/patient/medical-records', label: 'Rekam Medis', icon: 'bi bi-file-earmark-medical' }
  ];

  private doctorNav = [
    { route: '/dashboard/doctor', label: 'Dashboard', icon: 'bi bi-grid-1x2' },
    { route: '/doctor/appointments', label: 'Janji Temu', icon: 'bi bi-calendar-check' },
    { route: '/doctor/medical-records', label: 'Rekam Medis', icon: 'bi bi-file-earmark-medical' },
    { route: '/doctor/medical-records/create', label: 'Buat Rekam Medis', icon: 'bi bi-plus-circle' }
  ];

  private adminNav = [
    { route: '/dashboard/admin', label: 'Dashboard', icon: 'bi bi-grid-1x2' },
    { route: '/admin/patients', label: 'Data Pasien', icon: 'bi bi-people' },
    { route: '/admin/doctors', label: 'Data Dokter', icon: 'bi bi-person-badge' }
  ];

  onLogout(): void {
    if (confirm('Apakah Anda yakin ingin keluar?')) {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  }
}
