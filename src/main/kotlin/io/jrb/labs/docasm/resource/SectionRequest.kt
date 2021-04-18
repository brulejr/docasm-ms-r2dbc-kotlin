package io.jrb.labs.docasm.resource

import io.jrb.labs.docasm.constants.SectionType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class SectionRequest(

    @field:NotBlank(message = "Name is mandatory")
    @field:Size(min = 8, max = 64, message = "Name must be between 8 and 64 characters")
    val name: String,

    @field:NotNull(message = "Type is mandatory")
    val type: SectionType,

    val tags: List<String> = listOf()

)
