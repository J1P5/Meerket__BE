package org.j1p5.api.comment.controller;

import lombok.RequiredArgsConstructor;

import org.j1p5.api.comment.dto.request.CommentCreateRequestDto;
import org.j1p5.api.comment.usecase.CommentCreateUsecase;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/comments")
public class CommentController {

    private final CommentCreateUsecase commentCreateUsecase;

    @PostMapping("/{productId}")
    public Response<Void> createComment(@PathVariable(name = "productId") Long productId,
                                        @LoginUser Long userId,
                                        @RequestBody CommentCreateRequestDto request) {

        commentCreateUsecase.createComment(productId,userId,request);
        return Response.onSuccess();
    }
}