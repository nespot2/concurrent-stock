package com.example.concurrencystock.facade

import com.example.concurrencystock.service.StockService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

/**
 * @author nespot2
 **/
@Repository
class RedissonLockStockFacade(
    private val redissonClient: RedissonClient,
    private val stockService: StockService,
) {
    fun decrease(key: Long, quantity: Long) {
        val lock = redissonClient.getLock(key.toString())

        try {
            val available = lock.tryLock(5, 1, TimeUnit.SECONDS)
            if (!available) {
                println("lock 획득 실패")
                return
            }
            stockService.decrease(productId = key, quantity = quantity)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }

    }
}