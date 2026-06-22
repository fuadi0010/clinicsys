import {
  Component
} from '@angular/core';

@Component({
  selector:
    'app-loading-spinner',

  standalone: true,

  template: `
    <div class="spinner-container">

      <div class="spinner"></div>

    </div>
  `,
  styleUrl:
    './loading-spinner.component.scss'
})
export class LoadingSpinnerComponent {}