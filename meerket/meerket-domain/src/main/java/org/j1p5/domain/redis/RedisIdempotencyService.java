package org.j1p5.domain.redis;

/**
 * @author yechan
 */
public interface RedisIdempotencyService {

    /**
     * 요청 ID 저장(멱등성 보장)
     *
     * @param requestId 요청 고유 ID
     * @param ttl 키 만료 시간(초)
     * @return true: 성공적으로 저장됨, false: 이미 처리된 요청
     */
    boolean saveRequestId(String requestId, long ttl);
}
