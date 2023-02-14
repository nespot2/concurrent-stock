package com.example.concurrencystock.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

/**
 * @author nespot2
 **/
@Repository
class RedisLockRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {

    fun lock(key: Long): Boolean {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(generateKey(key = key), "lock", Duration.ofMillis(3_000)) ?: false

    }

    fun unlock(key: Long): Boolean {
        return redisTemplate.delete(generateKey(key = key))
    }

    fun generateKey(key: Long): String {
        return key.toString()
    }

}