package com.example.demo.domain.ai.service;

import com.example.demo.domain.thinq.tool.ThinQDeviceTools;
import com.example.demo.domain.thinq.tool.ThinQEnergyTools;
import com.example.demo.domain.thinq.tool.ThinQEventTools;
import com.example.demo.domain.thinq.tool.ThinQPushTools;
import com.example.demo.domain.thinq.tool.ThinQRouteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final ThinQDeviceTools thinQDeviceTools;
    private final ThinQRouteTools thinQRouteTools;
    private final ThinQPushTools thinQPushTools;
    private final ThinQEventTools thinQEventTools;
    private final ThinQEnergyTools thinQEnergyTools;

    public AiService(
            @Qualifier("claudeChatClient") ChatClient chatClient,
            ThinQDeviceTools thinQDeviceTools,
            ThinQRouteTools thinQRouteTools,
            ThinQPushTools thinQPushTools,
            ThinQEventTools thinQEventTools,
            ThinQEnergyTools thinQEnergyTools
    ) {
        this.chatClient = chatClient;
        this.thinQDeviceTools = thinQDeviceTools;
        this.thinQRouteTools = thinQRouteTools;
        this.thinQPushTools = thinQPushTools;
        this.thinQEventTools = thinQEventTools;
        this.thinQEnergyTools = thinQEnergyTools;
    }

    public String chat(String message) {
        return chatClient.prompt()
                .user(message)
                .tools(thinQDeviceTools, thinQRouteTools, thinQPushTools, thinQEventTools, thinQEnergyTools)
                .call()
                .content();
    }
}
