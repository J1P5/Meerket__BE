
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.user.dto.NameRegisterRequest;
import org.j1p5.api.user.usecase.UserNameRegisterUsecase;
import org.j1p5.common.exception.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "users", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserNameRegisterUsecase userNameRegisterUsecase;

    @PostMapping("/nickname")
    @Operation(summary = "유저 닉네임 등록", description = "로그인 후 추가 설정 과정 중 닉네임 등록 API")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "닉네임 설정 성공"),
                    @ApiResponse(
                            responseCode = "400",
                            description =
                                    "1. 닉네임 중복 \t\n 2. 15자 이상 입력 \t\n "
                                            + "3. null값  \t\n ",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public Response<Void> registerNickname(
            @LoginUser Long userId, @RequestBody @Valid NameRegisterRequest request) {
        userNameRegisterUsecase.execute(userId, request.name());
        return Response.onSuccess();
    }
}