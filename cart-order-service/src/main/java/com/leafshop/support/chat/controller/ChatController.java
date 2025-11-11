package com.leafshop.support.chat.controller;

import com.leafshop.support.chat.entity.ChatMessage;
import com.leafshop.support.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 🔹 Gửi tin nhắn
    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(
            @RequestParam Long userId,                 // đổi từ customerId → userId
            @RequestParam(required = false) Long staffId,
            @RequestParam String message,
            @RequestParam boolean fromCustomer) {
        return ResponseEntity.ok(chatService.sendMessage(userId, staffId, message, fromCustomer));
    }

    // 🔹 Lấy lịch sử chat của user
    @GetMapping("/history/{userId}")               // đổi path variable
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getChatHistory(userId));
    }
}
