package com.example.concurrencystock.service

import com.example.concurrencystock.domain.Stock
import com.example.concurrencystock.repository.StockRepository
import kotlinx.coroutines.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

/**
 * @author nespot2
 **/
@DataJpaTest
@Import(PessimisticLockStockService::class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PessimisticLockStockServiceTest @Autowired constructor(
    private val pessimisticLockStockService: PessimisticLockStockService,
    private val stockRepository: StockRepository
) {

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
    fun `a hundred requests by using PessimisticLock`() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(16)

        val latch = CountDownLatch(threadCount)

        for (i in 1..100) {
            executorService.submit {
                pessimisticLockStockService.decrease(productId = 1, quantity = 1)
                latch.countDown()
            }
        }

        latch.await()
        val stock =
            stockRepository.findFirstByProductId(productId = 1) ?: throw RuntimeException("stock을 찾을 수 없습니다.")
        assertEquals(0, stock.quantity)
    }

    @Test
    fun `a hundred requests by using PessimisticLock with coroutine`() = runBlocking {

        val action =
            withContext(Dispatchers.Default) {
                val result = mutableListOf<Deferred<Unit>>()
                for (i in 1..100) {
                    result.add(async { asyncDecrease() })
                }
                result
            }
        action.awaitAll()
        val stock =
            stockRepository.findFirstByProductId(productId = 1) ?: throw RuntimeException("stock을 찾을 수 없습니다.")
        assertEquals(0, stock.quantity)
    }

    private suspend fun asyncDecrease() {
        pessimisticLockStockService.decrease(productId = 1, quantity = 1)
    }


}