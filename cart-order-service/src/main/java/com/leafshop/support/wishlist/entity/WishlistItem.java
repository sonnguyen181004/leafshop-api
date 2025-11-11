package com.leafshop.support.wishlist.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;      // user đã thêm sản phẩm yêu thích

    @Column(nullable = false)
    private Long productId;   // id sản phẩm

    @Column(length = 255, nullable = false)
    private String productName;

    @Column(nullable = false)
    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
    }
}
