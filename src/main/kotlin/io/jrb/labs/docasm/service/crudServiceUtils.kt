/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.docasm.service

import io.jrb.labs.common.contract.Entity
import io.jrb.labs.common.contract.EntityBuilder
import io.jrb.labs.common.repository.EntityCrudRepository
import io.jrb.labs.common.service.ResourceNotFoundException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

fun <E: Entity> createEntity(
    entityBuilder: EntityBuilder<E>,
    entityClass: Class<E>,
    repository: EntityCrudRepository<E>,
    entityName: String = entityClass.simpleName
): Mono<E> {
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

fun <E: Entity> deleteEntity(
    guid: UUID,
    entityClass: Class<E>,
    repository: EntityCrudRepository<E>,
    entityName: String = entityClass.simpleName
): Mono<Void> {
    return repository.findByGuid(guid)
        .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, guid)))
        .flatMap { repository.delete(it) }
        .onErrorResume(handleServiceError("Unexpected error when deleting $entityName"))
}

fun <E: Entity> findEntityByGuid(
    guid: UUID,
    entityClass: Class<E>,
    repository: EntityCrudRepository<E>,
    entityName: String = entityClass.simpleName
): Mono<E> {
    return repository.findByGuid(guid)
        .switchIfEmpty(Mono.error(ResourceNotFoundException(entityName, guid)))
        .onErrorResume(handleServiceError("Unexpected error when finding $entityName"))
}

fun <E: Entity> listAllEntities(
    entityClass: Class<E>,
    repository: EntityCrudRepository<E>,
    entityName: String = entityClass.simpleName
): Flux<E> {
    return repository.findAll()
        .onErrorResume(handleServiceError("Unexpected error when retrieving $entityName"))
}
