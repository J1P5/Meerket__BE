package org.j1p5.api.areaAuth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.areaAuth.dto.AuthAreaRequest;
import org.j1p5.api.areaAuth.usecase.AreaAuthUsecase;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.common.exception.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/area-auth")
@Tag(name = "area-auth", description = "동네 인증 관련 API")

public class AreaAuthController {

    private final AreaAuthUsecase areaAuthUsecase;

    @PostMapping
    @Operation(summary = "지역 인증", description = "게시글 등록, 입찰 시 지역인증 API")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "지역 인증 성공"),
                    @ApiResponse(responseCode = "400", description = "1. 위도 경도 값이 범위를 벗어났습니다. \t\n" +
                            "2. emdId는 필수 값입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "1. 지역인증 히스토리/유저/읍면동 지역을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            })
    public Response<Void> registerProfile(
            @LoginUser Long userId,
            @Valid @RequestBody AuthAreaRequest request
    ) {
        areaAuthUsecase.execute(userId, AuthAreaRequest.toInfo(request));

        return Response.onSuccess();
    }
}
