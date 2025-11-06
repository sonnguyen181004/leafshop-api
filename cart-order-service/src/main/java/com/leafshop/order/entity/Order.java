package com.leafshop.order.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // Có thể null nếu checkout guest

    @Builder.Default
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount = 0.0;

    @Builder.Default
    @Column(length = 50, nullable = false)
    private String status = "PENDING"; // PENDING, CONFIRMED, SHIPPED...

    @Builder.Default
    @Column(name = "payment_status", length = 50, nullable = false)
    private String paymentStatus = "UNPAID"; // UNPAID, PAID, REFUNDED

    @Column(name = "assigned_staff_id")
    private Long assignedStaffId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "PENDING";
        if (paymentStatus == null) paymentStatus = "UNPAID";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}