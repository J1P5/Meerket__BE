package org.j1p5.domain.redis;

public interface RedisProductEditLockService {

    /**
     * 상품 수정할때의 Lock
     *
     * @param key
     * @param ttl
     * @return true: lock 성공, false: 이미 lock 되어있음
     */
    boolean setEditLock(String key, long ttl);

    /**
     * 상품이 수정중인지 확인
     *
     * @param key
     * @return true: 상품이 수정중인 상태, false: 상품이 수정중 아닌상태
     */
    boolean isEditLocked(String key);

    /**
     * 상품의 수정이 완료되고 나서 Lock 해제
     *
     * @param key
     */
    void releaseEditLock(String key);
}
