package com.leafshop.support.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;  // đổi từ customerId → userId

    private Long staffId;  // có thể null nếu tin nhắn từ khách hàng

    @Column(length = 1000, nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean fromCustomer; // true = khách gửi, false = nhân viên gửi

    @Column(nullable = false)
    private LocalDateTime sentAt;

    // 🔹 Helper: tự động set thời gian gửi khi tạo message
    @PrePersist
    public void prePersist() {
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}
