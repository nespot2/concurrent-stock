package com.example.concurrencystock.aop

import com.example.concurrencystock.repository.RedisLockRepository
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.lang.reflect.Method

/**
 * @author nespot2
 **/
@Aspect
@Component
class LettuceLockAop(
    private val redisRepository: RedisLockRepository,
) {
    @Around("@annotation(LettuceLock)")
    @Throws(Throwable::class)
    private fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val lettuceLock: LettuceLock = method.getAnnotation(LettuceLock::class.java)
        val args = joinPoint.args

        val key = createKey(parameterNames = signature.parameterNames, args = args, key = lettuceLock.key)
            ?: throw RuntimeException("lock 획득 실패")

        while (!redisRepository.lock(key = key.toLong())) {
            Thread.sleep(lettuceLock.sleepMills)
        }

        try {
            return joinPoint.proceed(args)
        } finally {
            redisRepository.unlock(key = key.toLong())
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