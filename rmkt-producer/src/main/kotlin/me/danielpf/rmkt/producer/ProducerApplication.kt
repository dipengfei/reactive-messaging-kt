package me.danielpf.rmkt.producer

import me.danielpf.rmkt.core.Product
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier
import kotlin.random.Random

@SpringBootApplication
class ProducerApplication

fun main(args: Array<String>) {
    runApplication<ProducerApplication>(*args)
}

@Configuration
class SourceConfig {

    @Bean
    fun productSource(): Supplier<Flux<Product>> = Supplier {
        Flux.interval(Duration.ofSeconds(1)).map {
            Product(
                UUID.randomUUID().toString(),
                RandomStringUtils.randomAlphanumeric(5, 10),
                Random.nextDouble(1000.00),
                LocalDateTime.now()
            )
        }.onBackpressureDrop().log()
    }

}
