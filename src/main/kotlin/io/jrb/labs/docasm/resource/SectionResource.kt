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
import io.jrb.labs.docasm.constants.SectionType
import io.jrb.labs.docasm.model.Section
import java.time.Instant
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class SectionResource(
    override val guid: UUID? = null,
    val type: SectionType,
    val name: String,
    val tags: List<String> = listOf(),
    override val createdOn: Instant?,
    override val createdBy: String?,
    override val modifiedOn: Instant?,
    override val modifiedBy: String?
) : Resource {

    data class Builder(
        private var type: SectionType? = null,
        private var name: String? = null,
        private var tags: MutableList<String> = mutableListOf()
    ) : ResourceBuilder<SectionResource, Section>() {

        constructor(section: Section): this() {
            this.guid = section.guid
            this.type = section.type
            this.name = section.name
            this.createdOn = section.createdOn
            this.createdBy = section.createdBy
            this.modifiedOn = section.modifiedOn
            this.modifiedBy = section.modifiedBy
        }

        fun type(type: SectionType?) = apply { this.type = type }
        fun name(name: String?) = apply { this.name = name }

        fun tag(tag: String) = apply { this.tags.add(tag) }
        fun tags(tags: List<String>) = apply { this.tags = tags.toMutableList() }

        override fun build() = SectionResource(
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
