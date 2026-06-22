# 🏥 Sistem Informasi Klinik Kesehatan (Sistem Klinik UAS)

Sistem Informasi Klinik Kesehatan adalah sebuah platform kesehatan digital berbasis **Microservice Architecture** yang dirancang khusus untuk memenuhi standar tugas akhir Ujian Akhir Semester (UAS). Proyek ini didevelop menggunakan pendekatan *Clean Architecture* dan *Decoupled System* antara backend dan frontend.

---

## 🛠️ Stack Teknologi

### Backend Service (Java 21)
* **Core Framework:** Spring Boot 3
* **Security:** Spring Security & Stateless JWT Authentication
* **Data Access:** Spring Data JPA / Hibernate
* **Database:** PostgreSQL (Setiap microservice memiliki database terisolasi/sendiri)
* **Build Tool:** Maven
* **Utility & Boilerplate Reduction:** Lombok

### Frontend App (Angular)
* **Core & Logic:** Angular 18+ & TypeScript
* **Forms:** Reactive Forms (dengan validasi input real-time)
* **Styling (CSS):** Tailwind CSS / Bootstrap (Modern premium aesthetics)

---

## 🧩 Struktur Microservices & Service Registry

Proyek ini dibangun secara independen (*Decoupled Services*) tanpa membagikan entitas maupun database antarservice (*Shared-Nothing Database Pattern*). Komunikasi antarservice dilakukan secara langsung menggunakan REST API atau Feign Client.

```
clinic-system/
├── api-gateway/            # Gateway pengarah routing & load balancer API
├── auth-service/           # Layanan autentikasi, registrasi, otorisasi, dan JWT
├── patient-service/        # Manajemen data diri pasien, NIK, & rekam medis regional
├── doctor-service/         # Profil dokter, spesialisasi, dan jadwal praktik harian
├── appointment-service/    # Booking antrean pemeriksaan, reschedule, & pembatalan
├── medical-record-service/ # Hasil diagnosis dokter, resep obat, & riwayat keluhan
└── frontend/               # Single Page Application (SPA) berbasis Angular
```

---

## 🔐 Roles & Hak Akses Sistem

Sistem membedakan hak akses secara ketat berdasarkan role pengguna menggunakan otorisasi berbasis klaim JWT:
1. **ADMIN:** Mengelola seluruh data master dokter, jadwal praktik, pasien, serta kontrol administratif sistem.
2. **DOCTOR:** Melihat riwayat janji temu pasien, menginput keluhan, diagnosis, resep obat, serta catatan rekam medis.
3. **PATIENT:** Mengelola profil data diri, memantau riwayat verifikasi akun, melakukan pemesanan janji temu secara mandiri, dan melihat riwayat medis mereka.

---

## 📐 Standar Clean Architecture Backend
Setiap modul backend diimplementasikan dengan membagi tanggung jawab struktur paket sebagai berikut:
```
com.klinik
├── controller     # REST Controller yang menangani HTTP Request/Response
├── service        # Implementasi logic bisnis murni (Service & ServiceImpl)
├── repository     # Antarmuka database Spring Data JPA
├── entity         # Objek data model database (Internal Domain)
├── dto            # Data Transfer Object (Request & Response) untuk komunikasi API
├── mapper         # Komponen konversi antara Entity <-> DTO
├── security       # Konfigurasi keamanan internal Spring Security
└── common         # Utility, Exception handler, global payload response
```

---

## 🚀 Panduan Menjalankan Sistem Secara Lokal

### Prasyarat (Prerequisites)
* Java JDK 21 terinstal
* Node.js v20+ & Angular CLI terinstal
* PostgreSQL server aktif
* Maven terinstal

### 1. Inisialisasi Database PostgreSQL
Buat database PostgreSQL terpisah untuk masing-masing service:
```sql
CREATE DATABASE clinic_auth_db;
CREATE DATABASE clinic_patient_db;
CREATE DATABASE clinic_doctor_db;
CREATE DATABASE clinic_appointment_db;
CREATE DATABASE clinic_medical_record_db;
```

### 2. Menjalankan Backend Services
Masuk ke masing-masing folder service di dalam direktori `backend/` dan jalankan perintah:
```bash
mvn clean spring-boot:run
```
*Pastikan konfigurasi database di `src/main/resources/application.properties` sudah disesuaikan.*

### 3. Menjalankan Frontend Angular
Masuk ke direktori `frontend/clinic-frontend/` lalu jalankan:
```bash
# Mengunduh dependensi (jika baru pertama kali clone)
npm install

# Menjalankan development server lokal
npm run dev
# atau menggunakan Angular CLI langsung
ng serve
```
Akses aplikasi melalui browser di [http://localhost:4200](http://localhost:4200).

