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
package io.jrb.labs.docasm.service

import io.jrb.labs.docasm.model.EntityType
import io.jrb.labs.docasm.model.LookupValueType
import io.jrb.labs.docasm.model.Section
import io.jrb.labs.docasm.repository.SectionRepository
import io.jrb.labs.docasm.resource.SectionRequest
import io.jrb.labs.docasm.resource.SectionResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class SectionService(
    val sectionRepository: SectionRepository,
    val lookupValueService: LookupValueService
) {

    @Transactional
    fun createSection(sectionRequest: SectionRequest): Mono<SectionResource> {
        return Mono.just(
            Section.Builder()
                .type(sectionRequest.type)
                .name(sectionRequest.name)
        )
            .flatMap { createEntity( it, Section::class.java, sectionRepository) }
            .flatMap { section ->
                val authorId = section.id!!
                Mono.zip(
                    Mono.just(section),
                    lookupValueService.createLookupValues(EntityType.SECTION.name, authorId, LookupValueType.TAG.name, sectionRequest.tags)
                )
            }
            .map { tuple -> SectionResource(section = tuple.t1, tags = tuple.t2) }
    }

    @Transactional
    fun deleteSection(guid: UUID): Mono<Void> {
        return findEntityByGuid(guid, Section::class.java, sectionRepository)
            .flatMap<Void?> { section -> lookupValueService.deleteLookupValues(EntityType.SECTION.name, section.id!!) }
            .then(deleteEntity(guid, Section::class.java, sectionRepository))
    }

    @Transactional
    fun findSectionByGuid(guid: UUID): Mono<SectionResource> {
        return findEntityByGuid(guid, Section::class.java, sectionRepository)
            .zipWhen { section -> lookupValueService.findLookupValueList(EntityType.SECTION.name, section.id!!) }
            .map { tuple -> SectionResource(section = tuple.t1, tags = tuple.t2) }
    }

    @Transactional
    fun listAllSections(): Flux<SectionResource> {
        return listAllEntities(Section::class.java, sectionRepository)
            .map { SectionResource(section = it) }
    }

}
