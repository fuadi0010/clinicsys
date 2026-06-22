# Sistem Informasi Klinik Kesehatan (UAS) - Repository Guide

Repositori ini menggunakan arsitektur microservices terdegradasi (decoupled microservices) dengan Java Spring Boot 3 di sisi Backend dan Angular di sisi Frontend.

Untuk mematuhi kebijakan **Zero-Leak Guardrails**, berkas konfigurasi lokal asli yang memuat data kredensial riil (password database, secret keys, password email, dsb) **diabaikan dari Git** (`ignored`). Pengembang harus mengonfigurasinya secara manual di lokal masing-masing menggunakan berkas template `.properties.example` yang disediakan.

---

## 🛠️ Daftar Service & Port Default

| Service | Deskripsi | Port Default |
|---|---|---|
| `api-gateway` | API Gateway routing ke seluruh microservices | `8080` |
| `auth-service` | Manajemen User, Role, Registrasi, Login & JWT | `8081` |
| `patient-service` | CRUD Biodata Pasien, Rating, & Emergency Contact | `8082` |
| `doctor-service` | Profil Dokter, Jadwal Praktik, & Spesialisasi | `8083` |
| `appointment-service`| Booking, Reschedule, Antrean, & Cancel Appointment | `8084` |
| `medical-record-service`| Riwayat Pemeriksaan, Keluhan, Diagnosis, & Resep | `8085` |
| `clinic-frontend` | Frontend Angular Web Application | `4200` |

---

## 🚀 Langkah Memulai Pengembangan Lokal (Setup Kredensial)

Sebelum menjalankan microservices backend, Anda wajib menyalin berkas template `.properties.example` menjadi berkas `application.properties` riil di setiap folder service.

### 1. Salin Berkas Properties (Lakukan untuk setiap backend service):
Buka terminal di root proyek utama, lalu jalankan perintah penyalinan berikut sesuai OS Anda:

**Windows PowerShell:**
```powershell
cp backend/api-gateway/src/main/resources/application.properties.example backend/api-gateway/src/main/resources/application.properties
cp backend/auth-service/src/main/resources/application.properties.example backend/auth-service/src/main/resources/application.properties
cp backend/patient-service/src/main/resources/application.properties.example backend/patient-service/src/main/resources/application.properties
cp backend/doctor-service/src/main/resources/application.properties.example backend/doctor-service/src/main/resources/application.properties
cp backend/appointment-service/src/main/resources/application.properties.example backend/appointment-service/src/main/resources/application.properties
cp backend/medical-record-service/src/main/resources/application.properties.example backend/medical-record-service/src/main/resources/application.properties
```

**Linux / macOS:**
```bash
cp backend/api-gateway/src/main/resources/application.properties.example backend/api-gateway/src/main/resources/application.properties
cp backend/auth-service/src/main/resources/application.properties.example backend/auth-service/src/main/resources/application.properties
cp backend/patient-service/src/main/resources/application.properties.example backend/patient-service/src/main/resources/application.properties
cp backend/doctor-service/src/main/resources/application.properties.example backend/doctor-service/src/main/resources/application.properties
cp backend/appointment-service/src/main/resources/application.properties.example backend/appointment-service/src/main/resources/application.properties
cp backend/medical-record-service/src/main/resources/application.properties.example backend/medical-record-service/src/main/resources/application.properties
```

### 2. Isi Nilai Konfigurasi Lokal
Buka berkas `application.properties` yang baru Anda buat, lalu isi placeholder berikut dengan kredensial lokal Anda:
- `spring.datasource.username` dan `spring.datasource.password`: Username & password PostgreSQL Anda.
- `jwt.secret-key`: Secret key HMAC-SHA 256/512 acak (minimal 32/64 karakter alfanumerik) untuk verifikasi token JWT.
- `spring.mail.username` dan `spring.mail.password`: Akun Gmail dan App Password Anda (diperlukan oleh `auth-service` dan `appointment-service`).

---

## 🧹 Git Cache Cleanup (Pembersihan Cache Sensitif)

Jika berkas properties asli atau berkas temporary IDE/OS terlanjur di-track oleh Git sebelum `.gitignore` diterapkan, bersihkan cache pelacakan Git dengan perintah berikut tanpa menghapus file fisiknya di disk lokal:

```bash
# Pindah ke root proyek utama
# Bersihkan cache untuk seluruh berkas application.properties asli yang terlanjur ter-track
git rm --cached -r **/src/main/resources/application.properties

# Bersihkan cache untuk file temporary IDE dan OS jika terlanjur ter-track
git rm --cached -r .idea/
git rm --cached -r .vscode/
git rm --cached -r **/node_modules/
git rm --cached -r **/target/
git rm --cached -r **/dist/
git rm --cached -r **/.angular/

# Commit perubahan .gitignore dan pembersihan cache tersebut
git commit -m "chore: apply zero-leak configuration and clean up git cache"
```
