package org.j1p5.domain.redis;

/**
 * @author yechan
 */
public interface RedisBidLockService {

    /**
     * 락 획득 시도
     * @param key 락 키
     * @param waitTimeSec 대기 시간(초) - 0이면 fail-fast
     * @param leaseTimeSec 임대 시간(초) - 이 시간이 지나면 자동 해제
     * @return true: 락 획득 성공
     */
    boolean tryLock(String key, long waitTimeSec, long leaseTimeSec);

    /**
     * 락 해제 (현재 스레드가 보유 중일 때만)
     */
    void unlock(String key);

    /**
     * 락이 잡혀있는지 확인
     */
    boolean isLocked(String key);
}