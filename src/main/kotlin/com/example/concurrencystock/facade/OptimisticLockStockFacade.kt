package com.example.concurrencystock.facade

import com.example.concurrencystock.service.OptimisticLockStockService
import org.springframework.stereotype.Service

/**
 * @author nespot2
 **/
@Service
class OptimisticLockStockFacade(
    private val optimisticLockService: OptimisticLockStockService
) {
    fun decrease(productId: Long, quantity: Long) {
        while (true) {
            try {
                optimisticLockService.decrease(productId = productId, quantity = quantity)
                break
            } catch (e: Exception) {
                Thread.sleep(50)
            }
        }
    }
}