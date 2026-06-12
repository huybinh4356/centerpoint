# Hướng Phát Triển (Future Development Plan)

Tài liệu này dùng để lưu trữ các ý tưởng, cấu trúc và định hướng phát triển các tính năng nâng cao trong tương lai cho dự án CenterPoint.

---

## 1. Tích hợp AI Chatbot Tư Vấn Sản Phẩm
- **Mục tiêu**: Xây dựng trợ lý ảo hỗ trợ tìm kiếm và tra cứu cấu hình laptop cho khách hàng bằng ngôn ngữ tự nhiên (VD: "Tìm laptop lập trình dưới 20 triệu").
- **Kiến trúc hệ thống đề xuất**: Microservices.
  - **Backend chính (Java/Spring Boot)**: Quản lý API, giao diện website, giỏ hàng, người dùng.
  - **Backend AI (Python - FastAPI/Flask)**: Đóng vai trò là "bộ não AI", xử lý ngôn ngữ và đưa ra khuyến nghị.
- **Luồng giao tiếp**: Người dùng -> Spring Boot -> REST API -> Python -> Spring Boot -> Người dùng. Cả 2 hệ thống có thể kết nối chung vào MySQL `centerpoint_db` để truy xuất sản phẩm.

## 2. Thuật toán AI Tìm kiếm & Khuyến nghị (Python)
Để đảm bảo tiêu chí **tốc độ phản hồi cực nhanh**, **không tốn chi phí/thời gian huấn luyện**, và **chạy mượt trên CPU máy chủ bình thường**:

- **Bước 1 - Lọc thông số chính xác (Entity Extraction)**: 
  - Sử dụng Regex (Biểu thức chính quy) hoặc spaCy để "bắt" chính xác các con số và yêu cầu phần cứng.
  - Ví dụ: Khách chat "RAM 16GB, dưới 15 triệu" -> Chuyển thành truy vấn SQL lọc cứng `WHERE price < 15000000 AND ram = '16GB'`. Đảm bảo độ chính xác 100%.
  
- **Bước 2 - Tìm kiếm theo ngữ nghĩa (Semantic Vector Search)**:
  - Dùng mô hình mã nguồn mở trên HuggingFace tải về là dùng ngay (VD: `keepitreal/vietnamese-sbert` hoặc `paraphrase-multilingual-MiniLM-L12-v2`).
  - Đưa thông tin sản phẩm và câu hỏi của khách hàng về dạng Vector (dãy số).
  - Sử dụng Database Vector **FAISS** (của Facebook) để tìm ra các Vector sản phẩm có khoảng cách gần nhất với yêu cầu của khách hàng.

## 3. Hệ thống Gợi ý Bán hàng (Recommender System)
Để tăng doanh thu (Cross-sell/Up-sell) thông qua việc gợi ý sản phẩm cá nhân hóa dựa trên lịch sử và hành vi tìm kiếm, có thể tích hợp các thuật toán sau vào Backend Python:

- **Gợi ý dựa trên nội dung (Content-Based Filtering)**:
  - Phân tích thông số kỹ thuật (RAM, CPU, Hãng) của laptop mà khách hàng đang xem hoặc đã mua.
  - Sử dụng **Cosine Similarity** (Đo lường khoảng cách Vector) để tự động gợi ý các laptop có cấu hình và mức giá tương đương.
  - *Ví dụ: Đang xem MSI Cyborg -> Gợi ý Acer Nitro, Asus TUF.*

- **Lọc cộng tác (Collaborative Filtering)**:
  - Phân tích chéo hành vi của hàng ngàn người dùng khác nhau.
  - Thuật toán khuyên dùng: **Matrix Factorization (SVD)** hoặc thư viện **LightFM** (của Python).
  - *Ví dụ: "Khách hàng mua laptop này cũng thường mua thêm chuột Logitech và Balo".*

- **Cá nhân hóa theo lịch sử (Personalized Ranking)**:
  - Lưu trữ log hành vi của khách hàng (Lượt xem = 1 điểm, Thêm vào giỏ = 3 điểm, Mua hàng = 5 điểm).
  - Kết hợp với thuật toán AI tìm kiếm ở phần 2: Khi khách tìm từ khóa "Laptop", hệ thống tự động đẩy (boost) các sản phẩm thuộc Hãng hoặc Tầm giá mà khách hàng này hay click lên đầu trang tìm kiếm.

---
*(Các ghi chú phát triển tính năng mới sẽ được cập nhật tiếp tục tại đây)*
