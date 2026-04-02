package com.example.demo.domain.ai.service;

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
        // 기본값: groqChatClient
        String clientBeanName = "groqChatClient";
        
        // 요청된 모델명이 있고 해당 빈이 존재하면 교체
        if (modelName != null && chatClients.containsKey(modelName + "ChatClient")) {
            clientBeanName = modelName + "ChatClient";
        }
        
        ChatClient selectedClient = chatClients.get(clientBeanName);

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
    }
}
