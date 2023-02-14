package com.example.concurrencystock.repository

import com.example.concurrencystock.domain.Stock
import com.example.concurrencystock.domain.VStock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType

/**
 * @author nespot2
 **/
interface VStockRepository : JpaRepository<VStock, Long> {

    fun findFirstByProductId(productId: Long): Stock?

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT vs FROM VStock vs WHERE vs.productId = :productId")
    fun findFirstByProductIdWithOptimisticLock(productId: Long): VStock?
}