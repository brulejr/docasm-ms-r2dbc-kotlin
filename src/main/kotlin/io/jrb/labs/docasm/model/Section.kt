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
package io.jrb.labs.docasm.model

import io.jrb.labs.common.model.Entity
import io.jrb.labs.common.model.EntityBuilder
import io.jrb.labs.docasm.constants.SectionType
import io.jrb.labs.docasm.resource.SectionResource
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(value = "t_section")
data class Section(

    @Column(value = "se_id")
    override val id: Long? = null,

    @Column(value = "se_guid")
    override val guid: UUID? = null,

    @Column(value = "se_type")
    val type: SectionType,

    @Column(value = "se_name")
    val name: String,

    @Column(value = "se_created_by")
    override val createdBy: String?,

    @Column(value = "se_created_on")
    override val createdOn: Instant?,

    @Column(value = "se_modified_by")
    override val modifiedBy: String?,

    @Column(value = "se_modified_on")
    override val modifiedOn: Instant?

) : Entity {

    data class Builder(
        private var type: SectionType? = null,
        private var name: String? = null
    ) : EntityBuilder<Section, SectionResource>() {
        constructor(sectionResource: SectionResource) : this() {
            this.guid = sectionResource.guid
            this.type = sectionResource.type
            this.name = sectionResource.name
            this.createdBy = sectionResource.createdBy
            this.createdOn = sectionResource.createdOn
            this.modifiedBy = sectionResource.modifiedBy
            this.modifiedOn = sectionResource.modifiedOn
        }

        constructor(section: Section) : this() {
            this.id = section.id
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

        override fun build() = Section(
            id = this.id,
            guid = this.guid,
            type = this.type!!,
            name = this.name!!,
            createdOn = this.createdOn,
            createdBy = this.createdBy,
            modifiedOn = this.modifiedOn,
            modifiedBy = this.modifiedBy
        )
    }

}
