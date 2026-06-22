import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../shared/components/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  private fb = inject(FormBuilder);

  private authService =
    inject(AuthService);

  private router =
    inject(Router);
  private toast =
    inject(ToastService);

  loading = false;

  registerForm =
    this.fb.nonNullable.group({

      usernameRequest: [
        '',
        [
          Validators.required,
          Validators.minLength(5)
        ]
      ],

      emailRequest: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],

      passwordRequest: [
        '',
        [
          Validators.required,
          Validators.minLength(8)
        ]
      ],

      role: [
        'PATIENT',
        Validators.required
      ]

    });

  onSubmit(): void {

    if (this.registerForm.invalid) {

      this.registerForm.markAllAsTouched();

      return;
    }

    this.loading = true;

    this.authService
      .register(
        this.registerForm.getRawValue()
      )
      .subscribe({

        next: () => {

          const email =
            this.registerForm.controls.emailRequest.value;

          this.router.navigate(
            ['/verify-otp'],
            {
              queryParams: {
                email
              }
            }
          );
        },

        error: (error) => {
          console.error(
            'Register failed',
            error
          );
          const msg = error.error?.message || 'Registrasi gagal. Silakan coba lagi.';
          this.toast.error(msg);
          this.loading = false;
        },

        complete: () => {

          this.loading = false;

        }

      });
  }
}