package io.jrb.labs.docasm.service

import io.jrb.labs.common.service.ResourceNotFoundException
import io.jrb.labs.common.service.ServiceException
import io.jrb.labs.docasm.model.Author
import io.jrb.labs.docasm.projection.AuthorProjection
import io.jrb.labs.docasm.projection.AuthorSummaryProjection
import io.jrb.labs.docasm.repository.AuthorRepository
import io.jrb.labs.docasm.resource.AuthorRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Service
class AuthorService(
    val authorRepository: AuthorRepository
) {

    private val log = KotlinLogging.logger {}

    @Transactional
    fun create(author: AuthorRequest): Mono<AuthorProjection> {
        return Mono.just(Author.Builder().name(author.name).build())
            .map {
                Author.Builder(it)
                    .guid(UUID.randomUUID())
                    .createdOn(Instant.now())
                    .modifiedOn(Instant.now())
                    .build()
            }
            .flatMap { authorRepository.save(it) }
            .flatMap { authorRepository.findAuthorByGuid(it.guid!!) }
            .onErrorResume(serviceErrorHandler("Unexpected error when creating Author"))
    }

    @Transactional
    fun findByGuid(guid: UUID): Mono<AuthorProjection> {
        return authorRepository.findAuthorByGuid(guid)
            .switchIfEmpty(Mono.error(ResourceNotFoundException("Author", guid)))
            .onErrorResume(serviceErrorHandler("Unexpected error when finding 'Author'"))
    }

    @Transactional
    fun listAll(): Flux<AuthorSummaryProjection> {
        return authorRepository.findAllBy()
            .onErrorResume(serviceErrorHandler("Unexpected error when retrieving Author"))
    }

    private fun <R> serviceErrorHandler(message: String): (Throwable) -> Mono<R> {
        return { e ->
            if (e !is ServiceException) {
                log.error(e.message, e)
            }
            Mono.error(if (e is ServiceException) e else ServiceException(message, e))
        }
    }

}
