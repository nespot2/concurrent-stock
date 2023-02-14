package com.example.concurrencystock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConcurrencystockApplication

fun main(args: Array<String>) {
    runApplication<ConcurrencystockApplication>(*args)
}
