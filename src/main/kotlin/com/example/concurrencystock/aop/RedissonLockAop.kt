package com.example.concurrencystock.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.lang.reflect.Method


/**
 * @author nespot2
 **/
@Aspect
@Component
class RedissonLockAop(
    private val redissonClient: RedissonClient,
) {


    @Around("@annotation(RedissonLock)")
    @Throws(Throwable::class)
    private fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val redissonLock: RedissonLock = method.getAnnotation(RedissonLock::class.java)
        val args = joinPoint.args

        val key = createKey(parameterNames = signature.parameterNames, args = args, key = redissonLock.key)
        val lock = redissonClient.getLock(key)

        try {
            val available = lock.tryLock(redissonLock.waitTime, redissonLock.leaseTime, redissonLock.timeUnit)
            if (!available) {
                throw RuntimeException("lock 획득 실패")
            }
            return joinPoint.proceed(args)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }

    private fun createKey(parameterNames: Array<String>, args: Array<Any>, key: String): String? {
        for ((i, name) in parameterNames.withIndex()) {
            if (name == key) {
                return args[i].toString()
            }
        }
        return null
    }
}