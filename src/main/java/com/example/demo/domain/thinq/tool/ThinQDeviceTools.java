package com.example.demo.domain.thinq.tool;

import com.example.demo.domain.thinq.dto.response.DeviceItem;
import com.example.demo.domain.thinq.exception.ThinQException;
import com.example.demo.domain.thinq.exception.ThinQExceptionInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThinQDeviceTools {
    private final RestClient thinQRestClent;

    @Tool(description = "ThinQ에 등록된 디바이스 목록을 조회합니다.")
    public List<DeviceItem> getDevices(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country
    ) {
        return thinQRestClent.get()
                .uri("/devices")
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if (response.getStatusCode().value() == 401) throw new ThinQException(ThinQExceptionInformation.THINQ_UNAUTHORIZED);
                    if (response.getStatusCode().value() == 403) throw new ThinQException(ThinQExceptionInformation.THINQ_FORBIDDEN);
                    if (response.getStatusCode().value() == 429) throw new ThinQException(ThinQExceptionInformation.THINQ_RATE_LIMIT);
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(ThinQDeviceApiResponse.class)
                .response();
    }

    @Tool(description = "디바이스 프로파일(제어 가능한 속성 정의)을 조회합니다.")
    public Map<String, Object> getDeviceProfile(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId
    ) {
        return thinQRestClent.get()
                .uri("/devices/{deviceId}/profile", deviceId)
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(ThinQObjectApiResponse.class)
                .response();
    }

    @Tool(description = "디바이스의 현재 상태를 조회합니다.")
    public Map<String, Object> getDeviceState(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId
    ) {
        return thinQRestClent.get()
                .uri("/devices/{deviceId}/state", deviceId)
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(ThinQObjectApiResponse.class)
                .response();
    }

    private record ThinQDeviceApiResponse(String messageId, String timestamp, List<DeviceItem> response) {}
    private record ThinQObjectApiResponse(String messageId, String timestamp, Map<String, Object> response) {}
}
