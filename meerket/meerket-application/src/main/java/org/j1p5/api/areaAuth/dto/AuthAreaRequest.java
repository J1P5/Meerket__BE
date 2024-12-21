package org.j1p5.api.areaAuth.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.j1p5.domain.areaAuth.vo.AreaAuthInfo;

public record AuthAreaRequest(
        @NotNull
        @DecimalMax(value = "90.0", message = "위도는 90도를 넘을 수 없습니다.")
        @DecimalMin(value = "-90.0", message = "위도는 -90도보다 작을 수 없습니다.")
        Double latitude,

        @NotNull
        @DecimalMax(value = "180.0", message = "경도는 180도를 넘을 수 없습니다.")
        @DecimalMin(value = "-180.0", message = "경도는 -180도보다 작을 수 없습니다.")
        Double longitude,

        @NotNull
        Integer emdId) {
    public static AreaAuthInfo toInfo(AuthAreaRequest request) {
        return new AreaAuthInfo(request.longitude(), request.latitude(), request.emdId());
    }
}
