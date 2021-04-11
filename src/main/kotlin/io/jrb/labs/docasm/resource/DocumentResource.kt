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
package io.jrb.labs.docasm.resource

import com.fasterxml.jackson.annotation.JsonInclude
import io.jrb.labs.common.resource.Resource
import io.jrb.labs.common.resource.ResourceBuilder
import io.jrb.labs.docasm.model.Document
import io.jrb.labs.docasm.constants.DocumentType
import java.time.Instant
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class DocumentResource(
    override val guid: UUID? = null,
    val type: DocumentType,
    val name: String,
    val tags: List<String> = listOf(),
    override val createdOn: Instant?,
    override val createdBy: String?,
    override val modifiedOn: Instant?,
    override val modifiedBy: String?
) : Resource {

    data class Builder(
        private var type: DocumentType? = null,
        private var name: String? = null,
        private var tags: MutableList<String> = mutableListOf()
    ) : ResourceBuilder<DocumentResource, Document>() {

        constructor(document: Document): this() {
            this.guid = document.guid
            this.type = document.type
            this.name = document.name
            this.createdOn = document.createdOn
            this.createdBy = document.createdBy
            this.modifiedOn = document.modifiedOn
            this.modifiedBy = document.modifiedBy
        }

        fun type(type: DocumentType?) = apply { this.type = type }
        fun name(name: String?) = apply { this.name = name }

        fun tag(tag: String) = apply { this.tags.add(tag) }
        fun tags(tags: List<String>) = apply { this.tags = tags.toMutableList() }

        override fun build() = DocumentResource(
            guid = this.guid,
            type = this.type!!,
            name = this.name!!,
            tags = this.tags,
            createdOn = this.createdOn,
            createdBy = this.createdBy,
            modifiedOn = this.modifiedOn,
            modifiedBy = this.modifiedBy
        )
    }

}
