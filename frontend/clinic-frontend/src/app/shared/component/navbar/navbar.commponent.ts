import {
  Component,
  inject
} from '@angular/core';

import {
  Router
} from '@angular/router';

import {
  AuthService
} from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.commponent.html',
  styleUrl: './navbar.commponent.scss'
})
export class NavbarComponent {

  private readonly authService =
    inject(AuthService);

  private readonly router =
    inject(Router);

  logout(): void {
    if (confirm('Apakah Anda yakin ingin keluar?')) {
      localStorage.removeItem(
        'token'
      );
      this.router.navigate([
        '/login'
      ]);
    }
  }

}