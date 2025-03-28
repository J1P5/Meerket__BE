package org.j1p5.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.redis.RedisIdempotencyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yechan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisIdempotencyServiceImpl implements RedisIdempotencyService {

    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 요청 ID 저장(멱등성 보장)
     *
     * @param requestId 요청 고유 ID
     * @param ttl 키 만료 시간(초)
     * @return true: 성공적으로 저장됨, false: 이미 처리된 요청
     */
    @Override
    public boolean saveRequestId(String requestId, long ttl) {
        Boolean success =
                redisTemplate
                        .opsForValue()
                        .setIfAbsent(requestId, "PROCESSED", ttl, TimeUnit.SECONDS);

        log.info("멱등성 여부 확인 결과 requestId = {}, 결과 =  {}", requestId, success);

        return success != null && success;
    }
}
