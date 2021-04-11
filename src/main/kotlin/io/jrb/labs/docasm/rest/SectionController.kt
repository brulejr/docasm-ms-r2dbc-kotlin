/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated sectionation files (the "Software"), to deal
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

import io.jrb.labs.docasm.resource.SectionResource
import io.jrb.labs.docasm.service.SectionService
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
@RequestMapping("/api/sections")
class SectionController(val sectionService: SectionService) {

    private val log = KotlinLogging.logger {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSection(@RequestBody sectionResource: SectionResource): Mono<EntityModel<SectionResource>> {
        return sectionService.createSection(sectionResource).map {
            EntityModel.of(it)
                .add(selfLink(it.guid!!))
                .add(collectionLink())
        }
    }

    @DeleteMapping("/{sectionGuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSection(@PathVariable sectionGuid: UUID): Mono<Void> {
        return sectionService.deleteSection(sectionGuid)
    }

    @GetMapping("/{sectionGuid}")
    fun getSectionById(@PathVariable sectionGuid: UUID): Mono<EntityModel<SectionResource>> {
        return sectionService.findSectionByGuid(sectionGuid).map {
            EntityModel.of(it)
                .add(selfLink(sectionGuid))
                .add(collectionLink())
        }
    }

    @GetMapping
    fun listSections(): Flux<EntityModel<SectionResource>> {
        return sectionService.listAllSections().map {
            EntityModel.of(it)
                .add(selfLink(it.guid!!))
        }
    }

    private fun collectionLink(): Link {
        return linkTo(methodOn(javaClass).listSections()).withRel("collection")
    }

    private fun selfLink(songGuid: UUID): Link {
        return linkTo(methodOn(javaClass).getSectionById(songGuid)).withSelfRel()
    }

}
