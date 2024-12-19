package org.j1p5.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.product.converter.MultipartFileConverter;
import org.j1p5.api.user.dto.request.ProfileSettingRequest;
import org.j1p5.api.user.dto.response.ProfileResponse;
import org.j1p5.api.user.usecase.UserProfileReadUsecase;
import org.j1p5.api.user.usecase.UserProfileSettingUsecase;
import org.j1p5.api.user.usecase.UserWithdrawUsecase;
import org.j1p5.common.exception.ErrorResponse;
import org.j1p5.domain.user.UserProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "users", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserProfileSettingUsecase userProfileSettingUsecase;
    private final UserProfileReadUsecase userProfileReadUsecase;
    private final UserWithdrawUsecase userWithdrawUsecase;

    @GetMapping("/profile")
    @Operation(summary = "유저 프로필 조회", description = "유저 프로필 조회 API")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "프로필 조회 성공",
                            content = @Content(schema = @Schema(implementation = ProfileResponse.class))
                    )
            })
    public Response<ProfileResponse> readProfile(@LoginUser Long userId) {
        UserProfile userProfile = userProfileReadUsecase.execute(userId);
        return Response.onSuccess(ProfileResponse.from(userProfile));
    }

    @PostMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "유저 프로필 업데이트", description = "로그인 후 등록, 마이페이지 수정 시 유저 프로필 업데이트 API")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "프로필 설정 성공"),
                    @ApiResponse(responseCode = "400", description = "1. 닉네임 중복 \t\n 2. 15자 이상 입력 \t\n "
                            + "3. null값  \t\n 4. 파일 확장자가 없습니다. \\t\\n" +
                            "5. 유효한 파일 확장자가 아닙니다. \\t\\n\" + 6. 빈 파일입니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "1. 파일 업로드 중 오류가 발생했습니다. \t\n" +
                            "2. 이미지 업로드 중 IO 예외가 발생했습니다",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            })
    public Response<Void> registerProfile(
            @LoginUser Long userId,
            @Valid @RequestPart(name = "request") ProfileSettingRequest request,
            @RequestPart(name = "image", required = false) MultipartFile imageFile
    ) {
        if (imageFile == null) {
            userProfileSettingUsecase.execute(userId, request.name(), null);
            return Response.onSuccess();
        }

        userProfileSettingUsecase.execute(userId, request.name(), MultipartFileConverter.convertMultipartFileToFile(imageFile));
        return Response.onSuccess();
    }

    @Operation(summary = "유저 세션 확인용 API", description = "유저 세션 확인용 임시 API")
    @GetMapping("/session")
    public Response<UserProfile> getUserInfo(@LoginUser Long userId) {
        UserProfile userProfile = userProfileReadUsecase.getSessionInfo(userId);
        return Response.onSuccess(userProfile);
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API")
    @GetMapping("/withdraw")
    public Response<Void> withdraw(
            @LoginUser Long userId,
            HttpServletRequest request
    ) {
        userWithdrawUsecase.execute(userId);
        request.getSession().invalidate();

        return Response.onSuccess();
    }
}