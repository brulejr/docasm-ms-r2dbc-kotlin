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
import java.util.UUID

@Service
class AuthorService(
    val authorRepository: AuthorRepository,
    val lookupValueService: LookupValueService
) {

    val log = KotlinLogging.logger {}

    @Transactional
    fun create(authorRequest: AuthorRequest): Mono<AuthorResource> {
        return Mono.just(Author.Builder().name(authorRequest.name))
            .flatMap { createEntity( it, Author::class.java, authorRepository) }
            .flatMap { author ->
                val authorId = author.id!!
                Mono.zip(
                    Mono.just(author),
                    lookupValueService.createLookupValues(EntityType.AUTHOR.name, authorId, LookupValueType.TAG.name, authorRequest.tags)
                )
            }
            .map { tuple -> AuthorResource(author = tuple.t1, tags = tuple.t2) }
    }

    @Transactional
    fun findByGuid(guid: UUID): Mono<AuthorResource> {
        return findEntityByGuid(guid, Author::class.java, authorRepository)
            .zipWhen { document -> lookupValueService.findLookupValueList(EntityType.AUTHOR.name, document.id!!) }
            .map { tuple -> AuthorResource(author = tuple.t1, tags = tuple.t2) }
    }

    @Transactional
    fun listAll(): Flux<AuthorResource> {
        return listAllEntities(Author::class.java, authorRepository)
            .map { AuthorResource(author = it) }
    }

}
