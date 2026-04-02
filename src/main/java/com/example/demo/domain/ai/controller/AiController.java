package com.example.demo.domain.ai.controller;

import com.example.demo.domain.ai.dto.request.ChatRequest;
import com.example.demo.domain.ai.dto.response.ChatResponse;
import com.example.demo.domain.ai.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "ThinQ AI 채팅 API")
public class AiController {
    private final AiService aiService;

    @PostMapping("/chat")
    @Operation(summary = "ThinQ AI 채팅", description = "AI에게 메시지를 보내고 응답을 받습니다")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String conversationId = request.conversationId() != null
                ? request.conversationId()
                : UUID.randomUUID().toString();
        String content = aiService.chat(conversationId, request.message());
        return new ChatResponse(content, conversationId);
    }
}
