package com.example.demo.domain.thinq.tool;

import com.example.demo.domain.thinq.dto.response.DeviceItem;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
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
        ThinQDeviceApiResponse apiResponse = thinQRestClent.get()
                .uri("/devices")
                .header("x-country", country)
                .retrieve()
                .body(ThinQDeviceApiResponse.class);

        return apiResponse.response();
    }

    @Tool(description = "디바이스 프로파일(제어 가능한 속성 정의)을 조회합니다.")
    public Map<String, Object> getDeviceProfile(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId
    ) {
        ThinQObjectApiResponse apiResponse = thinQRestClent.get()
                .uri("/devices/{deviceId}/profile", deviceId)
                .header("x-country", country)
                .retrieve()
                .body(ThinQObjectApiResponse.class);

        return apiResponse.response();
    }

    @Tool(description = "디바이스의 현재 상태를 조회합니다.")
    public Map<String, Object> getDeviceState(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId
    ) {
        ThinQObjectApiResponse apiResponse = thinQRestClent.get()
                .uri("/devices/{deviceId}/state", deviceId)
                .header("x-country", country)
                .retrieve()
                .body(ThinQObjectApiResponse.class);

        return apiResponse.response();
    }

    private record ThinQDeviceApiResponse(String messageId, String timestamp, List<DeviceItem> response) {}
    private record ThinQObjectApiResponse(String messageId, String timestamp, Map<String, Object> response) {}
}
