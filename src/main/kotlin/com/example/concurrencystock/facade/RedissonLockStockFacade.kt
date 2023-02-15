package com.example.concurrencystock.facade

import com.example.concurrencystock.aop.RedissonLock
import com.example.concurrencystock.service.StockService
import org.springframework.stereotype.Repository

/**
 * @author nespot2
 **/
@Repository
class RedissonLockStockFacade(
    private val stockService: StockService,
) {
    @RedissonLock(key = "productId")
    fun decrease(productId: Long, quantity: Long) {
        stockService.decrease(productId = productId, quantity = quantity)
    }
}