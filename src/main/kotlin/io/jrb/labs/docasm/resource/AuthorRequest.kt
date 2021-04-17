package io.jrb.labs.docasm.resource

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AuthorRequest(

    @field:NotBlank(message = "Name is mandatory")
    @field:Size(min = 8, max = 64, message = "Name must be between 8 and 64 characters")
    val name: String,

    val tags: List<String> = listOf()

)
