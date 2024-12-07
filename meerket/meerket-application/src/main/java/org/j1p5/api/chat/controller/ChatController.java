package org.j1p5.api.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.chat.dto.ChatRoomType;
import org.j1p5.api.chat.dto.request.ChatMessageRequest;
import org.j1p5.api.chat.dto.response.ChatMessageResponse;
import org.j1p5.api.chat.dto.response.ChatRoomEnterResponse;
import org.j1p5.api.chat.dto.response.ChatRoomInfoResponse;
import org.j1p5.api.chat.service.usecase.*;
import org.j1p5.api.global.annotation.LoginUser;
import org.j1p5.api.global.response.Response;
import org.j1p5.domain.chat.vo.MessageInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yechan
 */
@Tag(name = "chats", description = "채팅 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final EnterChatRoomUseCase enterChatRoomUseCase;
    private final ExitChatRoomUseCase exitChatRoomUseCase;
    private final GetChatMessageUseCase getChatMessageUseCase;
    private final SendChatMessageUseCase sendChatMessageUseCase;
    private final GetUserChatRoomsUseCase userChatRoomsUseCase;



    @Operation(summary = "채팅방 입장", description = "특정 채팅방에 입장시 채팅방 정보와 최근 메시지30개를 반환합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "채팅방 입장 성공"),
                    @ApiResponse(responseCode = "400", description = "ROOM_ID_400 roomId 올바르지 않음"),
                    @ApiResponse(responseCode = "404", description = "CHAT_ROOM_404 채팅방을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "CHAT_ROOM_500 채팅방 업데이트 실패(안읽은 메시지 기록)")
            }
    )
    @PostMapping("/enter/{roomId}")
    public Response<ChatRoomEnterResponse> enterChatRoom(
            @PathVariable String roomId,
            @LoginUser Long userId
    ) {

        ChatRoomEnterResponse response = enterChatRoomUseCase.execute(userId, roomId);
        return Response.onSuccess(response);
    }


    @Operation(summary = "채팅방 나가기", description = "특정 채팅방에서 나갑니다. 상대방에게 더 이상 채팅을 받지 못합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "채팅방 나가기 성공"),
                    @ApiResponse(responseCode = "400", description = "ROOM_ID_400 roomId 올바르지 않음"),
                    @ApiResponse(responseCode = "403", description = "CHAT_ROOM_403 채팅방에 속해있지 않은 유저"),
                    @ApiResponse(responseCode = "404", description = "CHAT_ROOM_404 채팅방을 찾을 수 없음")
            }
    )
    @PostMapping("/exit/{roomId}")
    public Response<Void> exitChatRoom(
            @PathVariable String roomId,
            @LoginUser Long userId
    ) {

        exitChatRoomUseCase.execute(userId, roomId);
        return Response.onSuccess();
    }


    @Operation(summary = "메시지 받아오기",
            description = "특정 날짜 이전의 메시지를 30개 조회합니다 beforeTime값이 null일때는 최근메시지를 기준으로 조회합니다..")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "메시지 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "ROOM_ID_400 roomId 올바르지 않음"),
                    @ApiResponse(responseCode = "403", description = "CHAT_ROOM_403 채팅방에 속해있지 않은 유저"),
                    @ApiResponse(responseCode = "404", description = "CHAT_ROOM_404 채팅방을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "CHAT_READ_500 메시지를 불러오던 중 에러 발생")
            }
    )
    @GetMapping("/messages")
    public Response<List<ChatMessageResponse>> getChatMessages(
            @RequestParam String roomId,
            @RequestParam(required = false) LocalDateTime beforeTime,
            @LoginUser Long userId

    ) {

        List<ChatMessageResponse> response = getChatMessageUseCase.execute(roomId, beforeTime, userId);
        return Response.onSuccess(response);
    }


    @Operation(summary = "메시지 보내기", description = "특정 채팅방에 메시지를 보냅니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "메시지 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "ROOM_ID_400 roomId 올바르지 않음"),
                    @ApiResponse(responseCode = "403", description = "CHAT_ROOM_403 채팅방에 속해있지 않은 유저"),
                    @ApiResponse(responseCode = "404", description = "CHAT_ROOM_404 채팅방을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "CHAT_SAVE_500 메시지를 저장 중 에러 발생"),
                    @ApiResponse(responseCode = "500", description = "CHAT_RECEIVER_FIND_500 상대의 접속 여부를 조회중 에러 발생"),
            }
    )
    @MessageMapping("/message")
    public Response<Void> sendChatMessage(
            @RequestBody @Validated ChatMessageRequest request
    ) {

        MessageInfo messageInfo = new MessageInfo(
                request.senderId(), request.receiverId(), request.content(), request.roomId());

        sendChatMessageUseCase.execute(messageInfo);
        return Response.onSuccess();
    }


    @Operation(summary = "채팅방 목록 조회", description = "채팅방 목록을 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "CHAT_ROOM_400 채팅방 목록 조회 타입 에러"),
                    @ApiResponse(responseCode = "500", description = "CHAT_ROOM_500 채팅방 목록 조회 중 서버에러 "),
            }
    )
    @GetMapping
    public Response<List<ChatRoomInfoResponse>> getUserChatRooms(
            @LoginUser Long userId,
            @RequestParam ChatRoomType type
    ) {

        List<ChatRoomInfoResponse> response = userChatRoomsUseCase.execute(userId, type);
        return Response.onSuccess(response);
    }


}
