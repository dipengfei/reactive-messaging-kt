package me.danielpf.rmkt.processor

import me.danielpf.rmkt.core.ExchangeRate
import me.danielpf.rmkt.core.Product
import me.danielpf.rmkt.core.ProductExchange
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Function

@SpringBootApplication
class ProcessorApplication

fun main(args: Array<String>) {
    runApplication<ProcessorApplication>(*args)
}

@Configuration
class ProcessorConfig {

    @Bean
    fun productProcessor(): Function<Flux<Product>, Flux<ProductExchange>> = Function {
        it.log()
            .map { product ->
                ProductExchange(
                    product,
                    ExchangeRate.values()
                        .map { exchange -> exchange.name to exchange.rate * product.price }
                        .toMap()
                )
            }.log()
    }

}