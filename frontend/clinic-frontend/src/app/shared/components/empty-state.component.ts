import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="text-center py-16 bg-white border border-slate-100 rounded-card-lg shadow-card">
      <div class="w-20 h-20 bg-slate-100 rounded-full flex items-center justify-center mx-auto mb-4">
        <i [class]="icon() + ' text-3xl text-slate-400'"></i>
      </div>
      <h3 class="font-bold text-slate-900 mb-1">{{ title() }}</h3>
      <p class="text-sm text-slate-500 max-w-sm mx-auto">{{ message() }}</p>
      <a *ngIf="actionLabel() && actionRoute()" [routerLink]="actionRoute()" 
         class="inline-flex items-center gap-2 mt-5 px-6 py-3 bg-blue-600 text-white font-bold rounded-button text-sm hover:bg-blue-700 transition-all shadow-md shadow-blue-600/20">
        <i class="bi bi-plus-lg"></i> {{ actionLabel() }}
      </a>
    </div>
  `
})
export class EmptyStateComponent {
  icon = input('bi-inbox');
  title = input('Data Tidak Ditemukan');
  message = input('');
  actionLabel = input('');
  actionRoute = input('');
}
