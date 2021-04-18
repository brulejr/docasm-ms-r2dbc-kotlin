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

import io.jrb.labs.docasm.model.Document
import io.jrb.labs.docasm.model.EntityType
import io.jrb.labs.docasm.model.LookupValueType
import io.jrb.labs.docasm.repository.DocumentRepository
import io.jrb.labs.docasm.resource.DocumentRequest
import io.jrb.labs.docasm.resource.DocumentResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class DocumentService(
    val documentRepository: DocumentRepository,
    val lookupValueService: LookupValueService
) {

    @Transactional
    fun createDocument(documentRequest: DocumentRequest): Mono<DocumentResource> {
        return Mono.just(
            Document.Builder()
                .type(documentRequest.type)
                .name(documentRequest.name)
        )
            .flatMap { createEntity( it, Document::class.java, documentRepository) }
            .flatMap { author ->
                val authorId = author.id!!
                Mono.zip(
                    Mono.just(author),
                    lookupValueService.createLookupValues(EntityType.DOCUMENT.name, authorId, LookupValueType.TAG.name, documentRequest.tags)
                )
            }
            .map { tuple -> DocumentResource(document = tuple.t1, tags = tuple.t2) }
    }


    @Transactional
    fun deleteDocument(guid: UUID): Mono<Void> {
        return findEntityByGuid(guid, Document::class.java, documentRepository)
            .flatMap<Void?> { document -> lookupValueService.deleteLookupValues(EntityType.DOCUMENT.name, document.id!!) }
            .then(deleteEntity(guid, Document::class.java, documentRepository))
    }

    @Transactional
    fun findDocumentByGuid(guid: UUID): Mono<DocumentResource> {
        return findEntityByGuid(guid, Document::class.java, documentRepository)
            .zipWhen { document -> lookupValueService.findLookupValueList(EntityType.DOCUMENT.name, document.id!!) }
            .map { tuple -> DocumentResource(document = tuple.t1, tags = tuple.t2) }
    }

    @Transactional
    fun listAllDocuments(): Flux<DocumentResource> {
        return listAllEntities(Document::class.java, documentRepository)
            .map { DocumentResource(document = it) }
    }

}
