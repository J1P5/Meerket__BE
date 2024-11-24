package org.j1p5.api.product.controller;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.global.response.Response;
import org.j1p5.api.product.dto.request.ProductRequestDto;
import org.j1p5.api.product.service.ProductUsecase;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUsecase productUsecase; //생성자 주입


    /**
     * @author sunghyun0610
     * @param request
     * @param images
     * @param userDetails
     * @return 200, 등록 완료 메세지
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response makeProduct(@RequestPart(name = "request") ProductRequestDto request ,
                                @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                @AuthenticationPrincipal UserDetails userDetails){

        String userEmail = userDetails.getUsername();//유저 정보를 가져오기 위함
        productUsecase.registerProduct(userEmail,request,images);

        return Response.onSuccess();
    }

}
