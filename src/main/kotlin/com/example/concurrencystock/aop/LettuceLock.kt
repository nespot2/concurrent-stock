package com.example.concurrencystock.aop

/**
 * @author nespot2
 **/
annotation class LettuceLock(
    val key: String,
    val sleepMills: Long = 50
) {
}