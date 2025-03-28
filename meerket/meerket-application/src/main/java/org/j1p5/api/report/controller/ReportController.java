package org.j1p5.api.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.block.dto.BlockRegisterRequest;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.product.converter.MultipartFileConverter;
import org.j1p5.api.product.dto.request.ProductCreateRequestDto;
import org.j1p5.api.report.dto.ReportRegisterRequest;
import org.j1p5.api.report.service.ReportRegisterService;
import org.j1p5.common.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
@Tag(name = "reports", description = "신고 관련 API")
public class ReportController {

    private final ReportRegisterService reportRegisterService;

    @Operation(summary = "신고", description = "신고하기 API, reportType은 \"USER\", \"POST\", \"COMMENT\" 문자열중 하나여야 한다.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> registerReport(
            @LoginUser Long userId,
            @RequestPart(name = "request") ReportRegisterRequest request, //TODO : 이미지 저장 방식 변경
            @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) {
        List<File> imageFiles = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            imageFiles = MultipartFileConverter.convertMultipartFilesToFiles(images);
        }

        reportRegisterService.register(userId, ReportRegisterRequest.toInfo(request), imageFiles);
        return Response.onSuccess();
    }
}
