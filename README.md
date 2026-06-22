# clinicsys
# 🏥 Sistem Informasi Klinik Kesehatan Modern (Microservices Architecture)

Sistem Informasi Klinik Kesehatan adalah platform digital berbasis arsitektur **Microservices** yang dirancang untuk mengotomatisasi dan mengintegrasikan seluruh alur operasional klinik secara *real-time*. Sistem ini mendukung interaksi multi-aktor yang terdiri dari **Pasien**, **Dokter**, dan **Admin**, dengan fokus pada keandalan data, keamanan otentikasi, dan otomatisasi layanan.

Proyek ini dibangun menggunakan **Spring Boot 3** untuk sisi backend (decoupled services) dan **Angular 17+** untuk sisi frontend (single-page application).

---

## 🚀 Fitur Utama Sistem

### 1. 🏥 Jalur Pasien (Patient Journey)
* **Onboarding & OTP Rescue:** Registrasi akun baru dengan validasi keunikan data (anti-duplikat email/username). Dilengkapi fitur *OTP Rescue* di halaman login untuk memverifikasi akun yang tertunda.
* **Strict Profiling:** Pengisian data medis wajib dengan validasi ketat pada level klien dan server (NIK tepat 16 digit, nomor HP maks 13 digit, dan dropdown alamat bertingkat).
* **Booking Jadwal Fleksibel (Slot 30 Menit):** Pemesanan janji temu dokter dengan interval waktu per 30 menit, dilengkapi logika *anti-collision* (jam yang telah terlewat otomatis dikunci).
* **QRIS Payment & Premium E-Tiket:** Integrasi simulasi pembayaran QRIS dengan mekanisme *short-polling* aman (anti-memory leak). Begitu status `PAID`, sistem menerbitkan **E-Tiket Reservasi Premium** lengkap dengan kode tiket unik (`TKT-...`) dan visual Barcode CSS.
* **Ulasan & Rating Transparan:** Pasien dapat memberikan rating bintang (1-5) dan ulasan setelah dokter menyelesaikan rekam medis. Evaluasi ini terakumulasi langsung ke profil dokter secara *real-time*.

### 2. 🩺 Jalur Dokter (Doctor Journey)
* **Elegant Empty State Handling:** Proteksi halaman pembuatan rekam medis. Jika dokter belum memilih pasien aktif, UI akan menampilkan visual penolakan yang halus dan mengarahkan kembali ke tabel jadwal secara sukarela.
* **Sanitasi Rekam Medis:** Pengisian form Keluhan, Diagnosis, Tindakan, dan Catatan Dokter dengan UI yang terkunci (*fixed textarea layout*).
* **Idempotency Guard:** Tombol pembuatan rekam medis otomatis terkunci (*disabled*) setelah data di-submit untuk mencegah duplikasi data rekam medis pasien.

### 3. ⚙️ Jalur Admin & Otomatisasi Sistem (Scheduler)
* **Real-time Dashboard Metrics:** Panel pemantauan agregasi jumlah total Dokter, Pasien, Janji Temu, dan Admin secara akurat langsung dari database.
* **Manajemen Dokter & Validasi STR:** Pendaftaran akun dokter baru dengan validasi ketat nomor STR wajib berformat prefix `STR-` diikuti 8-10 digit angka.
* **Soft Delete & Banned State:** Fitur pemblokiran akun pasien yang melanggar aturan. Status diubah menjadi `BANNED/DISABLED` pada Spring Security (akses login ditolak), namun data rekam medis tetap utuh di database demi kepatuhan hukum arsip klinik.
* **Cron Job Engine
