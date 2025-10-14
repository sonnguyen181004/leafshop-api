Cách chạy dự án

cd leafshop-api

cd cart-order-service

mvn spring-boot:run

git clone https://github.com/sonnguyen181004/leafshop-api.git

cd leafshop-api/cart-order-service

mvn spring-boot:run



Ứng dụng chạy tại:

 http://localhost:8080

Các API chính
1. Thêm sản phẩm vào giỏ	POST	/api/cart/add	userId, productId, quantity, price	 
2. Xem giỏ hàng hiện tại	GET	/api/cart	userId hoặc sessionId
3. Cập nhật số lượng sản phẩm	PUT	/api/cart/update	userId, productId, quantity	
4. Xóa sản phẩm khỏi giỏ	DELETE	/api/cart/remove	userId, productId
   
Ví dụ sử dụng (Test bằng Postman)

Thêm sản phẩm vào giỏ
POST http://localhost:8080/api/cart/add?userId=1&productId=101&quantity=2&price=250000


Kết quả:

{
  "userId": 1,
  "totalPrice": 500000.0,
  "items": [
    { "productId": 101, "quantity": 2, "price": 250000.0 }
  ]
}

Xem giỏ hàng
GET http://localhost:8080/api/cart?userId=1


→ Hiển thị toàn bộ sản phẩm trong giỏ của user 1.

 Cập nhật số lượng sản phẩm
PUT http://localhost:8080/api/cart/update?userId=1&productId=101&quantity=3


→ Cập nhật lại số lượng, tổng tiền thay đổi theo.

 Xóa sản phẩm khỏi giỏ
DELETE http://localhost:8080/api/cart/remove?userId=1&productId=101


→ Xóa sản phẩm ra khỏi giỏ hàng.

Ví dụ nhiều người dùng
Người dùng	API gọi	Kết quả tổng
User 1	/api/cart/add?userId=1&productId=101&quantity=2&price=250000	totalPrice = 500000
User 2	/api/cart/add?userId=2&productId=202&quantity=1&price=300000	totalPrice = 300000

 Mỗi người có giỏ hàng riêng biệt.

Khách chưa đăng nhập

Có thể dùng sessionId thay userId:

POST http://localhost:8080/api/cart/add?sessionId=ABC123&productId=303&quantity=1&price=200000
