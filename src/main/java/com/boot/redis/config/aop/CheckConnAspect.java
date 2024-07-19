package com.boot.redis.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @CheckConn Annotation 사용시, 트랜잭션 관련 로그 확인
 * Transaction active
 * Current transaction name
 * Current transaction isolation level
 */
@Aspect
@Component
@Slf4j
public class CheckConnAspect {

    @Before("@annotation(com.boot.redis.config.annotation.CheckConn)")
    public void logTransactionInfo() {
        boolean isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        Integer currentTransactionIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();

        log.info("@@ Is transaction active: {}", isActualTransactionActive);
        log.info("@@ Current transaction name: {}", currentTransactionName);
        log.info("@@ Current transaction isolation level: {}", currentTransactionIsolationLevel);
    }

}
