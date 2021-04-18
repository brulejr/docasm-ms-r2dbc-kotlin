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

import io.jrb.labs.common.contract.Entity
import io.jrb.labs.common.contract.EntityBuilder
import io.jrb.labs.docasm.constants.DocumentType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(value = "t_document")
data class Document(

    @Id
    @Column(value = "do_id")
    override val id: Long? = null,

    @Column(value = "do_guid")
    override val guid: UUID,

    @Column(value = "do_type")
    val type: DocumentType,

    @Column(value = "do_name")
    val name: String,

    @Column(value = "do_created_by")
    override val createdBy: String?,

    @Column(value = "do_created_on")
    override val createdOn: Instant?,

    @Column(value = "do_modified_by")
    override val modifiedBy: String?,

    @Column(value = "do_modified_on")
    override val modifiedOn: Instant?

) : Entity {

    data class Builder(
        private var type: DocumentType? = null,
        private var name: String? = null
    ) : EntityBuilder<Document>() {
        constructor(document: Document) : this() {
            this.type = document.type
            this.name = document.name
        }

        fun type(type: DocumentType?) = apply { this.type = type }
        fun name(name: String?) = apply { this.name = name }

        override fun build() = Document(
            id = this.id,
            guid = this.guid!!,
            type = this.type!!,
            name = this.name!!,
            createdOn = this.createdOn,
            createdBy = this.createdBy,
            modifiedOn = this.modifiedOn,
            modifiedBy = this.modifiedBy
        )
    }

}
