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

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import io.jrb.labs.common.service.CrudService
import io.jrb.labs.docasm.model.Section
import io.jrb.labs.docasm.model.EntityType
import io.jrb.labs.docasm.model.LookupValue
import io.jrb.labs.docasm.model.LookupValueType
import io.jrb.labs.docasm.repository.SectionRepository
import io.jrb.labs.docasm.repository.LookupValueRepository
import io.jrb.labs.docasm.resource.SectionResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class SectionService(
    val sectionRepository: SectionRepository,
    val lookupValueRepository: LookupValueRepository,
    val objectMapper: ObjectMapper
) : CrudService<Section, SectionResource>(
    sectionRepository,
    "Section",
    Section::class.java,
    Section.Builder::class.java,
    SectionResource::class.java,
    SectionResource.Builder::class.java,
    objectMapper
) {

    @Transactional
    fun createSection(sectionResource: SectionResource): Mono<SectionResource> {
        return super.createEntity(sectionResource)
            .flatMap { section ->
                val sectionId = section.id!!
                Mono.zip(
                    Mono.just(section),
                    createLookupValues(EntityType.SECTION, sectionId, LookupValueType.TAG, sectionResource.tags)
                )}
            .map { tuple ->
                SectionResource.Builder(tuple.t1)
                    .tags(tuple.t2)
                    .build()
            }
    }


    @Transactional
    fun deleteSection(guid: UUID): Mono<Void> {
        return super.findEntityByGuid(guid)
            .flatMap { section -> lookupValueRepository.deleteByEntityTypeAndEntityId(EntityType.SECTION, section.id!!) }
            .then(super.delete(guid))
    }

    @Transactional
    fun findSectionByGuid(guid: UUID): Mono<SectionResource> {
        return super.findEntityByGuid(guid)
            .zipWhen { section -> findLookupValueList(EntityType.SECTION, section.id!!) }
            .map { tuple ->
                val builder = SectionResource.Builder(tuple.t1)
                tuple.t2.forEach { lookupValue ->
                    val value = lookupValue.value
                    when (lookupValue.valueType) {
                        LookupValueType.TAG -> builder.tag(value)
                        else -> { }
                    }
                }
                builder.build()
            }
    }

    @Transactional
    fun listAllSections(): Flux<SectionResource> {
        return super.listAll()
    }

    @Transactional
    fun updateSection(guid: UUID, patch: JsonPatch): Mono<SectionResource> {
        return super.update(guid, patch)
    }

    private fun createLookupValues(entityType: EntityType, entityId: Long, valueType: LookupValueType, values: List<String>): Mono<List<String>> {
        return Flux.fromIterable(values)
            .map { value -> LookupValue(null, entityType, entityId, valueType, value) }
            .flatMap { lookupValueRepository.save(it) }
            .map(LookupValue::value)
            .collectList()
    }

    private fun findLookupValueList(entityType: EntityType, entityId: Long): Mono<List<LookupValue>> {
        return lookupValueRepository.findByEntityTypeAndEntityId(entityType, entityId)
            .collectList()
    }

}
