package org.j1p5.api.activityArea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.dto.ActivityAreaRegisterRequest;
import org.j1p5.api.activityArea.usecase.ActivityAreaRegisterUsecase;
import org.j1p5.api.activityArea.usecase.ActivityAreaUsecase;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.common.annotation.CursorDefault;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "activity-areas", description = "활동 지역 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activity-areas")
public class ActivityAreaController {

    private final ActivityAreaUsecase activityAreaUsecase;
    private final ActivityAreaRegisterUsecase activityAreaRegisterUsecase;

    @Operation(summary = "근처 지역 조회", description = "좌표 근처 읍면동 조회 API")
    @GetMapping
    public Response<PageResult<ActivityAreaFullAddress>> getAreas(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return Response.onSuccess(activityAreaUsecase.getAreas(longitude, latitude, page, size));
    }

    @Operation(summary = "동네 설정 등록", description = "동네 설정 등록 API")
    @PostMapping
    public Response<Void> register(
            @LoginUser Long userId,
            @Valid @RequestBody ActivityAreaRegisterRequest request
    ) {
        System.out.println(userId);
        activityAreaRegisterUsecase.execute(userId, request.emdId());
        return Response.onSuccess();
    }

}
