import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="open()" class="fixed inset-0 z-50 flex items-center justify-center p-4" (click)="dismiss()">
      <div class="absolute inset-0 bg-black/50 backdrop-blur-sm"></div>
      <div class="relative bg-white rounded-card-lg shadow-premium-lg p-8 w-full max-w-md space-y-6 animate-scale-in" (click)="$event.stopPropagation()">
        <div *ngIf="icon()" class="flex justify-center">
          <div class="w-16 h-16 rounded-full flex items-center justify-center" [class]="iconBg()">
            <i class="bi text-3xl" [class]="icon()"></i>
          </div>
        </div>
        <div class="text-center space-y-2" *ngIf="title() || body()">
          <h3 class="font-extrabold text-lg text-slate-900" *ngIf="title()">{{ title() }}</h3>
          <p class="text-sm text-slate-500" *ngIf="body()">{{ body() }}</p>
        </div>
        <ng-content />
        <div class="flex gap-3" *ngIf="confirmText() || cancelText()">
          <button *ngIf="cancelText()" (click)="dismiss()" 
                  class="flex-1 py-2.5 font-bold text-slate-700 border border-slate-200 rounded-button hover:bg-slate-50 transition-all text-sm">
            {{ cancelText() }}
          </button>
          <button *ngIf="confirmText()" (click)="onConfirm()" 
                  class="flex-1 py-2.5 font-bold text-white rounded-button transition-all text-sm"
                  [class]="confirmBg()">
            {{ confirmText() }}
          </button>
        </div>
      </div>
    </div>
  `
})
export class ModalComponent {
  open = input(false);
  title = input('');
  body = input('');
  icon = input('');
  iconBg = input('bg-slate-100 text-slate-600');
  confirmText = input('');
  confirmBg = input('bg-blue-600 hover:bg-blue-700');
  cancelText = input('Batal');

  confirmed = output<void>();
  dismissed = output<void>();

  onConfirm(): void { this.confirmed.emit(); }
  dismiss(): void { this.dismissed.emit(); }
}
