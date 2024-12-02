package org.j1p5.api.chat.controller;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.dto.ChatRoomType;
import org.j1p5.api.chat.dto.request.ChatMessageRequest;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatRoomEnterResponse;
import org.j1p5.api.chat.dto.response.ChatRoomInfoResponse;
import org.j1p5.api.chat.dto.response.CreateChatRoomResponse;
import org.j1p5.api.chat.service.usecase.*;
import org.j1p5.api.global.response.Response;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yechan
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final CreateChatRoomUseCase createChatRoomUseCase;
    private final EnterChatRoomUseCase enterChatRoomUseCase;
    private final ExitChatRoomUseCase exitChatRoomUseCase;
    private final GetChatMessageUseCase getChatMessageUseCase;
    private final SendChatMessageUseCase sendChatMessageUseCase;
    private final GetUserChatRoomsUseCase userChatRoomsUseCase;


    /**
     * productId를 받아 낙찰자와 판매자간의 채팅방 생성
     *
     * @param productId 상품id
     * @return 생성된 roomId
     */
    @PostMapping("/{roomId}")
    public Response<CreateChatRoomResponse> createChatRoom(
            @PathVariable Long productId
//            @LoginUser Long userId
    ) {
        Long userId = 1L;

        CreateChatRoomResponse response = createChatRoomUseCase.execute(userId, productId);
        return Response.onSuccess(response);
    }


    @PostMapping("/enter/{roomId}")
    public Response<ChatRoomEnterResponse> enterChatRoom(
            @PathVariable String roomId
//            @LoginUser Long userId
    ){
        Long userId = 1L;

        ChatRoomEnterResponse response = enterChatRoomUseCase.execute(userId, roomId);
        return Response.onSuccess(response);
    }


    // TODO 커스텀 예외처리
    @PostMapping("/exit/{roomId}")
    public Response<Void> exitChatRoom(
            @PathVariable String roomId
//            @LoginUser Long userId
    ) throws AccessDeniedException {
        Long userId = 1L;

        exitChatRoomUseCase.execute(userId,roomId);
        return Response.onSuccess();
    }


    // TODO 커스텀 예외처리
    @GetMapping("/messages")
    public Response<List<ChatMessageResponse>> getChatMessages(
            @RequestParam String roomId,
            @RequestParam(required = false) LocalDateTime beforeTime
//            @LoginUser Long userId
            ) throws AccessDeniedException {
        Long userId = 1L;

        List<ChatMessageResponse> response = getChatMessageUseCase.execute(roomId, beforeTime, userId);
        return Response.onSuccess(response);
    }


    // TODO 커스텀 예외처리
    @MessageMapping("/messages")
    public Response<Void> sendChatMessage(
            @RequestBody ChatMessageRequest request
//            @LoginUser Long userId,
            ) throws AccessDeniedException {

        Long userId = 1L;

        sendChatMessageUseCase.execute(userId,request.receiverId(),request.content(), request.roomId());
        return Response.onSuccess();
    }


    @GetMapping
    public Response<List<ChatRoomInfoResponse>> getUserChatRooms(
//            @LoginUser Long userId,
            @RequestParam ChatRoomType type
    ) {
        Long userId = 1L;

        List<ChatRoomInfoResponse> response = userChatRoomsUseCase.execute(userId, type);
        return Response.onSuccess(response);
    }


}
