package org.j1p5.infrastructure.redis.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.j1p5.domain.redis.RedisBidLockService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

/**
 * @author yechan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisBidLockServiceImpl implements RedisBidLockService {

    private final RedissonClient redissonClient;

    /**
     * 입찰 시작: 키 증가 또는 생성
     *
     * @param key Redis 키
     * @param ttl 키 만료 시간(초)
     * @return 현재 키의 값
     */
    @Override
    public long incrementBid(String key, long ttl) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        long currentValue = atomicLong.incrementAndGet();

        log.info("현재 입찰 락 증가  key = {}", key);
        log.info("현재 입찰 락 증가  count = {}", currentValue);

        if (currentValue == 1) {
            atomicLong.expire(Duration.ofSeconds(ttl));
        }

        return currentValue;
    }

    /**
     * 입찰 종료: 키 감소 또는 삭제
     *
     * @param key Redis 키
     * @return 현재 키의 값
     */
    @Override
    public long decrementBid(String key) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        long currentValue = atomicLong.decrementAndGet();

        log.info("현재 입찰 락 감소  key = {}", key);
        log.info("현재 입찰 락 감소  count = {}", currentValue);

        if (currentValue <= 0) {
            atomicLong.delete();
        }

        return currentValue;
    }

    /**
     * 현재 입찰이 진행되고 있는지를 확인
     *
     * @param key Redis 키
     * @return 현재 키의 값 (없으면 0 반환)
     */
    @Override
    public long getBidCount(String key) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);

        log.info("현재 입찰 중 개수 = {}", atomicLong);

        return atomicLong.get();
    }
}
