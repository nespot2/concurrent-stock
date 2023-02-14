package com.example.concurrencystock.service

import com.example.concurrencystock.repository.VStockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author nespot2
 **/
@Service
class OptimisticLockStockService(
    private val vStockRepository: VStockRepository
) {


    @Transactional
    fun decrease(productId: Long, quantity: Long) {
        val stock = vStockRepository.findFirstByProductIdWithOptimisticLock(productId = productId)
            ?: throw RuntimeException("stock을 찾을 수 없습니다")
        stock.decrease(quantity = quantity)
    }

}