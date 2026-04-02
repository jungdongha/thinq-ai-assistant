package com.example.demo.global.config;

import com.example.demo.global.properties.ThinQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

@Configuration
@EnableConfigurationProperties(ThinQProperties.class)
public class RestClientConfig {

    @Bean
    RestClient thinQRestClent(ThinQProperties thinQProperties) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(thinQProperties.readTimeout());

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(thinQProperties.baseUrl())
                .defaultHeader("x-api-key", thinQProperties.apiKey())
                .defaultHeader("x-client-id", thinQProperties.clientId())
                .defaultHeader("x-service-phase", "OP")
                .defaultHeader("Authorization", "Bearer " + thinQProperties.patToken())
                .requestInterceptor(messageIdInterceptor())
                .build();
    }

    private ClientHttpRequestInterceptor messageIdInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add("x-message-id", generateMessageId());
            return execution.execute(request, body);
        };
    }

    private String generateMessageId() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bb.array());
    }
}
