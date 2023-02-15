package com.example.concurrencystock.facade

import com.example.concurrencystock.aop.LettuceLock
import com.example.concurrencystock.service.StockService
import org.springframework.stereotype.Service

/**
 * @author nespot2
 **/
@Service
class LettuceLockStockFacade(
    private val stockService: StockService
) {

    @LettuceLock(key = "productId")
    fun decrease(productId: Long, quantity: Long) {
        stockService.decrease(productId = productId, quantity = quantity)
    }
}