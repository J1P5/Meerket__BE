package org.j1p5.api.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.j1p5.api.comment.dto.request.CommentCreateRequestDto;
import org.j1p5.api.comment.dto.request.CommentDeleteRequestDto;
import org.j1p5.api.comment.dto.request.CommentUpdateRequestDto;
import org.j1p5.api.comment.dto.response.CommentReadResponseDto;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.api.comment.usecase.CommentCreateUsecase;
import org.j1p5.api.comment.usecase.CommentDeleteUsecase;
import org.j1p5.api.comment.usecase.CommentReadUsecase;
import org.j1p5.api.comment.usecase.CommentUpdateUsecase;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/comments")
public class CommentController {

    private final CommentCreateUsecase commentCreateUsecase;
    private final CommentReadUsecase commentReadUsecase;
    private final CommentUpdateUsecase commentUpdateUsecase;
    private final CommentDeleteUsecase commentDeleteUsecase;

    @PostMapping("/{productId}")
    public Response<Void> createComment(@PathVariable(name = "productId") Long productId,
                                        @LoginUser Long userId,
                                        @RequestBody CommentCreateRequestDto request) {

        commentCreateUsecase.createComment(productId,userId,request);
        return Response.onSuccess();
    }

    @GetMapping("/{productId}")
    public Response<List<CommentReadResponseDto>> getComment(@PathVariable(name = "productId") Long productId,
                                                            @LoginUser Long userId,
                                                            @RequestParam(name = "page",defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size){

        Pageable pageable = PageRequest.of(page,size);
        return Response.onSuccess(commentReadUsecase.getAllComments(productId,userId,pageable));
    }

    @PatchMapping("/{commentId}")
    public Response<Void> updateComment(@PathVariable(name = "commentId") Long commentId,
                                        @LoginUser Long userId,
                                        @RequestBody CommentUpdateRequestDto request){
        commentUpdateUsecase.updateComment(commentId,userId,request);
        return Response.onSuccess();
    }

    @DeleteMapping("/{commentId}")
    public Response<Void> deleteComment(@PathVariable(name = "commentId") Long commentId,
                                        @LoginUser Long userId,
                                        @RequestBody CommentDeleteRequestDto request){

        commentDeleteUsecase.removeComment(request.productId(),userId,commentId);
        return Response.onSuccess();

    }
}