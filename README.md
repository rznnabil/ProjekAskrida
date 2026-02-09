"# Sistem Absensi Ruangan TU

Sistem absensi berbasis web untuk mengelola data anggota dan absensi ruangan TU.

## Struktur Proyek

```
ProjekAskrida/
├── absensiRuanganTU/    # Backend (Spring Boot)
├── frontend/            # Frontend (HTML/CSS/JS)
│   ├── index.html
│   ├── styles.css
│   └── script.js
└── README.md
```

## Backend Setup

### Prasyarat
- Java 17
- PostgreSQL
- Maven

### Instalasi Database

1. Buat database PostgreSQL:
```sql
CREATE DATABASE ruanganTU;
```

2. Jalankan script SQL untuk membuat tabel:
```sql
CREATE TABLE restexamplecrud (
    id serial PRIMARY KEY NOT NULL,
    key varchar(100) NOT NULL,
    value varchar(250) NOT NULL,
    rand smallint NOT NULL,
    nama varchar(250) NOT NULL,
    waktu_input varchar(250) NOT NULL
);
```

### Konfigurasi Database

Edit file `absensiRuanganTU/src/main/resources/application.properties`:
```properties
spring.datasource.jdbc-url=jdbc:postgresql://localhost:5432/ruanganTU
spring.datasource.username=postgres
spring.datasource.password=1234
server.port=8181
```

### Menjalankan Backend

```bash
cd absensiRuanganTU
./mvnw spring-boot:run
```

Backend akan berjalan di `http://localhost:8181`

## Frontend Setup

### Menjalankan Frontend

Buka file `frontend/index.html` di browser, atau gunakan web server sederhana:

**Dengan Python:**
```bash
cd frontend
python3 -m http.server 8080
```

**Dengan Node.js (http-server):**
```bash
cd frontend
npx http-server -p 8080
```

Frontend akan dapat diakses di `http://localhost:8080`

## Fitur-Fitur

### 1. Absensi
- **Absensi Masuk/Keluar**: Input key/ID anggota untuk mencatat kehadiran
- **Cek Status Batch**: Cek status absensi beberapa anggota sekaligus

### 2. Data Anggota
- **Tambah Anggota Baru**: Daftarkan anggota baru dengan key, nama, kelas, dan divisi
- **Update Data Anggota**: Perbarui informasi anggota yang sudah ada
- **Tambah Batch**: Import beberapa anggota sekaligus menggunakan format JSON

### 3. Riwayat
- **Lihat Riwayat**: Tampilkan semua data absensi
- **Hapus Data**: Hapus record berdasarkan ID

## API Endpoints

Backend menyediakan REST API berikut:

### Absensi
- `POST /rest/absensi` - Catat absensi
- `POST /rest/masuk` - Jam masuk
- `POST /rest/keluar` - Jam keluar
- `POST /rest/cekabsensibatch` - Cek status batch

### Anggota
- `POST /rest/tambahanggota` - Tambah anggota
- `POST /rest/updateanggota` - Update anggota
- `POST /rest/tambahanggotabatch` - Tambah batch

### Data
- `GET /rest/` - Ambil semua data
- `GET /rest/{id}` - Ambil data berdasarkan ID
- `POST /rest/deletedata` - Hapus data
- `DELETE /rest/{id}` - Hapus berdasarkan ID

## Teknologi yang Digunakan

### Backend
- Spring Boot 3.2.2
- Java 17
- PostgreSQL
- Maven

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript
- Fetch API

## Troubleshooting

### Backend tidak bisa connect ke database
- Pastikan PostgreSQL sudah berjalan
- Cek username dan password di `application.properties`
- Pastikan database `ruanganTU` sudah dibuat

### Frontend tidak bisa terhubung ke backend
- Pastikan backend berjalan di port 8181
- Cek console browser untuk error CORS (jika ada, tambahkan CORS config di backend)
- Verifikasi URL di `script.js` sesuai dengan backend URL

### Error CORS
Jika mendapat error CORS, tambahkan konfigurasi berikut di backend:

Buat file `CorsConfig.java` di `com.askrida.web.service.conf`:
```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

## Pengembangan

Untuk pengembangan lebih lanjut:
1. Tambahkan autentikasi/authorization
2. Implementasi validasi data lebih ketat
3. Tambahkan export ke Excel/PDF
4. Implementasi notifikasi real-time
5. Tambahkan grafik statistik absensi

## Lisensi

Project ini dibuat untuk keperluan internal." 
