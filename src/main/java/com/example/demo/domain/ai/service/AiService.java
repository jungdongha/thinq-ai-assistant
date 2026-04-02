package com.example.demo.domain.ai.service;

import com.example.demo.domain.ai.exception.AiException;
import com.example.demo.domain.ai.exception.AiExceptionInformation;
import com.example.demo.domain.thinq.tool.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiService {
    private final Map<String, ChatClient> chatClients;
    private final ChatMemory chatMemory;
    private final ThinQDeviceTools thinQDeviceTools;
    private final ThinQRouteTools thinQRouteTools;
    private final ThinQPushTools thinQPushTools;
    private final ThinQEventTools thinQEventTools;
    private final ThinQEnergyTools thinQEnergyTools;

    public AiService(
            Map<String, ChatClient> chatClients,
            ChatMemory chatMemory,
            ThinQDeviceTools thinQDeviceTools,
            ThinQRouteTools thinQRouteTools,
            ThinQPushTools thinQPushTools,
            ThinQEventTools thinQEventTools,
            ThinQEnergyTools thinQEnergyTools
    ) {
        this.chatClients = chatClients;
        this.chatMemory = chatMemory;
        this.thinQDeviceTools = thinQDeviceTools;
        this.thinQRouteTools = thinQRouteTools;
        this.thinQPushTools = thinQPushTools;
        this.thinQEventTools = thinQEventTools;
        this.thinQEnergyTools = thinQEnergyTools;
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

        try {
            return selectedClient.prompt()
                    .user(message)
                    .advisors(spec -> spec
                            .advisors(
                                    new SimpleLoggerAdvisor(),
                                    MessageChatMemoryAdvisor.builder(chatMemory)
                                            .conversationId(conversationId)
                                            .build()
                            )
                            .param("conversationId", conversationId))
                    .tools(thinQDeviceTools, thinQRouteTools, thinQPushTools, thinQEventTools, thinQEnergyTools)
                    .call()
                    .content();
        } catch (Exception e) {
            // AI 호출 중 예외 발생 시 도메인 예외로 래핑
            throw new AiException(AiExceptionInformation.AI_CLIENT_ERROR);
        }
    }
}
