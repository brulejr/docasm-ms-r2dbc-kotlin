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
package io.jrb.labs.docasm.rest

import io.jrb.labs.docasm.resource.DocumentRequest
import io.jrb.labs.docasm.resource.DocumentResource
import io.jrb.labs.docasm.service.DocumentService
import mu.KotlinLogging
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("/api/documents")
class DocumentController(val documentService: DocumentService) {

    private val log = KotlinLogging.logger {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createDocument(@RequestBody documentRequest: DocumentRequest): Mono<EntityModel<DocumentResource>> {
        return documentService.createDocument(documentRequest).map {
            EntityModel.of(it)
                .add(selfLink(it.guid!!))
                .add(collectionLink())
        }
    }

    @DeleteMapping("/{documentGuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDocument(@PathVariable documentGuid: UUID): Mono<Void> {
        return documentService.deleteDocument(documentGuid)
    }

    @GetMapping("/{documentGuid}")
    fun getDocumentById(@PathVariable documentGuid: UUID): Mono<EntityModel<DocumentResource>> {
        return documentService.findDocumentByGuid(documentGuid).map {
            EntityModel.of(it)
                .add(selfLink(documentGuid))
                .add(collectionLink())
        }
    }

    @GetMapping
    fun listDocuments(): Flux<EntityModel<DocumentResource>> {
        return documentService.listAllDocuments().map {
            EntityModel.of(it)
                .add(selfLink(it.guid!!))
        }
    }

    private fun collectionLink(): Link {
        return linkTo(methodOn(javaClass).listDocuments()).withRel("collection")
    }

    private fun selfLink(songGuid: UUID): Link {
        return linkTo(methodOn(javaClass).getDocumentById(songGuid)).withSelfRel()
    }

}
