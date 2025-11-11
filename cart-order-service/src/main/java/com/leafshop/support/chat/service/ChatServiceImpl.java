package com.leafshop.support.chat.service;

import com.leafshop.support.chat.entity.ChatMessage;
import com.leafshop.support.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public ChatMessage sendMessage(Long userId, Long staffId, String message, boolean fromCustomer) {
        ChatMessage chat = ChatMessage.builder()
                .userId(userId)  // 🔹 đổi customerId → userId
                .staffId(staffId)
                .message(message)
                .fromCustomer(fromCustomer)
                .sentAt(LocalDateTime.now())
                .build();
        return chatRepository.save(chat);
    }

    @Override
    public List<ChatMessage> getChatHistory(Long userId) {
        return chatRepository.findByUserIdOrderBySentAtAsc(userId); // 🔹 đổi từ customerId → userId
    }
}
