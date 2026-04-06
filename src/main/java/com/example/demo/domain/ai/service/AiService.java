package com.example.demo.domain.ai.service;

import com.example.demo.domain.ai.exception.AiException;
import com.example.demo.domain.ai.exception.AiExceptionInformation;
import com.example.demo.domain.thinq.tool.ThinQTool;
import com.example.demo.global.advisor.CustomLoggingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AiService {
    private final Map<String, ChatClient> chatClients;
    private final ChatMemory chatMemory;
    private final List<ThinQTool> thinQTools;
    private final String systemPromptTemplate;

    public AiService(
            Map<String, ChatClient> chatClients,
            ChatMemory chatMemory,
            List<ThinQTool> thinQTools,
            @Qualifier("systemPromptTemplate") String systemPromptTemplate
    ) {
        this.chatClients = chatClients;
        this.chatMemory = chatMemory;
        this.thinQTools = thinQTools;
        this.systemPromptTemplate = systemPromptTemplate;
    }

    public String chat(String modelName, String conversationId, String message) {
        String clientBeanName;
        
        if (modelName == null) {
            // 모델명이 없으면 기본값인 groqChatClient 사용
            clientBeanName = "groqChatClient";
        } else {
            // 모델명이 명시되었을 때, 지원하는 모델인지 확인
            String expectedBeanName = modelName + "ChatClient";
            if (!chatClients.containsKey(expectedBeanName)) {
                throw new AiException(AiExceptionInformation.MODEL_NOT_FOUND);
            }
            clientBeanName = expectedBeanName;
        }
        
        ChatClient selectedClient = chatClients.get(clientBeanName);

        String currentDate = LocalDate.now().toString();
        String systemPrompt = systemPromptTemplate.replace("{currentDate}", currentDate);

        try {
            return selectedClient.prompt()
                    .system(systemPrompt)
                    .user(message)
                    .advisors(spec -> spec
                            .advisors(
                                    new CustomLoggingAdvisor(),
                                    MessageChatMemoryAdvisor.builder(chatMemory)
                                            .conversationId(conversationId)
                                            .build()
                            )
                            .param("conversationId", conversationId))
                    .tools(thinQTools.toArray())
                    .call()
                    .content();
        } catch (Exception e) {
            // AI 호출 중 예외 발생 시 도메인 예외로 래핑
            throw new AiException(AiExceptionInformation.AI_CLIENT_ERROR);
        }
    }
}
