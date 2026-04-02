package com.example.demo.domain.thinq.tool;

import com.example.demo.domain.thinq.dto.response.RouteResponse;
import com.example.demo.domain.thinq.exception.ThinQException;
import com.example.demo.domain.thinq.exception.ThinQExceptionInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ThinQRouteTools {

    private final RestClient thinQRestClent;

    @Tool(description = "ThinQ 플랫폼의 API 서버, MQTT 서버, 웹소켓 서버 도메인 주소를 조회합니다.")
    public RouteResponse getRoute(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country
    ) {
        return thinQRestClent.get()
                .uri("/route")
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(ThinQRouteApiResponse.class)
                .response();
    }

    private record ThinQRouteApiResponse(String messageId, String timestamp, RouteResponse response) {}
}
