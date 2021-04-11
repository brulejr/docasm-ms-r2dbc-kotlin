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
package io.jrb.labs.common.resource

import io.jrb.labs.common.model.Entity
import java.time.Instant
import java.util.UUID

abstract class ResourceBuilder<RESOURCE: Resource, ENTITY: Entity>(
    protected var guid: UUID? = UUID.randomUUID(),
    protected var createdOn: Instant? = null,
    protected var createdBy: String? = null,
    protected var modifiedOn: Instant? = null,
    protected var modifiedBy: String? = null
) {

    constructor(entity: ENTITY): this()

    fun guid(guid: UUID?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.guid = guid }
    fun createdOn(createdOn: Instant?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.createdOn = createdOn }
    fun createdBy(createdBy: String?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.createdBy = createdBy }
    fun modifiedOn(modifiedOn: Instant?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.modifiedOn = modifiedOn }
    fun modifiedBy(modifiedBy: String?) : ResourceBuilder<RESOURCE, ENTITY> = apply { this.modifiedBy = modifiedBy }

    abstract fun build(): RESOURCE
}
