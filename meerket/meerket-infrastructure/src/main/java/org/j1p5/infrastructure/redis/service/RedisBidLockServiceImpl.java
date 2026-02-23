package org.j1p5.infrastructure.redis.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.redis.RedisBidLockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisBidLockServiceImpl implements RedisBidLockService {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryLock(String key, long waitTimeSec, long leaseTimeSec) {
        RLock lock = redissonClient.getLock(key);
        try {
            boolean locked = lock.tryLock(waitTimeSec, leaseTimeSec, TimeUnit.SECONDS);
            log.info("RLock tryLock key={}, locked={}, wait={}s, lease={}s", key, locked, waitTimeSec, leaseTimeSec);
            return locked;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("RLock tryLock interrupted key={}", key, e);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.info("RLock unlock key={}", key);
        } else {
            log.warn("RLock unlock skipped (not held by current thread) key={}", key);
        }
    }

    @Override
    public boolean isLocked(String key) {
        RLock lock = redissonClient.getLock(key);
        boolean locked = lock.isLocked();
        log.info("RLock isLocked key={}, locked={}", key, locked);
        return locked;
    }
}