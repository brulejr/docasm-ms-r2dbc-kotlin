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
package io.jrb.labs.common.contract

import java.time.Instant
import java.util.UUID

abstract class EntityBuilder<E: Entity>(
    protected var id: Long? = null,
    protected var guid: UUID? = UUID.randomUUID(),
    protected var createdOn: Instant? = null,
    protected var createdBy: String? = null,
    protected var modifiedOn: Instant? = null,
    protected var modifiedBy: String? = null
) {
    constructor(entity: E): this() {
        this.id = entity.id
        this.guid = entity.guid
        this.createdOn = entity.createdOn
        this.createdBy = entity.createdBy
        this.modifiedOn = entity.modifiedOn
        this.modifiedBy = entity.modifiedBy
    }

    fun id(id: Long?) : EntityBuilder<E> = apply { this.id = id }
    fun guid(guid: UUID?) : EntityBuilder<E> = apply { this.guid = guid }
    fun createdOn(createdOn: Instant?) : EntityBuilder<E> = apply { this.createdOn = createdOn }
    fun createdBy(createdBy: String?) : EntityBuilder<E> = apply { this.createdBy = createdBy }
    fun modifiedOn(modifiedOn: Instant?) : EntityBuilder<E> = apply { this.modifiedOn = modifiedOn }
    fun modifiedBy(modifiedBy: String?) : EntityBuilder<E> = apply { this.modifiedBy = modifiedBy }

    abstract fun build(): E
}
