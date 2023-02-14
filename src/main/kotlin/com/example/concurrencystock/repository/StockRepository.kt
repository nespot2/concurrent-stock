package com.example.concurrencystock.repository

import com.example.concurrencystock.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType

/**
 * @author nespot2
 **/
interface StockRepository : JpaRepository<Stock, Long> {

    fun findFirstByProductId(productId: Long): Stock?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.productId = :productId")
    fun findFirstByProductIdWithPessimisticLock(productId: Long): Stock?

}