package me.danielpf.rmkt.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

class Constants {
    companion object {

        const val PRODUCT_EXCHANGE_TOPIC = "topic:pe"

        const val DEFAULT_CURRENCY = "USD"
    }
}

object ObjectMapperExtension {
    val instance: ObjectMapper = jacksonObjectMapper().registerModules(Jdk8Module(), JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}