package com.example.concurrencystock.service

import com.example.concurrencystock.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author nespot2
 **/
@Service
class StockService(
    private val stockRepository: StockRepository
) {
    @Transactional
    fun decrease(productId: Long, quantity: Long) {
        val stock =
            stockRepository.findFirstByProductId(productId = productId)
                ?: throw RuntimeException("stock을 찾을 수 없습니다")
        stock.decrease(quantity = quantity)
    }
}