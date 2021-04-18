package io.jrb.labs.docasm.service

import io.jrb.labs.common.service.ServiceException
import mu.KLogger
import reactor.core.publisher.Mono

fun <R> handleServiceError(log: KLogger, message: String): (Throwable) -> Mono<R> {
    return { e ->
        if (e !is ServiceException) {
            log.error(e.message, e)
        }
        Mono.error(if (e is ServiceException) e else ServiceException(message, e))
    }
}
