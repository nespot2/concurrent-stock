package com.example.concurrencystock.domain

import javax.persistence.*

/**
 * @author nespot2
 **/
@Entity
class Stock(

    @Column(nullable = false, unique = true)
    var productId: Long,

    @Column(nullable = false)
    var quantity: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

) {

    fun decrease(quantity: Long) {
        if (this.quantity - quantity < 0) {
            throw RuntimeException("수량이 0개 미만입니다.")
        }

        this.quantity -= quantity
    }
}