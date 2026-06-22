import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { PatientService } from '../../../core/services/patient.service';
import { DashboardLayoutComponent } from '../../../shared/layout/dashboard-layout.component';
import { ToastService } from '../../../shared/components/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DashboardLayoutComponent],
  templateUrl: './patient-profile.component.html'
})
export class PatientProfileComponent implements OnInit {

  private fb = inject(FormBuilder);
  private patientService = inject(PatientService);
  private toast = inject(ToastService);
  private authService = inject(AuthService);
  private router = inject(Router);

  selectedFile: File | null = null;
  imagePreview: string | null = null;
  hasProfile = false;
  isVerified = true;
  email = '';

  // Data Alamat Statis Indonesia
  provinces = [
    {
      name: 'DKI Jakarta',
      cities: [
        {
          name: 'Jakarta Selatan',
          districts: ['Kebayoran Baru', 'Cilandak', 'Tebet', 'Pasar Minggu', 'Jagakarsa', 'Pancoran']
        },
        {
          name: 'Jakarta Pusat',
          districts: ['Gambir', 'Menteng', 'Tanah Abang', 'Kemayoran', 'Sawah Besar', 'Cempaka Putih']
        },
        {
          name: 'Jakarta Barat',
          districts: ['Kembangan', 'Kebon Jeruk', 'Palmerah', 'Grogol Petamburan', 'Cengkareng']
        },
        {
          name: 'Jakarta Utara',
          districts: ['Tanjung Priok', 'Kelapa Gading', 'Penjaringan', 'Koja', 'Pademangan']
        },
        {
          name: 'Jakarta Timur',
          districts: ['Jatinegara', 'Duren Sawit', 'Pulogadung', 'Cakung', 'Kramat Jati']
        }
      ]
    },
    {
      name: 'Jawa Barat',
      cities: [
        {
          name: 'Bandung',
          districts: ['Coblong', 'Sumur Bandung', 'Lengkong', 'Andir', 'Astanaanyar', 'Sukajadi']
        },
        {
          name: 'Bekasi',
          districts: ['Bekasi Barat', 'Bekasi Timur', 'Bekasi Utara', 'Bekasi Selatan', 'Pondok Gede']
        },
        {
          name: 'Depok',
          districts: ['Pancoran Mas', 'Margonda', 'Beji', 'Sukmajaya', 'Cimanggis', 'Cinere']
        },
        {
          name: 'Bogor',
          districts: ['Bogor Utara', 'Bogor Selatan', 'Bogor Timur', 'Bogor Barat', 'Bogor Tengah']
        }
      ]
    },
    {
      name: 'Jawa Timur',
      cities: [
        {
          name: 'Surabaya',
          districts: ['Tegalsari', 'Genteng', 'Bubutan', 'Simokerto', 'Gubeng', 'Sukolilo', 'Rungkut']
        },
        {
          name: 'Malang',
          districts: ['Klojen', 'Blimbing', 'Lowokwaru', 'Sukun', 'Kedungkandang']
        }
      ]
    },
    {
      name: 'Banten',
      cities: [
        {
          name: 'Tangerang',
          districts: ['Cipondoh', 'Karawaci', 'Pinang', 'Ciledug', 'Jatiuwung']
        },
        {
          name: 'Tangerang Selatan',
          districts: ['Serpong', 'Ciputat', 'Pamulang', 'Pondok Aren', 'Setu']
        }
      ]
    }
  ];

  citiesList: string[] = [];
  districtsList: string[] = [];

  // Date Picker Popover State
  showDatePicker = false;
  years: number[] = [];
  months = [
    'Januari', 'Februari', 'Maret', 'April', 'Mei', 'Juni',
    'Juli', 'Agustus', 'September', 'Oktober', 'November', 'Desember'
  ];
  viewYear = new Date().getFullYear() - 20; // Default view year to 20 years ago
  viewMonth = 0; // Januari
  calendarDays: (number | null)[] = [];

