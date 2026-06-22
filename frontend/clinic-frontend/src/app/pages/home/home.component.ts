import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DoctorService } from '../../core/services/doctor.service';
import { ContactMessageService } from '../../core/services/contact-message.service';
import { ToastService } from '../../shared/components/toast.service';
import { DoctorListResponse } from '../../core/models/doctor/doctor-list-response.model';
import { NavbarComponent } from '../../shared/layout/navbar.component';
import { FooterComponent } from '../../shared/layout/footer.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, NavbarComponent, FooterComponent],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit, OnDestroy {
  private doctorService = inject(DoctorService);
  private contactMessageService = inject(ContactMessageService);
  private toast = inject(ToastService);
  private fb = inject(FormBuilder);

  doctors: DoctorListResponse[] = [];
  loadingDoctors = false;
  submitting = false;

  contactForm = this.fb.nonNullable.group({
    senderName: ['', Validators.required],
    senderEmail: ['', [Validators.required, Validators.email]],
    subject: ['', Validators.required],
    messageContent: ['', Validators.required]
  });

  testimonials = [
    {
      name: 'Sarah Wijaya',
      role: 'Pasien Poli Umum',
      image: '',
      text: 'Pelayanan cepat dan ramah. Saya tidak perlu antri lama karena sudah booking melalui aplikasi. Dokternya juga sangat komunikatif dan jelas dalam menjelaskan diagnosis.',
      rating: 5
    },
    {
      name: 'Budi Santoso',
      role: 'Pasien Poli Anak',
      image: '',
      text: 'Anak saya senang sekali diperiksa di sini. Suasananya nyaman dan dokter spesialis anaknya sangat sabar. Rekomendasi banget buat orang tua!',
      rating: 5
    },
    {
      name: 'Dian Permata',
      role: 'Pasien Kardiologi',
      image: '',
      text: 'Klinik ini punya alat EKG modern dan dokter jantung yang berpengalaman. Hasil cek kolesterol juga cepat keluar. Sangat puas dengan pelayanannya.',
      rating: 5
    },
    {
      name: 'Ahmad Fauzi',
      role: 'Pasien Laboratorium',
      image: '',
      text: 'Proses pengambilan sampel darah cepat dan hampir tidak terasa sakit. Hasil lab keluar dalam hitungan jam dan bisa diakses online. Praktis!',
      rating: 4
    }
  ];

  currentTestimonial = 0;
  private testimonialTimer: ReturnType<typeof setInterval> | null = null;

  facilities = [
    { name: 'Ruang Tunggu Nyaman', icon: 'bi bi-cup-hot', desc: 'Area tunggu ber-AC dengan kursi ergonomis dan minuman gratis.' },
    { name: 'Laboratorium Modern', icon: 'bi bi-microscope', desc: 'Alat diagnostik automated dengan hasil akurat dan cepat.' },
    { name: 'Farmasi 24 Jam', icon: 'bi bi-prescription2', desc: 'Apotek lengkap dengan obat resep dan non-resep siap melayani.' },
    { name: 'Ruang Operasi Steril', icon: 'bi bi-hospital', desc: 'Ruang operasi standar rumah sakit dengan protokol kebersihan ketat.' },
    { name: 'UGD Siaga', icon: 'bi bi-ambulance', desc: 'Unit gawat darurat 24 jam dengan tim medis siaga setiap saat.' },
    { name: 'Rawat Inap', icon: 'bi bi-bed', desc: 'Kamar rawat inap nyaman dengan monitor pasien dan panggilan perawat.' }
  ];

  faqs = [
    {
      question: 'Bagaimana cara melakukan registrasi pasien baru?',
      answer: 'Anda dapat mendaftar dengan mengklik tombol "Daftar Akun" di kanan atas halaman utama, mengisi data diri dasar, dan memverifikasi alamat email Anda melalui kode OTP yang dikirimkan.',
      open: false
    },
    {
      question: 'Bagaimana cara membuat janji temu dengan dokter?',
      answer: 'Setelah login ke akun Pasien Anda, masuk ke menu "Buat Janji Temu", pilih poli/spesialisasi yang diinginkan, pilih dokter, pilih tanggal dan jam praktiknya, lalu masukkan keluhan Anda.',
      open: false
    },
    {
      question: 'Apakah saya bisa mengakses catatan medis saya secara online?',
      answer: 'Ya. Seluruh riwayat rekam medis, diagnosis dokter, tindakan medis, dan resep obat yang dikeluarkan oleh dokter di klinik kami dapat Anda lihat pada menu "Rekam Medis" di portal Pasien Anda.',
      open: false
    },
    {
      question: 'Bagaimana jika saya ingin menjadwalkan ulang atau membatalkan janji temu?',
      answer: 'Anda dapat masuk ke portal Pasien, kunjungi menu "Janji Temu Saya", lalu pilih janji temu aktif yang ingin dibatalkan dengan menekan tombol "Batalkan Janji Temu". Untuk reschedule, Anda bisa membuat janji temu baru.',
      open: false
    }
  ];

  ngOnInit(): void {
    this.loadFeaturedDoctors();
    this.startTestimonials();
  }

  ngOnDestroy(): void {
    this.stopTestimonials();
  }

  loadFeaturedDoctors(): void {
    this.loadingDoctors = true;
    this.doctorService.getAllDoctors().subscribe({
      next: (data) => {
        this.doctors = data.slice(0, 3);
        this.loadingDoctors = false;
      },
      error: () => {
        this.loadingDoctors = false;
      }
    });
  }

  toggleFaq(index: number): void {
    this.faqs[index].open = !this.faqs[index].open;
  }

  nextTestimonial(): void {
    this.currentTestimonial = (this.currentTestimonial + 1) % this.testimonials.length;
    this.resetTimer();
  }

  prevTestimonial(): void {
    this.currentTestimonial = (this.currentTestimonial - 1 + this.testimonials.length) % this.testimonials.length;
    this.resetTimer();
  }

  goToTestimonial(index: number): void {
    this.currentTestimonial = index;
    this.resetTimer();
  }

  private startTestimonials(): void {
    this.testimonialTimer = setInterval(() => {
      this.currentTestimonial = (this.currentTestimonial + 1) % this.testimonials.length;
    }, 5000);
  }

  private stopTestimonials(): void {
    if (this.testimonialTimer) {
      clearInterval(this.testimonialTimer);
      this.testimonialTimer = null;
    }
  }

  private resetTimer(): void {
    this.stopTestimonials();
    this.startTestimonials();
  }

  onSendContact(): void {
    if (this.contactForm.invalid) {
      this.contactForm.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.contactMessageService.sendContactMessage(this.contactForm.getRawValue()).subscribe({
      next: (res) => {
        this.toast.success(res.message || 'Pesan Anda berhasil terkirim');
        this.contactForm.reset();
        this.submitting = false;
      },
      error: (err) => {
        this.toast.error(err.error?.message || 'Gagal mengirim pesan');
        this.submitting = false;
      }
    });
  }

  getDoctorImageUrl(path: string | undefined | null): string {
    if (!path || path === 'null' || path === 'undefined' || path.endsWith('/null') || path.endsWith('/undefined')) {
      return 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&w=600&q=80';
    }
    if (path.startsWith('http://') || path.startsWith('https://') || path.startsWith('data:')) {
      return path;
    }
    if (path.startsWith('/api/doctors') || path.startsWith('/api/files')) {
      return `http://localhost:8080${path}`;
    }
    if (path.startsWith('/')) {
      return `http://localhost:8080/api/doctors${path}`;
    }
    return `http://localhost:8080/api/doctors/${path}`;
  }
}
