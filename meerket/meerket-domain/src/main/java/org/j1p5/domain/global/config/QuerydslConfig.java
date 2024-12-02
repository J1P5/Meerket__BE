package org.j1p5.domain.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    @PersistenceContext private EntityManager entityManager;

    @Bean // 이 메서드가 반환하는 객체를 빈으로 등록한다
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
    // JPAQueryFactory : QueryDSL의 핵심 클래스 중 하나로, 타입 세이프한 쿼리를 생성하고 실행하는 기능을 제공
    // entityManger기반으 생성됨
}
