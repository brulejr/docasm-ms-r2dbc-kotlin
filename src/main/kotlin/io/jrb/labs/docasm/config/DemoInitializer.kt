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
import io.jrb.labs.docasm.resource.DocumentResource
import io.jrb.labs.docasm.service.DocumentService
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.Arrays

class DemoInitializer(val documentService: DocumentService) : ApplicationListener<ApplicationReadyEvent> {

    private val log = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        log.info("Creating Documents")
        log.info("------------------")
        Flux.fromIterable(Arrays.asList(
            DocumentResource.Builder().name("Document1").type(DocumentType.SONG_SET_LIST).tag("A").build()
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

}
