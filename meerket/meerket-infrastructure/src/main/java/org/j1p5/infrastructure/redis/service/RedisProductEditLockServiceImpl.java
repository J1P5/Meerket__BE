package org.j1p5.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.redis.RedisProductEditLockService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yechan
 */
@Slf4j
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
        log.info("상품 수정 락 설정");
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
        log.info("상품 수정 락 확인");
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 상품의 수정이 완료되고 나서 Lock 해제
     *
     * @param key
     */
    @Override
    public void releaseEditLock(String key) {
        log.info("상품 수정 락 해제");
        redisTemplate.delete(key);
    }
}
