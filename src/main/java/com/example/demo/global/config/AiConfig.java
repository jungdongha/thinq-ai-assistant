package com.example.demo.global.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean("geminiChatClient")
    ChatClient geminiChatClient(GoogleGenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("당신은 정확하고 논리적이고 친절한 어시스턴트이다.")
                .build();
    }

    @Bean("claudeChatClient")
    ChatClient claudeChatClient(AnthropicChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("당신은 정확하고 논리적이고 친절한 어시스턴트이다.")
                .build();
    }
}
