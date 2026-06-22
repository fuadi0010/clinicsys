import { Component } from '@angular/core';

import { RouterOutlet } from '@angular/router';

import { SidebarComponent } from '../../shared/component/sidebar/sidebar.component';

@Component({
  selector: 'app-patient-layout',

  standalone: true,

  imports: [RouterOutlet, SidebarComponent],

  templateUrl: './patient.layout.component.html',

  styleUrl: './patient.layout.component.scss',
})
export class PatientLayoutComponent {}
