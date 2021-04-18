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
import io.jrb.labs.common.contract.Tagable
import io.jrb.labs.common.resource.Resource
import io.jrb.labs.docasm.constants.SectionType
import io.jrb.labs.docasm.model.Section
import java.time.Instant
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class SectionResource(
    override val guid: UUID,
    val type: SectionType,
    override val name: String,
    override val tags: List<String>,
    override val createdOn: Instant?,
    override val createdBy: String?,
    override val modifiedOn: Instant?,
    override val modifiedBy: String?
) : Resource, Tagable {

    constructor(section: Section, tags: List<String>? = listOf()) : this(
        guid = section.guid,
        type = section.type,
        name = section.name,
        tags = tags!!,
        createdBy = section.createdBy,
        createdOn = section.createdOn,
        modifiedBy = section.modifiedBy,
        modifiedOn = section.modifiedOn
    )

}
