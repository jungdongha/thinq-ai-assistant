package com.example.demo.domain.ai.service;

import com.example.demo.domain.thinq.tool.ThinQDeviceTools;
import com.example.demo.domain.thinq.tool.ThinQRouteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final ThinQDeviceTools thinQDeviceTools;
    private final ThinQRouteTools thinQRouteTools;

    public AiService(
            @Qualifier("claudeChatClient") ChatClient chatClient,
            ThinQDeviceTools thinQDeviceTools,
            ThinQRouteTools thinQRouteTools
    ) {
        this.chatClient = chatClient;
        this.thinQDeviceTools = thinQDeviceTools;
        this.thinQRouteTools = thinQRouteTools;
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .tools(thinQDeviceTools, thinQRouteTools)
                .call()
                .content();
    }
}
