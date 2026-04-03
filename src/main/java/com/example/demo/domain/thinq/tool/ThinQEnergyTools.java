package com.example.demo.domain.thinq.tool;

import com.example.demo.domain.thinq.exception.ThinQException;
import com.example.demo.domain.thinq.exception.ThinQExceptionInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThinQEnergyTools implements ThinQTool {

    private final RestClient thinQRestClent;

    @Tool(description = "디바이스에서 제공 가능한 에너지 데이터 항목 목록을 조회합니다.")
    public Map<String, Object> getEnergyProfile(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId
    ) {
        return thinQRestClent.get()
                .uri("/devices/energy/{deviceId}/profile", deviceId)
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("[ThinQ] {} {} → {} body={}", request.getMethod(), request.getURI(), response.getStatusCode(), body);
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(EnergyApiResponse.class)
                .result();
    }

    @Tool(description = """
            디바이스의 에너지 사용량 데이터를 조회합니다.
            - period=DAILY: 하루 단위 조회. startDate와 endDate는 동일한 날짜(YYYYMMDD)를 사용하세요. 예) startDate=20260403, endDate=20260403
            - period=MONTHLY: 월 단위 조회. startDate와 endDate는 해당 월의 1일로 설정하세요(YYYYMMDD). 예) 3월 조회 → startDate=20260301, endDate=20260301
            """)
    public Map<String, Object> getEnergyUsage(
            @ToolParam(description = "ISO 3166-1 alpha-2 국가 코드 (예: KR, US, GB)") String country,
            @ToolParam(description = "디바이스 ID") String deviceId,
            @ToolParam(description = "조회 기간 단위: DAILY 또는 MONTHLY") String period,
            @ToolParam(description = "시작 날짜 (YYYYMMDD). DAILY이면 조회할 날짜, MONTHLY이면 해당 월의 1일") String startDate,
            @ToolParam(description = "종료 날짜 (YYYYMMDD). DAILY이면 startDate와 동일, MONTHLY이면 startDate와 동일") String endDate
    ) {
        return thinQRestClent.get()
                .uri("/devices/energy/{deviceId}/usage?period={period}&startDate={startDate}&endDate={endDate}",
                        deviceId, period, startDate, endDate)
                .header("x-country", country)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("[ThinQ] {} {} → {} body={}", request.getMethod(), request.getURI(), response.getStatusCode(), body);
                    throw new ThinQException(ThinQExceptionInformation.THINQ_API_ERROR);
                })
                .body(EnergyApiResponse.class)
                .result();
    }

    private record EnergyApiResponse(String resultCode, Map<String, Object> result) {}
}
