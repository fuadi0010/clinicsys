import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from './toast.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed top-4 right-4 z-[100] flex flex-col gap-3 max-w-sm w-full pointer-events-none">
      <div *ngFor="let toast of toastService.toasts()" 
           class="pointer-events-auto flex items-center gap-3 px-5 py-4 rounded-card border shadow-premium animate-slide-in-right"
           [class]="bgClass(toast.type)">
        <i class="bi text-lg" [class]="iconClass(toast.type)"></i>
        <p class="text-sm font-medium flex-1">{{ toast.message }}</p>
        <button (click)="toastService.remove(toast.id)" class="text-current opacity-50 hover:opacity-100 transition-opacity">
          <i class="bi bi-x-lg text-xs"></i>
        </button>
      </div>
    </div>
  `
})
export class ToastComponent {
  toastService = inject(ToastService);

  private icons: Record<string, string> = {
    success: 'bi-check-circle-fill',
    error: 'bi-x-circle-fill',
    warning: 'bi-exclamation-circle-fill',
    info: 'bi-info-circle-fill'
  };

  private bg: Record<string, string> = {
    success: 'bg-emerald-50 border-emerald-200 text-emerald-700',
    error: 'bg-red-50 border-red-200 text-red-700',
    warning: 'bg-amber-50 border-amber-200 text-amber-700',
    info: 'bg-blue-50 border-blue-200 text-blue-700'
  };

  iconClass(type: string): string {
    return this.icons[type] || this.icons['info'];
  }

  bgClass(type: string): string {
    return this.bg[type] || this.bg['info'];
  }
}
