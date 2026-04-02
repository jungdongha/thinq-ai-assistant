package com.example.demo.domain.thinq.tool;

import com.example.demo.domain.thinq.exception.ThinQException;
import com.example.demo.domain.thinq.exception.ThinQExceptionInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ThinQEventTools {

    private final RestClient thinQRestClent;

    @Tool(description = "이벤트 메시지를 구독 중인 디바이스 ID 목록을 조회합니다.")
    public List<String> getEventSubscriptions(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country
    ) {
        return thinQRestClent.get()
                .uri("/event")
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(EventListApiResponse.class)
                .response().stream()
                .map(EventItem::deviceId)
                .toList();
    }

    private record EventListApiResponse(String messageId, String timestamp, List<EventItem> response) {}
    private record EventItem(String deviceId) {}
}
