package com.mna.springbootsecurity.logging.aspect;

import com.mna.springbootsecurity.logging.annotation.LogTransaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class TransactionLoggingAspect {

    @Pointcut("@within(com.mna.springbootsecurity.logging.annotation.LogTransaction)")
    public void logTransactionClassPointcut() {}

    @Pointcut("@annotation(com.mna.springbootsecurity.logging.annotation.LogTransaction)")
    public void logTransactionMethodPointcut() {}

    @Around("logTransactionMethodPointcut() || logTransactionClassPointcut()")
    public Object logTransaction(JoinPoint joinPoint) throws Throwable {
        String transactionId = null;
        LocalDateTime startTime = null;
        String transactionName = null;

        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();

        // Log only if a transaction is active
        if (isTransactionActive) {
            transactionId = UUID.randomUUID().toString();
            startTime = LocalDateTime.now();

            // Here, we're assuming the transaction name could be set somewhere in the service layer.
            // Otherwise, you'd set and manage this name at the point where transactions are started.
            transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
            if (transactionName == null) {
                transactionName = "UnnamedTransaction";
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String methodName = signature.getName();
            String className = signature.getDeclaringTypeName();

            LogTransaction logTransaction = method.getAnnotation(LogTransaction.class);
            if (logTransaction == null) {
                logTransaction = joinPoint.getTarget().getClass().getAnnotation(LogTransaction.class);
            }

            String description = logTransaction != null ? logTransaction.value() : "";

            log.info("Starting transaction:: TransactionId: {}, TransactionName: {}, Method: {}.{}(), Description: {}, StartTime: {}, Transaction Active: {}",
                    transactionId, transactionName, className, methodName, description, startTime, isTransactionActive);
        }

        Object result;
        try {
            result = ((org.aspectj.lang.ProceedingJoinPoint) joinPoint).proceed();

            if (isTransactionActive) {
                LocalDateTime endTime = LocalDateTime.now();
                log.info("Transaction completed successfully:: TransactionId: {}, TransactionName: {}, EndTime: {}, Duration: {}ms",
                        transactionId, transactionName, endTime, java.time.Duration.between(startTime, endTime).toMillis());
            }
        } catch (Throwable throwable) {
            if (isTransactionActive) {
                log.error("Transaction failed. TransactionId:: {}, TransactionName: {}, Error: {}",
                        transactionId, transactionName, throwable.getMessage(), throwable);
            }
            throw throwable;
        }

        return result;
    }

    @AfterThrowing(pointcut = "logTransactionMethodPointcut() || logTransactionClassPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (isTransactionActive) {
            String transactionId = UUID.randomUUID().toString();
            String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
            if (transactionName == null) {
                transactionName = "UnnamedTransaction";
            }

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getName();
            String className = signature.getDeclaringTypeName();

            log.error("Transaction failed:: TransactionId {}, TransactionName: {}, Method: {}.{}(), Error: {}",
                    transactionId, transactionName, className, methodName, exception.getMessage(), exception);
        }
    }
}
