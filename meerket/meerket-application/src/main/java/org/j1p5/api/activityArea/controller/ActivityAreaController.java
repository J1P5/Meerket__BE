package org.j1p5.api.activityArea.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.usecase.ActivityAreaUsecase;
import org.j1p5.api.global.response.Response;
import org.j1p5.common.annotation.CursorDefault;
import org.j1p5.common.dto.Cursor;
import org.j1p5.common.dto.CursorResult;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "activity-areas", description = "활동 지역 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activity-areas")
public class ActivityAreaController {

    private final ActivityAreaUsecase activityAreaUsecase;

    @GetMapping
    public Response<PageResult<ActivityAreaFullAddress>> getAreas(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return Response.onSuccess(activityAreaUsecase.getAreas(longitude, latitude, page, size));
    }

}
