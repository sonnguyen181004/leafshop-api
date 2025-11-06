    package com.leafshop.cart.entity;

    import com.fasterxml.jackson.annotation.JsonManagedReference;
    import jakarta.persistence.*;
    import lombok.*;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Table(name = "carts",
            indexes = {
                    @Index(name = "idx_cart_user", columnList = "userId"),
                    @Index(name = "idx_cart_session", columnList = "sessionId")
            })
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Cart {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Nếu user đã login
        private Long userId;

        // Nếu khách chưa login thì dùng sessionId
        @Column(length = 100)
        private String sessionId;

        // Xóa precision/scale vì Double không hỗ trợ
        @Builder.Default
        @Column(nullable = false)
        private Double totalPrice = 0.0;

        @Builder.Default
        @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
        @JsonManagedReference
        private List<CartItem> items = new ArrayList<>();

        // Logic kiểm tra: phải có userId hoặc sessionId (chỉ 1 trong 2)
        @PrePersist
        @PreUpdate
        private void validateCartOwner() {
            if ((userId == null && (sessionId == null || sessionId.isEmpty()))
                    || (userId != null && sessionId != null)) {
                throw new IllegalArgumentException("Cart must have either userId OR sessionId (only one allowed).");
            }
        }
    }
