package org.j1p5.api.chat.event;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.redis.RedisService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * @author WebSocket 이벤트 처리를 이용하여 채팅방 입장,퇴장 처리
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final RedisService redisService;

    /**
     * 채팅방 접속 시 Redis에 유저-룸 매핑 정보 추가
     *
     * @param event
     */
    @EventListener
    public void handleWebSocketSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = getUserId(headerAccessor);
        String roomId = getRoomId(headerAccessor);

        log.info("roomId = {}", roomId);
        log.info("userId = {}", userId);

        headerAccessor.getSessionAttributes().put("userId", userId);

        redisService.saveUserRoomMapping(userId, roomId);
        log.info("사용자 {}가 채팅방 {}에 입장했습니다. Redis에 기록했습니다.", userId, roomId);
    }

    /**
     * 채팅방 퇴장 시 Redis에서 유저 정보 제거
     *
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        redisService.removeUserRoomMapping(userId);
        log.info("사용자 {}의 채팅방 정보가 Redis에서 삭제되었습니다.", userId);
    }

    private String getUserId(StompHeaderAccessor headerAccessor) {
        List<String> userIdHeaders = headerAccessor.getNativeHeader("userId");

        if (userIdHeaders == null || userIdHeaders.isEmpty()) {
            throw new NullPointerException("WebSocket 요청에서 userId 헤더를 찾을 수 없습니다.");
        }

        return userIdHeaders.get(0);
    }

    private String getRoomId(StompHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        if (destination == null || !destination.startsWith("/sub/chatroom/")) {
            log.error("잘못된 요청입니다. destination: {}", destination);
            throw new IllegalArgumentException(
                    "WebSocket 요청에서 유효하지 않은 roomId를 받았습니다: " + destination);
        }

        String roomId = destination.split("/sub/chatroom/")[1];
        if (roomId == null || roomId.isEmpty()) {
            log.error("roomId가 비어 있습니다. destination: {}", destination);
            throw new IllegalArgumentException(
                    "WebSocket 요청에서 유효하지 않은 roomId를 받았습니다: " + destination);
        }

        return roomId;
    }
}
