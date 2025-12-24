# 📇 Contact Manager - Java Swing Application

Aplikasi manajemen kontak berbasis desktop yang dibangun menggunakan Java Swing dengan fitur CRUD (Create, Read, Update, Delete) lengkap dan antarmuka yang modern.

## 📋 Daftar Isi

- [Fitur Utama](#fitur-utama)
- [Teknologi](#teknologi)
- [Struktur Proyek](#struktur-proyek)
- [Instalasi](#instalasi)
- [Cara Menjalankan](#cara-menjalankan)
- [Kredensial Login](#kredensial-login)
- [Panduan Penggunaan](#panduan-penggunaan)
- [Screenshot](#screenshot)
- [Arsitektur Aplikasi](#arsitektur-aplikasi)
- [Validasi Data](#validasi-data)
- [Kontributor](#kontributor)

---

## ✨ Fitur Utama

### 🔐 Autentikasi
- Login dengan username dan password
- Validasi input dengan pesan error yang jelas
- UI login yang modern dengan gradient background

### 📊 Dashboard
- Tampilan tabel kontak dengan informasi:
    - Nama
    - Kategori
    - Status Favorit
- Filter berdasarkan kategori (Friend, Family, School, Work)
- Filter kontak favorit
- Pencarian kontak by nama atau nomor telepon
- Tombol logout

### ➕ Tambah Kontak
- Form input lengkap:
    - Nama (required)
    - Nomor Telepon (required)
    - Email (optional)
    - Kategori (Friend/Family/School/Work)
    - Mark as Favorite
- Validasi input real-time
- Notifikasi sukses/error

### 📝 Detail & Edit Kontak
- Tampilan informasi lengkap kontak
- Mode view (read-only)
- Mode edit dengan toggle button
- Update data kontak
- Hapus kontak dengan konfirmasi
- Cancel edit dengan konfirmasi

### 💾 Penyimpanan Data
- Data tersimpan dalam file CSV (`data/contacts.csv`)
- Auto-create folder dan file jika belum ada
- Persistent storage - data tidak hilang saat aplikasi ditutup

---

## 🛠️ Teknologi

- **Java 8+** - Programming Language
- **Swing** - GUI Framework
- **Lombok** - Mengurangi boilerplate code
- **CSV** - File-based storage

---

## 📁 Struktur Proyek
```
contact-manager/
│
├── src/
│   └── com/uap/
│       ├── controller/
│       │   ├── Service/
│       │   │   └── UserService.java          # Business logic untuk kontak
│       │   ├── ContactController.java        # Controller untuk operasi kontak
│       │   └── LoginController.java          # Controller untuk autentikasi
│       │
│       ├── model/
│       │   ├── Contact.java                  # Model kontak
│       │   └── User.java                     # Model user
│       │
│       ├── repository/
│       │   ├── ContactRepository.java        # Interface repository
│       │   └── CsvContactRepository.java     # Implementasi CSV storage
│       │
│       ├── util/
│       │   ├── ValidationUtil.java           # Utility untuk validasi
│       │   └── ValidationException.java      # Custom exception
│       │
│       └── view/
│           ├── LoginView.java                # Halaman login
│           ├── DashboardView.java            # Halaman dashboard utama
│           ├── AddContactView.java           # Halaman tambah kontak
│           └── ContactDetailView.java        # Halaman detail kontak
│
├── data/
│   └── contacts.csv                          # File penyimpanan data
│
└── README.md
```

---

## 💻 Instalasi

### Prasyarat

1. **Java Development Kit (JDK) 8 atau lebih tinggi**
```bash
   java -version
```

2. **IDE (pilih salah satu):**
    - IntelliJ IDEA
    - Eclipse
    - NetBeans
    - Visual Studio Code dengan Extension Pack for Java

3. **Lombok Plugin** (untuk IDE)
    - IntelliJ IDEA: File → Settings → Plugins → Cari "Lombok" → Install
    - Eclipse: Download lombok.jar dari [projectlombok.org](https://projectlombok.org/) → Run installer
    - NetBeans: Sudah include support Lombok

### Langkah Instalasi

1. **Clone atau Download Project**
```bash
   git clone <repository-url>
   cd contact-manager
```

2. **Import Project ke IDE**
    - **IntelliJ IDEA**: File → Open → Pilih folder project
    - **Eclipse**: File → Import → Existing Projects into Workspace
    - **NetBeans**: File → Open Project

3. **Pastikan Lombok Terinstall**
    - Build project untuk memastikan tidak ada error
    - Jika ada error terkait getter/setter, pastikan Lombok plugin aktif

4. **Struktur Folder akan Otomatis Dibuat**
    - Folder `data/` akan dibuat otomatis saat aplikasi pertama kali dijalankan
    - File `contacts.csv` akan dibuat dengan beberapa data contoh

---

## 🚀 Cara Menjalankan

### Melalui IDE

1. **Buka file `LoginView.java`**
    - Lokasi: `src/com/uap/view/LoginView.java`

2. **Jalankan method `main()`**
    - **IntelliJ IDEA**: Klik kanan pada file → Run 'LoginView.main()'
    - **Eclipse**: Klik kanan pada file → Run As → Java Application
    - **NetBeans**: Klik kanan pada file → Run File

### Melalui Command Line

1. **Compile semua file:**
```bash
   javac -d bin -cp . src/com/uap/**/*.java
```

2. **Jalankan aplikasi:**
```bash
   java -cp bin com.uap.view.LoginView
```

---

## 🔑 Kredensial Login

Aplikasi memiliki 2 akun default:

| Username | Password  |
|----------|-----------|
| `fazel`  | `fazel123`|
| `faizul` | `faizul123`|

---

## 📖 Panduan Penggunaan

### 1. Login
- Masukkan username dan password
- Klik tombol **LOGIN** atau tekan **Enter**
- Jika berhasil, akan masuk ke Dashboard

### 2. Melihat Daftar Kontak
- Dashboard menampilkan semua kontak dalam bentuk tabel
- Data default sudah tersedia untuk demo

### 3. Mencari Kontak
- **Search**: Ketik nama atau nomor telepon di search box
- **Filter Kategori**: Pilih kategori dari dropdown (All/Friend/Family/School/Work)
- **Filter Favorit**: Centang "Favorites Only" untuk melihat kontak favorit saja

### 4. Menambah Kontak Baru
1. Klik tombol **"+ Add Contact"**
2. Isi form:
    - **Name** (wajib): Nama kontak
    - **Phone** (wajib): Nomor telepon (format: +1234567890)
    - **Email** (opsional): Alamat email
    - **Category**: Pilih kategori
    - **Mark as Favorite**: Centang jika kontak favorit
3. Klik **"Save Contact"** untuk menyimpan
4. Klik **"Cancel"** untuk membatalkan

### 5. Melihat Detail Kontak
- **Double-click** pada baris kontak di tabel
- Halaman detail akan menampilkan informasi lengkap

### 6. Mengedit Kontak
1. Buka detail kontak (double-click pada tabel)
2. Klik tombol **"Edit"**
3. Field akan aktif untuk diedit
4. Ubah data yang diperlukan
5. Klik **"Save"** untuk menyimpan perubahan
6. Klik **"Cancel"** untuk membatalkan edit

### 7. Menghapus Kontak
1. Buka detail kontak
2. Klik tombol **"Delete Contact"**
3. Konfirmasi penghapusan
4. Kontak akan dihapus permanen

### 8. Logout
- Klik tombol **"Logout"** di pojok kanan atas Dashboard
- Konfirmasi logout
- Kembali ke halaman login

---

## 📸 Screenshot

### 1. Login Page
```
┌─────────────────────────────────────┐
│     Contact Manager (Blue)          │
│  Manage your contacts efficiently   │
│                                     │
│  ┌───────────────────────────────┐ │
│  │   Welcome Back!               │ │
│  │   Please login to continue    │ │
│  │                               │ │
│  │   Username: [___________]     │ │
│  │   Password: [___________]     │ │
│  │                               │ │
│  │   [     LOGIN (Blue)     ]    │ │
│  │                               │ │
│  │   Demo Accounts:              │ │
│  │   fazel / fazel123            │ │
│  │   faizul / faizul123          │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

### 2. Dashboard
```
┌────────────────────────────────────────────────────────────┐
│ Contact Manager (Blue)          Welcome, fazel   [Logout]  │
├────────────────────────────────────────────────────────────┤
│ Search: [______] Category: [All ▼] [✓] Favorites  [+Add]  │
├────────────────────────────────────────────────────────────┤
│ Name            │ Category  │ Favorite                     │
│─────────────────┼───────────┼──────────                    │
│ John Doe        │ Friend    │ ★ Yes                        │
│ Jane Smith      │ Family    │ No                           │
│ Bob Wilson      │ Work      │ ★ Yes                        │
│ Alice Brown     │ School    │ No                           │
└────────────────────────────────────────────────────────────┘
```

### 3. Add Contact Page
```
┌─────────────────────────────────────┐
│ Add New Contact (Blue)              │
├─────────────────────────────────────┤
│                                     │
│  Name *                             │
│  [____________________________]     │
│                                     │
│  Phone *                            │
│  [____________________________]     │
│                                     │
│  Email                              │
│  [____________________________]     │
│                                     │
│  Category                           │
│  [Friend ▼                    ]     │
│                                     │
│  [✓] Mark as Favorite ⭐            │
│                                     │
├─────────────────────────────────────┤
│              [Cancel] [Save Contact]│
└─────────────────────────────────────┘
```

### 4. Contact Detail Page
```
┌─────────────────────────────────────┐
│ Contact Details (Blue)              │
├─────────────────────────────────────┤
│                                     │
│  Name *                             │
│  [John Doe                    ]     │
│                                     │
│  Phone *                            │
│  [+1234567890                 ]     │
│                                     │
│  Email                              │
│  [john@example.com            ]     │
│                                     │
│  Category                           │
│  [Friend ▼                    ]     │
│                                     │
│  [✓] Mark as Favorite ⭐            │
│                                     │
├─────────────────────────────────────┤
│ [Delete]              [Back] [Edit] │
└─────────────────────────────────────┘
```

---

## 🏗️ Arsitektur Aplikasi

### Model-View-Controller (MVC) Pattern
```
┌──────────┐      ┌──────────────┐      ┌────────────┐
│   View   │ ───> │  Controller  │ ───> │   Model    │
│  (GUI)   │ <─── │   (Logic)    │ <─── │  (Data)    │
└──────────┘      └──────────────┘      └────────────┘
                          │
                          ▼
                  ┌──────────────┐
                  │  Repository  │
                  │   (Storage)  │
                  └──────────────┘
                          │
                          ▼
                   ┌────────────┐
                   │ contacts.csv│
                   └────────────┘
```

### Flow Aplikasi
```
1. LoginView
   └─> LoginController.authenticate()
       └─> Success → DashboardView
       └─> Fail → Error Message

2. DashboardView
   ├─> ContactController.getAllContacts()
   ├─> Add Contact → AddContactView
   └─> Double Click → ContactDetailView

3. AddContactView
   └─> ContactController.createContact()
       └─> UserService.createContact()
           └─> CsvContactRepository.save()

4. ContactDetailView
   ├─> Edit Mode → ContactController.updateContact()
   └─> Delete → ContactController.deleteContact()
```

---

## ✅ Validasi Data

### Validasi Nama
- **Tidak boleh kosong**
- Error: "Name must not be empty"

### Validasi Nomor Telepon
- **Tidak boleh kosong**
- **Format**: Hanya angka, spasi, tanda plus (+), dan tanda hubung (-)
- **Panjang**: 6-20 karakter
- **Contoh valid**: `+1234567890`, `081234567890`, `+62 812-3456-7890`
- Error: "Phone must not be empty" atau "Phone must be numeric-ish and 6-20 chars"

### Validasi Email
- **Opsional** (boleh kosong)
- **Format**: Harus format email yang valid
- **Pattern**: `example@domain.com`
- Error: "Invalid email format"

### Pop-up Notifikasi
- ✅ **Success**: Notifikasi hijau untuk aksi berhasil
- ❌ **Error**: Notifikasi merah untuk validasi gagal
- ⚠️ **Warning**: Konfirmasi untuk aksi berbahaya (delete, cancel)

---

## 🎨 Desain UI

### Color Palette

| Elemen | Warna | Hex Code | Penggunaan |
|--------|-------|----------|------------|
| Primary Blue | `rgb(33, 150, 243)` | #2196F3 | Header, Login Button, Save Button |
| Light Blue Hover | `rgb(25, 118, 210)` | #1976D2 | Button Hover |
| Red | `rgb(244, 67, 54)` | #F44336 | Delete Button, Cancel Button |
| Orange | `rgb(255, 152, 0)` | #FF9800 | Edit Button |
| Gray | `rgb(158, 158, 158)` | #9E9E9E | Back/Cancel Button |
| Background | `rgb(245, 245, 245)` | #F5F5F5 | App Background |
| White | `rgb(255, 255, 255)` | #FFFFFF | Cards, Buttons Text |

### Typography
- **Font Family**: Segoe UI
- **Heading**: Bold, 24-28px
- **Body**: Regular, 13-14px
- **Button**: Bold, 13px

---

## 📝 TODO / Future Improvements

- [ ] Import/Export kontak dari/ke file
- [ ] Tambah foto profil kontak
- [ ] Search dengan multiple criteria
- [ ] Sort tabel by column
- [ ] Dark mode theme
- [ ] Backup & Restore data
- [ ] Password encryption
- [ ] Multi-user support dengan database
- [ ] Export to PDF/Excel
- [ ] Group contacts management

---

## 👥 Kontributor

- **Developer**: 1. Faizul Mushofa (202410370110418)
                 2. Fazel Rui Dsyadzilli (202410370110439)
- **Universitas**: Universitiy of Muhammadiyah Malang
- **Mata Kuliah**: Pemrograman Lanjut

---

## 📄 Lisensi

Project ini dibuat untuk keperluan akademik.

---
**© 2024 Contact Manager. All Rights Reserved.**