  profileForm = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s',.-]+$/)]],
    gender: ['', Validators.required],
    birthDate: ['', Validators.required],
    phoneNumber: ['', [Validators.required, Validators.pattern(/^(08[0-9]{8,11}|8[0-9]{9,12})$/)]],
    address: [''],
    streetDetail: ['', Validators.required],
    province: ['', Validators.required],
    city: ['', Validators.required],
    district: ['', Validators.required],
    bloodType: ['', Validators.required],
    identityNumber: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
    allergyNotes: [''],
    insuranceNumber: [''],
    medicalNotes: ['']
  });

  ngOnInit(): void {
    this.isVerified = this.authService.isVerified();
    this.email = this.authService.getEmail() || '';
    this.initDatePicker();
    this.loadProfile();
  }

  initDatePicker(): void {
    const currentYear = new Date().getFullYear();
    for (let y = currentYear; y >= 1900; y--) {
      this.years.push(y);
    }
    this.generateCalendarDays();
  }

  generateCalendarDays(): void {
    const days: (number | null)[] = [];
    const firstDay = new Date(this.viewYear, this.viewMonth, 1).getDay();
    const totalDays = new Date(this.viewYear, this.viewMonth + 1, 0).getDate();

    for (let i = 0; i < firstDay; i++) {
      days.push(null);
    }
    for (let d = 1; d <= totalDays; d++) {
      days.push(d);
    }
    this.calendarDays = days;
  }

  prevMonth(event: Event): void {
    event.stopPropagation();
    if (this.viewMonth === 0) {
      this.viewMonth = 11;
      this.viewYear--;
    } else {
      this.viewMonth--;
    }
    this.generateCalendarDays();
  }

  nextMonth(event: Event): void {
    event.stopPropagation();
    if (this.viewMonth === 11) {
      this.viewMonth = 0;
      this.viewYear++;
    } else {
      this.viewMonth++;
    }
    this.generateCalendarDays();
  }

  selectDay(day: number | null): void {
    if (!day) return;
    const formattedMonth = String(this.viewMonth + 1).padStart(2, '0');
    const formattedDay = String(day).padStart(2, '0');
    const selectedDate = `${this.viewYear}-${formattedMonth}-${formattedDay}`;
    this.profileForm.patchValue({ birthDate: selectedDate });
    this.showDatePicker = false;
  }

  isDaySelected(day: number): boolean {
    const birthDate = this.profileForm.get('birthDate')?.value;
    if (!birthDate) return false;
    const parts = birthDate.split('-');
    if (parts.length === 3) {
      return parseInt(parts[0], 10) === this.viewYear &&
             parseInt(parts[1], 10) === (this.viewMonth + 1) &&
             parseInt(parts[2], 10) === day;
    }
    return false;
  }

  onDatePickerYearChange(year: string): void {
    this.viewYear = parseInt(year, 10);
    this.generateCalendarDays();
  }

  onDatePickerMonthChange(monthIdx: string): void {
    this.viewMonth = parseInt(monthIdx, 10);
    this.generateCalendarDays();
  }

  toggleDatePicker(event: Event): void {
    event.stopPropagation();
    this.showDatePicker = !this.showDatePicker;
    if (this.showDatePicker) {
      const currentDate = this.profileForm.get('birthDate')?.value;
      if (currentDate) {
        const parts = currentDate.split('-');
        if (parts.length === 3) {
          this.viewYear = parseInt(parts[0], 10);
          this.viewMonth = parseInt(parts[1], 10) - 1;
        }
      }
      this.generateCalendarDays();
    }
  }

  closeDatePicker(): void {
    this.showDatePicker = false;
  }

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

  onProvinceChange(provinceName: string, resetControls = true): void {
    if (resetControls) {
      this.profileForm.patchValue({ city: '', district: '' });
      this.districtsList = [];
    }
    const prov = this.provinces.find(p => p.name === provinceName);
    this.citiesList = prov ? prov.cities.map(c => c.name) : [];
  }

  onCityChange(cityName: string, resetControls = true): void {
    if (resetControls) {
      this.profileForm.patchValue({ district: '' });
    }
    const currentProvince = this.profileForm.get('province')?.value;
    const prov = this.provinces.find(p => p.name === currentProvince);
    if (prov) {
      const city = prov.cities.find(c => c.name === cityName);
      this.districtsList = city ? city.districts : [];
    } else {
      this.districtsList = [];
    }
  }

  parseAddress(fullAddress: string): { streetDetail: string, province: string, city: string, district: string } {
    if (!fullAddress) {
      return { streetDetail: '', province: '', city: '', district: '' };
    }
    const parts = fullAddress.split(', ');
    if (parts.length >= 4) {
      const province = parts[parts.length - 1].replace(/^Prov\.\s+/i, '').trim();
      const city = parts[parts.length - 2].replace(/^(Kota|Kab\.)\s+/i, '').trim();
      const district = parts[parts.length - 3].replace(/^Kec\.\s+/i, '').trim();
      const streetDetail = parts.slice(0, parts.length - 3).join(', ');
      return { streetDetail, province, city, district };
    }
    return { streetDetail: fullAddress, province: '', city: '', district: '' };
  }

  loadProfile(): void {
    this.patientService.getMyProfile().subscribe({
      next: profile => {
        this.hasProfile = true;
        const addrParsed = this.parseAddress(profile.address);
        this.profileForm.patchValue({
          fullName: profile.fullName,
          gender: profile.gender,
          birthDate: profile.birthDate,
          phoneNumber: profile.phoneNumber,
          address: profile.address,
          streetDetail: addrParsed.streetDetail,
          province: addrParsed.province,
          city: addrParsed.city,
          district: addrParsed.district,
          bloodType: profile.bloodType,
          identityNumber: profile.identityNumber || '',
          allergyNotes: profile.allergyNotes || '',
          insuranceNumber: profile.insuranceNumber || '',
          medicalNotes: profile.medicalNotes || ''
        });

        this.onProvinceChange(addrParsed.province, false);
        this.onCityChange(addrParsed.city, false);

        this.imagePreview = this.getProfileImageUrl(profile.profileImageUrl);
      },
      error: () => {
        this.hasProfile = false;
      }
    });
  }

  goToVerification(): void {
    if (this.email) {
      this.router.navigate(['/verify-otp'], { queryParams: { email: this.email } });
    } else {
      this.router.navigate(['/verify-otp']);
    }
  }

  getProfileImageUrl(path: string | null | undefined): string | null {
    if (!path) return null;
    if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('data:')) {
      return path;
    }
    if (path.startsWith('/api/patients')) {
      return `http://localhost:8080${path}`;
    }
    return `http://localhost:8080/api/patients${path}`;
  }

  updateProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      this.toast.error('Mohon lengkapi seluruh field wajib dengan benar.');
      return;
    }

    const rawVal = this.profileForm.getRawValue();
    const fullAddress = `${rawVal.streetDetail}, Kec. ${rawVal.district}, ${rawVal.city}, Prov. ${rawVal.province}`;
    
    const payload: any = {
      fullName: rawVal.fullName,
      gender: rawVal.gender,
      birthDate: rawVal.birthDate,
      bloodType: rawVal.bloodType,
      phoneNumber: rawVal.phoneNumber,
      address: fullAddress,
      insuranceNumber: rawVal.insuranceNumber ? rawVal.insuranceNumber.trim() : null,
      allergyNotes: rawVal.allergyNotes,
      medicalNotes: rawVal.medicalNotes
    };

    if (!this.hasProfile) {
      payload.identityNumber = rawVal.identityNumber;
    }

    const req$ = this.hasProfile
      ? this.patientService.updateProfile(payload)
      : this.patientService.createProfile(payload);

    req$.subscribe({
      next: () => {
        const msg = this.hasProfile ? 'Profil berhasil diperbarui' : 'Profil berhasil dibuat';
        this.toast.success(msg);
        this.hasProfile = true;
        this.loadProfile();
      },
      error: (err) => {
        if (err.status === 400 && err.error?.message === 'Validation Failed' && err.error?.data) {
          const validationErrors = err.error.data;
          const errorMessages = Object.keys(validationErrors).map(field => {
            const backendMsg = validationErrors[field];
            let indonesianField = field;
            if (field === 'fullName') indonesianField = 'Nama lengkap';
            else if (field === 'gender') indonesianField = 'Jenis kelamin';
            else if (field === 'birthDate') indonesianField = 'Tanggal lahir';
            else if (field === 'phoneNumber') indonesianField = 'Nomor telepon';
            else if (field === 'address') indonesianField = 'Alamat';
            else if (field === 'bloodType') indonesianField = 'Golongan darah';
            else if (field === 'identityNumber') indonesianField = 'Nomor identitas (NIK)';
            else if (field === 'insuranceNumber') indonesianField = 'Nomor asuransi';
            
            let indonesianMsg = backendMsg;
            if (backendMsg.includes('must not be blank') || backendMsg.includes('must not be null') || backendMsg.includes('wajib diisi')) {
              indonesianMsg = 'wajib diisi';
            }
            
            return `${indonesianField} ${indonesianMsg}`;
          });
          this.toast.error(errorMessages.join(', '));
        } else {
          this.toast.error(err.error?.message || 'Gagal menyimpan profil');
        }
      }
    });
  }

  onFileSelected(event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }
    if (!target.files || target.files.length === 0) {
      return;
    }
    this.selectedFile = target.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result;
      if (typeof result === 'string') {
        this.imagePreview = result;
      }
    };
    reader.readAsDataURL(this.selectedFile);
  }

  uploadImage(): void {
    if (!this.selectedFile) {
      return;
    }

    this.patientService
      .uploadProfileImage(this.selectedFile)
      .subscribe({
        next: (imageUrl) => {
          this.toast.success('Foto profil berhasil diunggah');
          const cleanUrl = this.getProfileImageUrl(imageUrl);
          if (cleanUrl) {
            this.imagePreview = `${cleanUrl}?t=${new Date().getTime()}`;
          }
          this.selectedFile = null;
        },
        error: (err) => {
          this.toast.error(err.error?.message || 'Gagal mengunggah foto profil');
        }
      });
  }
}