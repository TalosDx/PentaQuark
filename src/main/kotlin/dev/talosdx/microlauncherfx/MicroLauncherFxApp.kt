package dev.talosdx.microlauncherfx

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
    log.info { "Start application" }
    log.debug { "some output" }


    log.info { "Stop application" }
}