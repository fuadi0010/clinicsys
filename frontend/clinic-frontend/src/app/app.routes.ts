import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  // === Rute Publik ===
  {
    path: 'home',
    loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'about',
    loadComponent: () => import('./pages/about/about.component').then(m => m.AboutComponent)
  },
  {
    path: 'services',
    loadComponent: () => import('./pages/services/services.component').then(m => m.ServicesComponent)
  },
  {
    path: 'contact',
    loadComponent: () => import('./pages/contact/contact.component').then(m => m.ContactComponent)
  },
  {
    path: 'doctors',
    loadComponent: () => import('./pages/doctors/doctors.component').then(m => m.DoctorsComponent)
  },
  {
    path: 'doctors/:id',
    loadComponent: () => import('./pages/doctors/doctor-detail-page.component').then(m => m.DoctorDetailPageComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'verify-otp',
    loadComponent: () => import('./pages/verify-otp/verify-otp.component').then(m => m.VerifyOtpComponent)
  },

  // === Rute Pasien (Terautentikasi) ===
  {
    path: 'dashboard/patient',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./pages/dashboard/patient-dashboard.component').then(m => m.PatientDashboardComponent)
  },
  {
    path: 'patient/profile',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./features/patient/profile/patient-profile.component').then(m => m.PatientProfileComponent)
  },
  {
    path: 'patient/doctors',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./features/patient/doctors/doctor-list.component').then(m => m.DoctorListComponent)
  },
  {
    path: 'patient/doctors/:id',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./features/patient/doctors/doctor-detail.component').then(m => m.DoctorDetailComponent)
  },
  {
    path: 'patient/appointments/create',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./features/patient/appointments/create-appointment.component').then(m => m.CreateAppointmentComponent)
  },
  {
    path: 'patient/appointments',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./features/patient/appointments/my-appointments.component').then(m => m.MyAppointmentsComponent)
  },
  {
    path: 'patient/medical-records',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['PATIENT'] },
    loadComponent: () => import('./features/patient/medical-records/patient-medical-records.component').then(m => m.PatientMedicalRecordsComponent)
  },

  // === Rute Dokter (Terautentikasi) ===
  {
    path: 'dashboard/doctor',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['DOCTOR'] },
    loadComponent: () => import('./pages/dashboard/doctor-dashboard.component').then(m => m.DoctorDashboardComponent)
  },
  {
    path: 'doctor/appointments',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['DOCTOR'] },
    loadComponent: () => import('./features/doctor/appointments/doctor-appointments.component').then(m => m.DoctorAppointmentsComponent)
  },
  {
    path: 'doctor/medical-records',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['DOCTOR'] },
    loadComponent: () => import('./features/doctor/medical-records/doctor-medical-records.component').then(m => m.DoctorMedicalRecordsComponent)
  },
  {
    path: 'doctor/medical-records/create',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['DOCTOR'] },
    loadComponent: () => import('./features/doctor/medical-records/create-medical-record.component').then(m => m.CreateMedicalRecordComponent)
  },

  // === Rute Admin (Terautentikasi) ===
  {
    path: 'dashboard/admin',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./pages/dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent)
  },
  {
    path: 'admin/patients',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./features/admin/admin-patients.component').then(m => m.AdminPatientsComponent)
  },
  {
    path: 'admin/doctors',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./features/admin/admin-doctors.component').then(m => m.AdminDoctorsComponent)
  },
  {
    path: 'admin/doctors/create',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./features/admin/create-doctor.component').then(m => m.CreateDoctorComponent)
  },

  // === Wildcard Redirect ===
  {
    path: '**',
    redirectTo: ''
  }
];