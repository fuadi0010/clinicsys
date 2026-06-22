import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { DoctorService } from '../../core/services/doctor.service';
import { ToastService } from '../../shared/components/toast.service';
import { DashboardLayoutComponent } from '../../shared/layout/dashboard-layout.component';

@Component({
  selector: 'app-create-doctor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, DashboardLayoutComponent],
  templateUrl: './create-doctor.component.html'
})
export class CreateDoctorComponent implements OnInit {
  private fb = inject(FormBuilder);
  private doctorService = inject(DoctorService);
  private toast = inject(ToastService);
  private router = inject(Router);

  loading = false;
  selectedFile: File | null = null;

  specializations = [
    { value: 'DENTIST', label: 'Spesialis Gigi' },
    { value: 'OTOLARYNGOLOGIST', label: 'Spesialis THT' }
  ];

  form = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(40)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    fullName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s',.-]+$/)]],
    specialization: ['', [Validators.required]],
    strNumber: ['', [Validators.required, Validators.pattern(/^STR-[0-9]{8,10}$/)]],
    phoneNumber: ['', [Validators.required, Validators.pattern(/^(08[0-9]{8,11}|8[0-9]{9,12})$/)]],
    consultationFee: [150000, [Validators.required, Validators.min(0)]],
    bio: ['']
  });

  ngOnInit(): void {}

  limitDigits(event: KeyboardEvent, maxLength: number): void {
    const charCode = event.key;
    if (['Backspace', 'Delete', 'Tab', 'Enter', 'ArrowLeft', 'ArrowRight'].includes(charCode)) {
      return;
    }
    if (!/^[0-9]$/.test(charCode)) {
      event.preventDefault();
      return;
    }
    const input = event.target as HTMLInputElement;
    if (input.value.length >= maxLength) {
      event.preventDefault();
    }
  }

  onFileSelected(event: Event): void {
    const target = event.target;
    if (target instanceof HTMLInputElement && target.files && target.files.length > 0) {
      this.selectedFile = target.files[0];
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.toast.error('Mohon lengkapi seluruh field wajib dengan benar.');
      return;
    }

    this.loading = true;
    const reqData = this.form.value;

    this.doctorService.createDoctorByAdmin(reqData, this.selectedFile).subscribe({
      next: () => {
        this.toast.success('Akun & Profil Dokter berhasil didaftarkan');
        this.router.navigate(['/admin/doctors']);
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        let errorMsg = 'Gagal mendaftarkan dokter';
        if (err.status === 400 && err.error?.message === 'Validation Failed' && err.error?.data) {
          const validationErrors = err.error.data;
          const errorMessages = Object.keys(validationErrors).map(field => {
            const backendMsg = validationErrors[field];
            let indonesianField = field;
            if (field === 'username') indonesianField = 'Username';
            else if (field === 'email') indonesianField = 'Email';
            else if (field === 'password') indonesianField = 'Password';
            else if (field === 'fullName') indonesianField = 'Nama lengkap';
            else if (field === 'specialization') indonesianField = 'Spesialisasi';
            else if (field === 'strNumber') indonesianField = 'Nomor STR';
            else if (field === 'phoneNumber') indonesianField = 'Nomor telepon';
            else if (field === 'consultationFee') indonesianField = 'Tarif konsultasi';
            
            let indonesianMsg = backendMsg;
            if (backendMsg.includes('must not be blank') || backendMsg.includes('wajib diisi')) {
              indonesianMsg = 'wajib diisi';
            }
            return `${indonesianField}: ${indonesianMsg}`;
          });
          errorMsg = errorMessages.join(', ');
        } else if (err.error?.message) {
          errorMsg = err.error.message;
        } else if (err.message) {
          errorMsg = err.message;
        }
        this.toast.error(errorMsg);
      }
    });
  }
}
