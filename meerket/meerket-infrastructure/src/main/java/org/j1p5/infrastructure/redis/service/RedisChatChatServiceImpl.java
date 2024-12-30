package org.j1p5.infrastructure.redis.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.redis.RedisChatService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yechan
 */
@Service
@RequiredArgsConstructor
public class RedisChatChatServiceImpl implements RedisChatService {

    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveUserRoomMapping(String userId, String roomId) {
        String redisKey = "user:" + userId;
        redisTemplate.opsForValue().set(redisKey, roomId);
    }

    @Override
    public String getUserCurrentRoom(String userId) {
        String redisKey = "user:" + userId;
        return redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void removeUserRoomMapping(String userId) {
        String redisKey = "user:" + userId;
        redisTemplate.delete(redisKey);
    }
}
