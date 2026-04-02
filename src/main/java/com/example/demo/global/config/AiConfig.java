package com.example.demo.global.config;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;

@Configuration
public class AiConfig {

    //PromptTemplate
    String systemPrompts;
    {
        try {
            systemPrompts = new ClassPathResource("prompts/system.st").getContentAsString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            systemPrompts = "";
        }
    }

    //chat memory (inmemory)
    @Bean
    ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    //gemini
    @Bean("geminiChatClient")
    ChatClient geminiChatClient(GoogleGenAiChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("당신은 정확하고 논리적이고 친절한 어시스턴트이다.")
                .build();
    }

    //claude
    @Bean("claudeChatClient")
    ChatClient claudeChatClient(AnthropicChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem(systemPrompts)
                .build();
    }

    //groq (openai-compatible)
    @Bean("groqChatClient")
    ChatClient groqChatClient(
            @Value("${spring.ai.groq.api-key}") String apiKey,
            @Value("${spring.ai.groq.base-url}") String baseUrl,
            @Value("${spring.ai.groq.model}") String model,
            @Value("${spring.ai.groq.max-tokens}") int maxTokens) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model(model)
                .maxTokens(maxTokens)
                .build();
        return ChatClient.builder(OpenAiChatModel.builder()
                        .openAiApi(openAiApi)
                        .defaultOptions(options)
                        .build())
                .defaultSystem(systemPrompts)
                .build();
    }
}
