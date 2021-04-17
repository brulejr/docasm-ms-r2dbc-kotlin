package io.jrb.labs.docasm.service

import io.jrb.labs.common.service.ResourceNotFoundException
import io.jrb.labs.common.service.ServiceException
import io.jrb.labs.docasm.model.Author
import io.jrb.labs.docasm.model.EntityType
import io.jrb.labs.docasm.model.LookupValueType
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
    val authorRepository: AuthorRepository,
    val lookupValueService: LookupValueService
) {

    private val log = KotlinLogging.logger {}

    @Transactional
    fun create(authorRequest: AuthorRequest): Mono<AuthorResource> {
        return Mono.just(
            Author.Builder()
                .name(authorRequest.name)
                .guid(UUID.randomUUID())
                .createdOn(Instant.now())
                .modifiedOn(Instant.now())
                .build())
            .flatMap { authorRepository.save(it) }
            .flatMap { author ->
                val authorId = author.id!!
                Mono.zip(
                    Mono.just(author),
                    lookupValueService.createLookupValues(EntityType.AUTHOR.name, authorId, LookupValueType.TAG.name, authorRequest.tags)
                )
            }
            .map { tuple -> AuthorResource(author = tuple.t1, tags = tuple.t2) }
            .onErrorResume(serviceErrorHandler("Unexpected error when creating Author"))
    }

    @Transactional
    fun findByGuid(guid: UUID): Mono<AuthorResource> {
        return authorRepository.findByGuid(guid)
            .switchIfEmpty(Mono.error(ResourceNotFoundException("Author", guid)))
            .zipWhen { document -> lookupValueService.findLookupValueList(EntityType.AUTHOR.name, document.id!!) }
            .map { tuple -> AuthorResource(author = tuple.t1, tags = tuple.t2) }
            .onErrorResume(serviceErrorHandler("Unexpected error when finding 'Author'"))
    }

    @Transactional
    fun listAll(): Flux<AuthorResource> {
        return authorRepository.findAll()
            .map { AuthorResource(author = it) }
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
