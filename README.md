# ☕ Coffee Ecommerce App (Android + Spring Boot)

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6.0-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Hilt](https://img.shields.io/badge/Dagger%20Hilt-2.51-000000?style=for-the-badge&logo=dagger&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor%20Client-2.3.11-087CFA?style=for-the-badge&logo=ktor&logoColor=white)
![VietQR](https://img.shields.io/badge/VietQR-Payment-0052CC?style=for-the-badge)

Ứng dụng thương mại điện tử mua sắm Cà phê hoàn chỉnh bao gồm **Android Client (Jetpack Compose)** và **Backend RESTful API (Spring Boot)**. Được thiết kế theo chuẩn kiến trúc MVVM, áp dụng Dependency Injection với Hilt, hỗ trợ chế độ Dark Mode toàn ứng dụng và tích hợp 3 cổng thanh toán thực tế (VietQR, COD, VNPay).

---

## 🌟 Tính Năng Nổi Bật (Key Features)

- 🔒 **Xác Thực Bảo Mật (Auth System)**: Đăng ký & Đăng nhập với mật khẩu được mã hóa an toàn bằng thuật toán **BCrypt** (10 salt rounds).
- ☕ **Trang Chủ & Danh Mục**: Lọc danh mục sản phẩm (Cappuccino, Macchiato, Latte, Americano) và tìm kiếm sản phẩm theo từ khóa thời gian thực.
- 💖 **Danh Sách Yêu Thích (Wishlist)**: Thả tim lưu sản phẩm yêu thích.
- 🛒 **Giỏ Hàng & Quản Lý Đơn**: Chọn Size (S/M/L), tăng giảm số lượng, tính tổng tiền tự động.
- 💳 **3 Phương Thức Thanh Toán Thực Tế**:
  - 💜 **VietQR**: Tạo mã QR Ngân Hàng động (MB Bank / ZaloPay / MoMo) tự động tính số tiền quy đổi VNĐ.
  - 🟢 **COD**: Thanh toán tiền mặt khi nhận hàng.
  - 🟠 **VNPay Sandbox**: Cổng thanh toán qua thẻ ngân hàng / ví VNPay.
- 📜 **Lịch Sử Đơn Hàng (Order History)**: Theo dõi danh sách đơn hàng kèm trạng thái (`PAID_QR`, `COD`, `PAID`).
- 🌙 **Giao Diện Dark Mode**: Chuyển đổi linh hoạt giữa Light Mode (Nền trắng) và Dark Mode (Nền đen - Chữ trắng) toàn ứng dụng.

---

## 🏗️ Kiến Trúc Mã Nguồn (System Architecture)

### 📱 Android Client (`Ecommerce/`)
- **UI Framework**: Jetpack Compose (Material3)
- **Architecture**: MVVM (Model-View-ViewModel) + StateFlow & Compose State
- **Dependency Injection**: Dagger Hilt
- **Networking**: Ktor Client + Kotlinx Serialization
- **Image Loading**: Coil AsyncImage

### 🍃 Backend RESTful Service (`backend/`)
- **Framework**: Spring Boot 3.3.5 (Java 21)
- **Security**: Spring Security + BCrypt Password Encoder
- **Database**: H2 File Database (Standalone zero-config) / PostgreSQL
- **ORM**: Spring Data JPA / Hibernate

---

## 🛠️ Hướng Dẫn Cài Đặt & Chạy Dự Án (Getting Started)

### 1. Khởi chạy Backend (Spring Boot)
```bash
cd backend/ecommerce/ecommerce
mvnw.cmd spring-boot:run
```
> Server sẽ khởi động tại `http://localhost:8080` và tự động nạp dữ liệu mẫu sản phẩm cà phê.

### 2. Khởi chạy Android Client (Android Studio)
- Mở thư mục `Ecommerce/` trong Android Studio.
- Nhấn nút **Run (`Shift + F10`)** chọn thiết bị máy ảo (Emulator) hoặc điện thoại thật.

---

## 📄 License
Project developed for educational and portfolio presentation purposes.
