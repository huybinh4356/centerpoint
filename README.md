# CenterPoint - Cửa hàng Laptop

Dự án Website bán Laptop sử dụng **Spring Boot**, **Thymeleaf**, và **MySQL**.

## 🚀 Hướng dẫn Clone và Setup nhanh (Dành cho máy mới)

Project này đã được cấu hình tự động tối đa. Khi clone code về, bạn chỉ cần setup môi trường cơ bản sau:

### 1. Yêu cầu môi trường
* **Java**: Cài đặt JDK 17 trở lên.
* **MySQL**: Cài đặt MySQL Server (chạy cổng mặc định 3306).

### 2. Cấu hình Database
Đặc biệt, hệ thống đã bật chế độ tự động tạo database `createDatabaseIfNotExist=true`. 
Bạn **KHÔNG CẦN** phải tạo Database hay chạy script SQL thủ công. Bạn chỉ cần đảm bảo **Tài khoản MySQL** trong máy khớp với thông tin trong file `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=abc123!
```
*(Nếu MySQL của bạn xài mật khẩu khác, hãy đổi dòng `abc123!` thành mật khẩu của máy bạn trước khi chạy)*.

### 3. Khởi động ứng dụng (Tự động tải thư viện)

Hệ thống đã tích hợp sẵn **Maven Wrapper**, bạn không cần cài Maven thủ công, nó sẽ tự động tải mọi thư viện cần thiết.

**Trên Windows:**
Chỉ cần nhấp đúp vào file `run.bat` hoặc mở Terminal gõ:
```cmd
.\run.bat
```

**Trên Mac / Linux:**
Mở Terminal và gõ:
```bash
./run.sh
```

> **Lưu ý:** Ở lần chạy đầu tiên, màn hình có thể hơi đứng một lúc để hệ thống tự động tải tất cả các file thư viện (`.jar`) trên mạng về máy. Các lần sau sẽ chạy rất nhanh.

### 4. Truy cập hệ thống
* **Trang khách hàng:** http://localhost:8081/
* **Trang quản trị (Admin):** http://localhost:8081/admin

**Tài khoản Admin mặc định** (Hệ thống sẽ tự động tạo nếu chưa có):
* **Email:** admin@gmail.com
* **Mật khẩu:** 123456
