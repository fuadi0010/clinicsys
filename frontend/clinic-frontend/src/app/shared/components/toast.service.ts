import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'warning' | 'info';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private _toasts = signal<Toast[]>([]);
  private nextId = 0;

  readonly toasts = this._toasts.asReadonly();

  show(message: string, type: Toast['type'] = 'info', duration = 4000): void {
    const id = this.nextId++;
    this._toasts.update(t => [...t, { id, message, type }]);
    setTimeout(() => this.remove(id), duration);
  }

  success(message: string): void { this.show(message, 'success'); }
  error(message: string): void { this.show(message, 'error', 6000); }
  warning(message: string): void { this.show(message, 'warning'); }
  info(message: string): void { this.show(message, 'info'); }

  remove(id: number): void {
    this._toasts.update(t => t.filter(toast => toast.id !== id));
  }
}
