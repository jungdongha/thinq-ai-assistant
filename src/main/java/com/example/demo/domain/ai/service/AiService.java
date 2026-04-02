package com.example.demo.domain.ai.service;

import com.example.demo.domain.thinq.tool.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final ThinQDeviceTools thinQDeviceTools;
    private final ThinQRouteTools thinQRouteTools;
    private final ThinQPushTools thinQPushTools;
    private final ThinQEventTools thinQEventTools;
    private final ThinQEnergyTools thinQEnergyTools;

    public AiService(
            @Qualifier("claudeChatClient") ChatClient chatClient,
            ChatMemory chatMemory,
            ThinQDeviceTools thinQDeviceTools,
            ThinQRouteTools thinQRouteTools,
            ThinQPushTools thinQPushTools,
            ThinQEventTools thinQEventTools,
            ThinQEnergyTools thinQEnergyTools
    ) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.thinQDeviceTools = thinQDeviceTools;
        this.thinQRouteTools = thinQRouteTools;
        this.thinQPushTools = thinQPushTools;
        this.thinQEventTools = thinQEventTools;
        this.thinQEnergyTools = thinQEnergyTools;
    }

    public String chat(String conversationId, String message) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        //작성한 순서대로 실행
                        .advisors(
                                //로깅
                                new SimpleLoggerAdvisor(),
                                //메모리
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
