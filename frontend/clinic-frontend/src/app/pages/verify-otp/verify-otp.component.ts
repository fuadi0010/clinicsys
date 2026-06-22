import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../shared/components/toast.service';

@Component({
  selector: 'app-verify-otp',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './verify-otp.component.html'
})
export class VerifyOtpComponent {

  private fb =
    inject(FormBuilder);

  private route =
    inject(ActivatedRoute);

  private router =
    inject(Router);

  private authService =
    inject(AuthService);

  private toast =
    inject(ToastService);

  loading = false;
  isEmailReadOnly = false;

  otpForm =
    this.fb.nonNullable.group({

      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      otp: [
        '',
        Validators.required
      ]

    });

  constructor() {

    const email =
      this.route
        .snapshot
        .queryParamMap
        .get('email');

    if (email) {

      this.otpForm.patchValue({
        email
      });
      this.isEmailReadOnly = false;

    }
  }

  onSubmit(): void {

    if (this.otpForm.invalid) {

      this.otpForm.markAllAsTouched();

      return;
    }

    this.loading = true;

    this.authService
      .verifyOtp(
        this.otpForm.getRawValue()
      )
      .subscribe({

        next: () => {

          this.toast.success(
            'Akun berhasil diverifikasi, silakan login'
          );

          this.loading = false;

          this.router.navigate([
            '/login'
          ]);

        },

        error: (error) => {
          let message = 'Verifikasi gagal';
          const backendMessage = error.error?.message;
          
          if (backendMessage === 'Invalid OTP') {
            message = 'Kode OTP salah';
          } else if (backendMessage === 'OTP expired') {
            message = 'Kode OTP telah kedaluwarsa';
          } else if (backendMessage === 'OTP not found') {
            message = 'Kode OTP tidak ditemukan';
          } else if (backendMessage) {
            message = backendMessage;
          }

          this.toast.error(message);

          this.otpForm.patchValue({
            otp: ''
          });

          this.otpForm
            .controls
            .otp
            .markAsUntouched();

          this.loading = false;
        }

      });
  }

  resendOtp(): void {

    const email =
      this.otpForm.controls.email.value;

    this.authService
      .resendOtp(email)
      .subscribe({

        next: () => {

          this.toast.success(
            'Kode OTP berhasil dikirim ulang'
          );

        },

        error: (error) => {

          const message =
            error.error?.message ||
            'Gagal mengirim ulang OTP';

          this.toast.error(message);

        }

      });
  }
}