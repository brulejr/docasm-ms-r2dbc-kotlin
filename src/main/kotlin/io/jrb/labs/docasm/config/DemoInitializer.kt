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
package io.jrb.labs.docasm.config

import io.jrb.labs.docasm.constants.DocumentType
import io.jrb.labs.docasm.constants.SectionType
import io.jrb.labs.docasm.resource.DocumentRequest
import io.jrb.labs.docasm.resource.SectionRequest
import io.jrb.labs.docasm.service.DocumentService
import io.jrb.labs.docasm.service.SectionService
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.Arrays

class DemoInitializer(
    val documentService: DocumentService,
    val sectionService: SectionService
) : ApplicationListener<ApplicationReadyEvent> {

    private val log = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        createDocuments()
        createSections()
    }

    private fun createDocuments() {
        log.info("Creating Documents")
        log.info("------------------")
        Flux.fromIterable(Arrays.asList(
            DocumentRequest(name = "Document1", type = DocumentType.SONG_SET_LIST, tags = listOf("A"))
        ))
            .flatMap { documentService.createDocument(it) }
            .blockLast(Duration.ofSeconds(10))

        // fetch all customers
        log.info("Documents found with findAll()")
        log.info("------------------------------")
        documentService.listAllDocuments()
            .doOnNext { document -> log.info(document.toString()) }
            .blockLast(Duration.ofSeconds(10))
    }

    private fun createSections() {
        log.info("Creating Section")
        log.info("----------------")
        Flux.fromIterable(Arrays.asList(
            SectionRequest(name = "Secti    on1", type = SectionType.SONG, tags = listOf("A"))
        ))
            .flatMap { sectionService.createSection(it) }
            .blockLast(Duration.ofSeconds(10))

        // fetch all customers
        log.info("Sections found with findAll()")
        log.info("-----------------------------")
        sectionService.listAllSections()
            .doOnNext { section -> log.info(section.toString()) }
            .blockLast(Duration.ofSeconds(10))
    }

}
