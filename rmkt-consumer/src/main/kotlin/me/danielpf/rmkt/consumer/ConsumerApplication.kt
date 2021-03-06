package me.danielpf.rmkt.consumer

import me.danielpf.rmkt.core.Constants.Companion.PRODUCT_EXCHANGE_TOPIC
import me.danielpf.rmkt.core.ObjectMapperExtension
import me.danielpf.rmkt.core.ProductExchange
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Function


@SpringBootApplication
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
}


@Configuration
class ConsumerConfig {

    @Bean
    fun productExchangeConsumer(operations: ReactiveRedisOperations<String, ProductExchange>)
            : Function<Flux<ProductExchange>, Mono<Void>> =
        Function {
            it.filter { pe -> pe.product.price > 500.00 }
                .log()
                .flatMap { pe -> operations.convertAndSend(PRODUCT_EXCHANGE_TOPIC, pe) }
                .then()
        }

    @Bean
    fun productExchangeReactiveRedisOperations(factory: ReactiveRedisConnectionFactory)
            : ReactiveRedisOperations<String, ProductExchange> =

        Jackson2JsonRedisSerializer(ProductExchange::class.java).also {
            it.setObjectMapper(ObjectMapperExtension.instance)
        }.let {
            RedisSerializationContext.newSerializationContext<String, ProductExchange>(StringRedisSerializer())
                .value(it).build()
        }.let { ReactiveRedisTemplate<String, ProductExchange>(factory, it) }

}