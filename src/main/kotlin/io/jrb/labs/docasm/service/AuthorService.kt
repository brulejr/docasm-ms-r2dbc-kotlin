package io.jrb.labs.docasm.service

import io.jrb.labs.common.service.ResourceNotFoundException
import io.jrb.labs.common.service.ServiceException
import io.jrb.labs.docasm.model.Author
import io.jrb.labs.docasm.repository.AuthorRepository
import io.jrb.labs.docasm.resource.AuthorRequest
import io.jrb.labs.docasm.resource.AuthorResource
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
    fun create(author: AuthorRequest): Mono<AuthorResource> {
        return Mono.just(Author.Builder().name(author.name).build())
            .map {
                Author.Builder(it)
                    .guid(UUID.randomUUID())
                    .createdOn(Instant.now())
                    .modifiedOn(Instant.now())
                    .build()
            }
            .flatMap { authorRepository.save(it) }
            .flatMap { authorRepository.findByGuid(it.guid!!) }
            .onErrorResume(serviceErrorHandler("Unexpected error when creating Author"))
            .map { AuthorResource(it) }
    }

    @Transactional
    fun findByGuid(guid: UUID): Mono<AuthorResource> {
        return authorRepository.findByGuid(guid)
            .switchIfEmpty(Mono.error(ResourceNotFoundException("Author", guid)))
            .onErrorResume(serviceErrorHandler("Unexpected error when finding 'Author'"))
            .map { AuthorResource(it) }
    }

    @Transactional
    fun listAll(): Flux<AuthorResource> {
        return authorRepository.findAll()
            .onErrorResume(serviceErrorHandler("Unexpected error when retrieving Author"))
            .map { AuthorResource(it) }
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
