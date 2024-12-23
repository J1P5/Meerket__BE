package org.j1p5.api.block.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.block.dto.BlockDeleteRequest;
import org.j1p5.api.block.dto.BlockRegisterRequest;
import org.j1p5.api.block.service.BlockDeleteService;
import org.j1p5.api.block.service.BlockReadService;
import org.j1p5.api.block.service.BlockRegisterService;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.block.BlockUserInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
@Tag(name = "blocks", description = "차단 관련 API")
public class BlockController {
    private final BlockRegisterService blockRegisterService;
    private final BlockReadService blockReadService;
    private final BlockDeleteService blockDeleteService;

    @Operation(summary = "차단", description = "사용자 차단 등록 API")
    @PostMapping
    public Response<Void> registerBlock(
            @LoginUser Long userId, @RequestBody BlockRegisterRequest blockRegisterRequest) {
        blockRegisterService.register(userId, blockRegisterRequest.blockUserId());
        return Response.onSuccess();
    }

    @Operation(summary = "차단 조회", description = "사용자 차단 조회 API")
    @GetMapping
    public Response<PageResult<BlockUserInfo>> readBlocks(
            @LoginUser Long userId,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        return Response.onSuccess(blockReadService.read(userId, page, size));
    }

    @Operation(summary = "차단 해제", description = "사용자 차단 해제 API")
    @DeleteMapping
    public Response<Void> deleteBlock(
            @LoginUser Long userId, @RequestBody BlockDeleteRequest blockDeleteRequest) {
        blockDeleteService.delete(userId, blockDeleteRequest.unblockId());
        return Response.onSuccess();
    }
}
