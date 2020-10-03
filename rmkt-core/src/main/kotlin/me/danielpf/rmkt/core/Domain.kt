package me.danielpf.rmkt.core

import java.time.LocalDateTime

data class Product(val id: String, val name: String, val price: Double, val createdTime: LocalDateTime)

data class ProductExchange(val product: Product, val localPrices: Map<String, Double>)

enum class ExchangeRate(val rate: Double) {

    USD(1.00),
    CNY(6.7906),
    JPY(105.3246),
    EUR(0.8535),
    GBP(0.773)


}