package com.example.concurrencystock.aop

import java.util.concurrent.TimeUnit

/**
 * @author nespot2
 **/
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RedissonLock(
    val key: String,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
    val waitTime: Long = 5,
    val leaseTime: Long = 5,
)