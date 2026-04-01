package com.example.demo.domain.thinq.tool;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThinQEnergyTools {

    private final RestClient thinQRestClent;

    @Tool(description = "디바이스에서 제공 가능한 에너지 데이터 항목 목록을 조회합니다.")
    public Map<String, Object> getEnergyProfile(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId
    ) {
        EnergyApiResponse apiResponse = thinQRestClent.get()
                .uri("/devices/energy/{deviceId}/profile", deviceId)
                .header("x-country", country)
                .retrieve()
                .body(EnergyApiResponse.class);

        return apiResponse.response();
    }

    @Tool(description = "디바이스의 에너지 사용량 데이터를 조회합니다. period는 DAILY 또는 MONTHLY, 날짜 형식은 YYYYMMDD입니다.")
    public Map<String, Object> getEnergyUsage(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId,
            @ToolParam(description = "조회 기간 단위: DAILY 또는 MONTHLY") String period,
            @ToolParam(description = "시작 날짜 (YYYYMMDD)") String startDate,
            @ToolParam(description = "종료 날짜 (YYYYMMDD)") String endDate
    ) {
        EnergyApiResponse apiResponse = thinQRestClent.get()
                .uri("/devices/energy/{deviceId}/usage?period={period}&startDate={startDate}&endDate={endDate}",
                        deviceId, period, startDate, endDate)
                .header("x-country", country)
                .retrieve()
                .body(EnergyApiResponse.class);

        return apiResponse.response();
    }

    private record EnergyApiResponse(String messageId, String timestamp, Map<String, Object> response) {}
}
