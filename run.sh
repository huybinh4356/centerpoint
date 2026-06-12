#!/bin/bash
echo "========================================================"
echo "       CENTERPOINT - Cửa hàng Laptop"
echo "========================================================"
echo ""
echo "[1] Kiểm tra Java..."
if type -p java; then
    echo "[OK] Java đã được cài đặt."
else
    echo "[LỖI] Không tìm thấy Java! Vui lòng cài đặt JDK 17 trở lên."
    exit 1
fi

echo ""
echo "[2] Cấp quyền thực thi cho Maven Wrapper..."
chmod +x ./mvnw

echo ""
echo "[3] Đang tải thư viện tự động và Khởi động Server..."
echo "[INFO] Nếu đây là lần đầu chạy, quá trình tải thư viện có thể mất vài phút..."
echo ""
./mvnw spring-boot:run
