package org.j1p5.domain.redis;

/**
 * @author yechan
 */
public interface RedisBidLockService {

    /**
     * 입찰 시작: 키 증가 또는 생성
     *
     * @param key Redis 키
     * @param ttl 키 만료 시간(초)
     * @return 현재 키의 값
     */
    long incrementBid(String key, long ttl);

    /**
     * 입찰 종료: 키 감소 또는 삭제
     *
     * @param key Redis 키
     * @return 현재 키의 값
     */
    long decrementBid(String key);

    /**
     * 현재 입찰이 진행되고 있는지를 확인
     *
     * @param key Redis 키
     * @return 현재 키의 값 (없으면 0 반환)
     */
    long getBidCount(String key);
}
