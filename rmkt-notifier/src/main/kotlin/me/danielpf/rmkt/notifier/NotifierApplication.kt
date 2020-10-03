package me.danielpf.rmkt.notifier

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import me.danielpf.rmkt.core.ProductExchange
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.ReplayProcessor
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import javax.annotation.PostConstruct


@SpringBootApplication
class NotifierApplication

fun main(args: Array<String>) {
    runApplication<NotifierApplication>(*args) {
        addInitializers(
            beans {
                bean<ReactiveRedisMessageListenerContainer>()
                bean<NotifyService>()
                bean {

                    ref<NotifyService>().let { notifyService ->
                        router {
                            GET("/pl/{currency}") {
                                ServerResponse.ok().body(
                                    BodyInserters.fromServerSentEvents(
                                        notifyService.notifyEvents(
                                            it.pathVariable("currency")
                                        )
                                    )
                                )
                            }
                        }
                    }

                }
            }
        )
    }
}


class NotifyService(private val container: ReactiveRedisMessageListenerContainer) {

    private val processor = ReplayProcessor.create<ProductExchange>()

    @PostConstruct
    fun init() =
        processor.sink().let { sink ->
            container.receive(
                listOf(ChannelTopic.of("topic:pe")),
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()),
                RedisSerializationContext.SerializationPair.fromSerializer(
                    Jackson2JsonRedisSerializer(ProductExchange::class.java).also {
                        it.setObjectMapper(
                            jacksonObjectMapper().registerModules(Jdk8Module(), JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        )
                    }
                )
            )
                .map { m -> m.message }
                .doOnNext { sink.next(it) }
                .log()
                .subscribe({}, {})
        }

    fun notifyEvents(currency: String): Flux<ServerSentEvent<ProductLocal>> =
        processor.map { p ->

            (if (!p.localPrices.containsKey(currency.toUpperCase())) "USD" else currency.toUpperCase())
                .let {
                    ProductLocal(
                        p.product.id,
                        p.product.name,
                        p.product.createdTime,
                        BigDecimal(p.localPrices[it] ?: 0.0).setScale(4, RoundingMode.HALF_EVEN),
                        it
                    )
                }.let { ServerSentEvent.builder(it).id(it.id).build() }
        }
}

data class ProductLocal(
    val id: String,
    val name: String,
    val createdTime: LocalDateTime,
    val localPrice: BigDecimal,
    val localCurrency: String
)