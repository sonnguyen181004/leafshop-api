1) Môi trường & cách chạy
Yêu cầu

Java 17+ (hoặc bản tương thích với project)

Maven

CSDL: mặc định project dùng H2 (embedded) hoặc DB bạn cấu hình (MySQL/Postgres) — kiểm tra application.properties / application.yml

IDE (IntelliJ/NetBeans) hoặc terminal

Start app

Từ thư mục project:

mvn clean install
mvn spring-boot:run


Hoặc chạy class Main có @SpringBootApplication trong IDE.

Kiểm tra server chạy:

GET http://localhost:8080/hello


=> trả về Hello Leaf Shop! 

2) Kiến trúc ngắn gọn / packages chính

com.leafshop.cart.* — Cart module

Controller: CartController

Service: CartService, CartServiceImpl

Entity: Cart, CartItem

Repository: CartRepository, CartItemRepository

com.leafshop.order.* — Order module

Controller: OrderController

Service: OrderService, OrderServiceImpl

Entity: Order, OrderItem

Repository: OrderRepository, OrderItemRepository

(Optional) com.leafshop.exception.GlobalExceptionHandler — xử lý lỗi trả JSON gọn

3) Danh sách endpoint (tóm tắt)
Cart

GET /api/cart?userId={userId} — Lấy giỏ theo userId (hoặc sessionId)

POST /api/cart/add — Thêm sản phẩm vào giỏ (JSON body)

PUT /api/cart/update — Cập nhật số lượng (JSON body)

DELETE /api/cart/remove — Xóa sản phẩm (JSON body)

GET /api/cart/total?userId={userId} — Tính tổng (subtotal, shippingFee, discount, grandTotal)

POST /api/cart/checkout?userId={userId} — Checkout (trả order summary & clear cart)

Order

POST /api/orders/create — Tạo order từ cart (body: { "userId": 1 } hoặc DTO)

GET /api/orders/history/{userId} — Lấy lịch sử đơn hàng user

GET /api/orders/{orderId} — Lấy chi tiết đơn hàng

PUT /api/orders/{orderId}/status — Cập nhật trạng thái đơn (body { "status": "..." })

PUT /api/orders/{orderId}/payment — Cập nhật trạng thái thanh toán (body { "paymentStatus": "..." })

PUT /api/orders/{orderId}/assign/{staffId} — Gán đơn cho nhân viên

POST /api/orders/{orderId}/return — Xử lý return (đổi/trả)

4) Test tuần tự (end-to-end) — bước từng bước

Trước khi bắt đầu: đảm bảo server đang chạy và port đúng (mặc định 8080).

A. Kiểm tra server
GET http://localhost:8080/hello


Expected: Hello Leaf Shop! 

B. Test module Cart — tuần tự
1) Add item vào cart

Request

POST http://localhost:8080/api/cart/add
Content-Type: application/json
Body:
{
  "userId": 1,
  "sessionId": null,
  "productId": 101,
  "productName": "Áo thun trắng",
  "quantity": 2,
  "price": 150000
}


Expected response (200) — ví dụ:

{
  "success": true,
  "message": "Item added to cart",
  "data": {
    "id": 1,
    "userId": 1,
    "sessionId": null,
    "totalPrice": 300000.0,
    "items": [
      { "id": 3, "productId": 101, "productName": "Áo thun trắng", "quantity": 2, "price": 150000.0 }
    ]
  }
}

2) Lấy giỏ hàng
GET http://localhost:8080/api/cart?userId=1


Expected: data.items chứa sản phẩm đã thêm; totalPrice = quantity × price.

3) Cập nhật số lượng
PUT http://localhost:8080/api/cart/update
Content-Type: application/json
Body:
{ "userId":1, "sessionId":null, "productId":101, "quantity":3 }


Expected: quantity cập nhật, totalPrice thay đổi.

4) Tính tổng
GET http://localhost:8080/api/cart/total?userId=1


Expected: trả { subtotal, shippingFee, discount, grandTotal } theo logic:

shippingFee = 0 nếu subtotal >= 500000, else 30000

discount = 0 (tạm thời)

5) Checkout (chuyển cart → order và clear cart)

Trước checkout: đảm bảo cart có item(s).

POST http://localhost:8080/api/cart/checkout?userId=1


Expected: JSON orderSummary gồm orderId, subtotal, shippingFee, discount, grandTotal, itemCount.
Sau checkout, gọi GET /api/cart?userId=1 → items phải rỗng và totalPrice = 0.0.

C. Test module Order — tuần tự
1) Tạo order từ cart (thực tế OrderServiceImpl tạo order dựa trên CartService.getCart)

Request (nếu bạn đã checkout ở bước trên thì cart đã rỗng; trong flow bình thường bạn tạo order bằng /api/orders/create thay vì /api/cart/checkout nếu muốn)

POST http://localhost:8080/api/orders/create
Content-Type: application/json
Body:
{ "userId": 1 }


Expected: OrderResponse (id, userId, totalAmount, status=PENDING, paymentStatus=UNPAID, items[]).

Lưu ý: nếu cart trống thì API trả lỗi Cart is empty! (tốt nhất xử lý bằng GlobalExceptionHandler thành 400).

2) Lấy lịch sử đơn hàng
GET http://localhost:8080/api/orders/history/1


Expected: array chứa order(s).

3) Lấy chi tiết đơn hàng
GET http://localhost:8080/api/orders/{orderId}

4) Cập nhật trạng thái đơn
PUT http://localhost:8080/api/orders/{orderId}/status


5) Cập nhật trạng thái thanh toán
PUT http://localhost:8080/api/orders/{orderId}/payment
Body: { "paymentStatus": "PAID" }

6) Gán nhân viên
PUT http://localhost:8080/api/orders/{orderId}/assign/10

7) Xử lý return (đổi/trả)
POST http://localhost:8080/api/orders/{orderId}/return


Expected: order.status = RETURNED và nếu bạn có logic tồn kho/hoàn tiền, các bước đó cũng được kích hoạt.
Coupon Module (theo code bạn vừa gửi)
Method	Endpoint	Mô tả
POST	/api/coupons	Tạo mới coupon
PUT	/api/coupons/{id}	Cập nhật coupon
DELETE	/api/coupons/{id}	Xóa coupon
GET	/api/coupons	Lấy danh sách coupon
GET	/api/coupons/{code}	Lấy thông tin coupon theo mã
POST	/api/coupons/apply	Áp dụng coupon cho giỏ hàng/đơn hàng
ạo coupon

POST /api/coupons
Content-Type: application/json
{
  "code": "SALE20",
  "discountType": "PERCENT",
  "discountValue": 20,
  "minOrderValue": 100000,
  "expiryDate": "2025-12-31"
}


2️ Lấy danh sách coupon

GET /api/coupons


3️ Lấy chi tiết coupon theo mã

GET /api/coupons/SALE20


4 Áp dụng coupon

POST /api/coupons/apply
Content-Type: application/json
{
  "userId": 1,
  "couponCode": "SALE20",
  "cartTotal": 300000
}


Expected:

{
  "discountAmount": 60000.0,
  "finalTotal": 240000.0
}


5Cập nhật coupon

PUT /api/coupons/1
{
  "discountValue": 25
}


6️⃣ Xóa coupon

DELETE /api/coupons/1