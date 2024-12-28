package org.j1p5.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.j1p5.domain.redis.RedisProductEditLockService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yechan
 */
@Service
@RequiredArgsConstructor
public class RedisProductEditLockServiceImpl implements RedisProductEditLockService {

    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 상품 수정할때의 Lock
     *
     * @param key
     * @param ttl
     * @return true: lock 성공, false: 이미 lock 되어있음
     */
    @Override
    public boolean setEditLock(String key, long ttl) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(key, "LOCKED", ttl, TimeUnit.SECONDS));
    }

    /**
     * 상품이 수정중인지 확인
     *
     * @param key
     * @return true: 상품이 수정중인 상태, false: 상품이 수정중 아닌상태
     */
    @Override
    public boolean isEditLocked(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 상품의 수정이 완료되고 나서 Lock 해제
     *
     * @param key
     */
    @Override
    public void releaseEditLock(String key) {
        redisTemplate.delete(key);
    }
}
