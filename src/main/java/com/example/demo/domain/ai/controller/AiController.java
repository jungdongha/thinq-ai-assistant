package com.example.demo.domain.ai.controller;


import com.example.demo.domain.ai.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Gemini AI 채팅 API")
public class AiController {
    private final AiService aiService;

    @PostMapping("/chat")
    @Operation(summary = "Gemini 채팅", description = "Gemini AI에게 메시지를 보내고 응답을 받습니다")
    public String chat(@RequestBody Map<String, String> request) {
        return aiService.chat(request.get("message"));
    }
}
