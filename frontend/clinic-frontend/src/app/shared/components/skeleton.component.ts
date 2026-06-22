import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-skeleton',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="space-y-4">
      <div *ngFor="let _ of [].constructor(rows())" class="h-[var(--sk-height)] skeleton rounded-card" [style]="'width: 100%'"></div>
    </div>
  `
})
export class SkeletonComponent {
  rows = input(3);
  height = input('32px');
}
