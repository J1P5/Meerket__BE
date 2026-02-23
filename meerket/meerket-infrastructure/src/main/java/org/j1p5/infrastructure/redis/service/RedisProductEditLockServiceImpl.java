package org.j1p5.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.redis.RedisProductEditLockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisProductEditLockServiceImpl implements RedisProductEditLockService {

    private final RedissonClient redissonClient;

    @Override
    public boolean setEditLock(String key, long ttl) {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean locked = lock.tryLock(0, ttl, TimeUnit.SECONDS);
            log.info("상품 수정 락 설정 key={}, locked={}, lease={}s", key, locked, ttl);
            return locked;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("상품 수정 락 설정 interrupted key={}", key, e);
            return false;
        }
    }

    @Override
    public boolean isEditLocked(String key) {
        RLock lock = redissonClient.getLock(key);
        boolean locked = lock.isLocked();
        log.info("상품 수정 락 확인 key={}, locked={}", key, locked);
        return locked;
    }

    @Override
    public void releaseEditLock(String key) {
        RLock lock = redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.info("상품 수정 락 해제 key={}", key);
        } else {
            log.warn("상품 수정 락 해제 스킵(현재 스레드 미보유) key={}", key);
        }
    }
}