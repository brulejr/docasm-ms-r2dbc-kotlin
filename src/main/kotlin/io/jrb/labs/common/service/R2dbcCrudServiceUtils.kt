package io.jrb.labs.common.service

import io.jrb.labs.common.model.Entity
import io.jrb.labs.common.model.EntityBuilder
import io.jrb.labs.common.repository.EntityRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

class R2dbcCrudServiceUtils<E: Entity>(
    override val entityClass: Class<E>,
    override val repository: EntityRepository<E>,
    override val entityName: String = entityClass.simpleName
) : CrudServiceUtils<E> {

    override fun createEntity(entityBuilder: EntityBuilder<E>): Mono<E> {
        return Mono.fromCallable {
            val timestamp: Instant = Instant.now()
            entityBuilder
                .guid(UUID.randomUUID())
                .createdOn(timestamp)
                .modifiedOn(timestamp)
                .build()
        }
            .flatMap { repository.save(it) }
            .onErrorResume(handleServiceError("Unexpected error when creating $entityName"))
    }

    override fun deleteEntity(guid: UUID): Mono<Void> {
        return repository.findByGuid(guid)
            .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, guid)))
            .flatMap { repository.delete(it) }
            .onErrorResume(handleServiceError("Unexpected error when deleting $entityName"))
    }

    override fun findEntityByGuid(guid: UUID): Mono<E> {
        return repository.findByGuid(guid)
            .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, guid)))
            .onErrorResume(handleServiceError("Unexpected error when finding $entityName"))
    }

    override fun listAllEntities(): Flux<E> {
        return repository.findAll()
            .onErrorResume(handleServiceError("Unexpected error when retrieving $entityName"))
    }

}
