package com.leafshop.support.chat.service;

import com.leafshop.support.chat.entity.ChatMessage;
import java.util.List;

public interface ChatService {
    ChatMessage sendMessage(Long userId, Long staffId, String message, boolean fromCustomer);
    List<ChatMessage> getChatHistory(Long userId);
}
