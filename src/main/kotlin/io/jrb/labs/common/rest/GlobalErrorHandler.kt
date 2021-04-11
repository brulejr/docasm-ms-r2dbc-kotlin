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
package io.jrb.labs.common.rest

import com.google.common.base.VerifyException
import io.jrb.labs.common.resource.ErrorResponseEntity
import io.jrb.labs.common.service.DuplicateResourceException
import io.jrb.labs.common.service.InvalidResourceException
import io.jrb.labs.common.service.PatchInvalidException
import io.jrb.labs.common.service.ResourceNotFoundException
import io.jrb.labs.common.service.ServiceException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalErrorHandler {

    @ExceptionHandler(DuplicateResourceException::class)
    fun forumException(exception: DuplicateResourceException) = ErrorResponseEntity.conflict(exception.message)

    @ExceptionHandler(InvalidResourceException::class)
    fun forumException(exception: InvalidResourceException) = ErrorResponseEntity.badRequest(exception.message)

    @ExceptionHandler(PatchInvalidException::class)
    fun forumException(exception: PatchInvalidException) = ErrorResponseEntity.badRequest(exception.message)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun forumException(exception: ResourceNotFoundException) = ErrorResponseEntity.notFound(exception.message)

    @ExceptionHandler(ServiceException::class)
    fun forumException(exception: ServiceException) = ErrorResponseEntity.serverError(exception.message)

    @ExceptionHandler(VerifyException::class)
    fun forumException(exception: VerifyException) = ErrorResponseEntity.badRequest(exception.message)

}
