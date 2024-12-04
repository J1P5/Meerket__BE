package org.j1p5.api.activityArea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.dto.ActivityAreaSettingRequest;
import org.j1p5.api.activityArea.usecase.ActivityAreaSettingUsecase;
import org.j1p5.api.activityArea.usecase.ActivityAreaReadUsecase;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.springframework.web.bind.annotation.*;

@Tag(name = "activity-areas", description = "활동 지역 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activity-areas")
public class ActivityAreaController {

    private final ActivityAreaReadUsecase activityAreaReadUsecase;
    private final ActivityAreaSettingUsecase activityAreaSettingUsecase;

    @Operation(summary = "근처 지역 조회", description = "좌표 근처 읍면동 조회 API")
    @GetMapping
    public Response<PageResult<ActivityAreaFullAddress>> getAreas(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return Response.onSuccess(activityAreaReadUsecase.getAreas(longitude, latitude, page, size));
    }

    @Operation(summary = "동네 설정 등록", description = "동네 설정 등록 API")
    @PostMapping
    public Response<Void> register(
            @LoginUser Long userId,
            @Valid @RequestBody ActivityAreaSettingRequest request
    ) {
        System.out.println(userId);
        activityAreaSettingUsecase.register(userId, request.emdId());
        return Response.onSuccess();
    }

    @Operation(summary = "설정 동네 삭제", description = "설정한 동네 삭제 API")
    @DeleteMapping
    public Response<Void> delete(
            @LoginUser Long userId,
            @Valid @RequestBody ActivityAreaSettingRequest request
    ) {
        activityAreaSettingUsecase.delete(userId, request.emdId());
        return Response.onSuccess();
    }
}
