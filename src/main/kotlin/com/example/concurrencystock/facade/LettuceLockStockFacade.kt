package com.example.concurrencystock.facade

import com.example.concurrencystock.repository.RedisLockRepository
import com.example.concurrencystock.service.StockService
import org.springframework.stereotype.Service

/**
 * @author nespot2
 **/
@Service
class LettuceLockStockFacade(
    private val redisRepository: RedisLockRepository,
    private val stockService: StockService
) {
    fun decrease(key: Long, quantity: Long) {
        while (!redisRepository.lock(key = key)) {
            Thread.sleep(50)
        }
        try {
            stockService.decrease(productId = key, quantity = quantity)
        } finally {
            redisRepository.unlock(key = key)
        }
    }
}