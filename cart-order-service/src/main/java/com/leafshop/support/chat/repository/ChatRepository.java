package com.leafshop.support.chat.repository;

import com.leafshop.support.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    // 🔹 đổi từ customerId → userId
    List<ChatMessage> findByUserIdOrderBySentAtAsc(Long userId);
}
