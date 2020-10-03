package me.danielpf.rmkt.notifier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotifierApplication

fun main(args: Array<String>) {
    runApplication<NotifierApplication>(*args)
}
