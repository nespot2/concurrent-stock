package com.example.concurrencystock.facade

import com.example.concurrencystock.domain.Stock
import com.example.concurrencystock.repository.StockRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

/**
 * @author nespot2
 */
internal class LettuceLockStockFacadeTest @Autowired constructor(
    private val lettuceLockStockFacade: LettuceLockStockFacade,
    private val stockRepository: StockRepository
) : IntegrationTest() {
    @BeforeEach
    fun before() {
        val stock = Stock(
            productId = 1,
            quantity = 100
        )
        stockRepository.save(stock)
    }

    @AfterEach
    fun after() {
        stockRepository.deleteAll()
    }

    @Test
    fun `a hundred requests`() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(8)

        val latch = CountDownLatch(threadCount)

        for (i in 1..100) {
            executorService.submit {
                lettuceLockStockFacade.decrease(productId = 1, quantity = 1)
                latch.countDown()
            }
        }

        latch.await()
        val stock =
            stockRepository.findFirstByProductId(productId = 1) ?: throw RuntimeException("stock을 찾을 수 없습니다.")
        assertEquals(0, stock.quantity)
    }

}