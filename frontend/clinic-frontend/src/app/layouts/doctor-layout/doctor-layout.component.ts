import {
  Component
} from '@angular/core';

import {
  RouterOutlet
} from '@angular/router';

import {
  SidebarComponent
} from '../../shared/component/sidebar/sidebar.component';

@Component({
  selector:
    'app-doctor-layout',

  standalone: true,

  imports: [
    RouterOutlet,
    SidebarComponent
  ],

  templateUrl:
    './doctor-layout.component.html',

  styleUrl:
    './doctor-layout.component.scss'
})
export class DoctorLayoutComponent {
}