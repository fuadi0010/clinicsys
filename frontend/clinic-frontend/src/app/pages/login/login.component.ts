import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../shared/components/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private toast = inject(ToastService);

  loading = false;

  onForgotPassword(event: Event): void {
    event.preventDefault();
    this.toast.info('Lupa Password? Silakan hubungi Administrator Klinik Kesehatan di nomor UGD Siaga (+62-21-555-8899) atau email ke support@kliniksys.com.');
  }

  loginForm =
    this.fb.nonNullable.group({

      username: [
        '',
        Validators.required
      ],

      password: [
        '',
        Validators.required
      ]

    });

  onSubmit(): void {

    if (this.loginForm.invalid) {

      this.loginForm.markAllAsTouched();

      return;
    }

    this.loading = true;

    this.authService
      .login(
        this.loginForm.getRawValue()
      )
      .subscribe({

        next: (response) => {
          this.authService
            .saveAuthData(
              response.accessToken,
              response.refreshToken
            );

          const role =
            this.authService
              .getRole();

          switch (role) {

            case 'PATIENT':

              this.router.navigate([
                '/dashboard/patient'
              ]);

              break;

            case 'DOCTOR':

              this.router.navigate([
                '/dashboard/doctor'
              ]);

              break;

            case 'ADMIN':

              this.router.navigate([
                '/dashboard/admin'
              ]);

              break;

            default:

              this.router.navigate([
                '/login'
              ]);
          }
        },

        error: (error) => {
          console.error('Login failed', error);
          
          let errorMsg = 'Login gagal. Silakan coba lagi.';
          const backendMessage = error.error?.message;

          if (backendMessage === 'Account not verified') {
            errorMsg = 'Akun Anda belum diverifikasi. Silakan verifikasi akun Anda terlebih dahulu.';
          } else if (backendMessage === 'Akun Anda telah diblokir oleh Admin. Silakan hubungi layanan pelanggan.') {
            errorMsg = backendMessage;
          } else if (error.status === 401 || backendMessage === 'Invalid password') {
            errorMsg = 'Password yang Anda masukkan salah.';
          } else if (error.status === 404 || backendMessage === 'User not found') {
            errorMsg = 'Akun tidak ditemukan atau username salah.';
          } else if (backendMessage) {
            errorMsg = backendMessage;
          } else if (error.status === 403) {
            errorMsg = 'Akun Anda belum diverifikasi. Silakan verifikasi akun Anda terlebih dahulu.';
          }

          this.toast.error(errorMsg);
          this.loading = false;
        },

        complete: () => {

          this.loading = false;

        }

      });
  }
}