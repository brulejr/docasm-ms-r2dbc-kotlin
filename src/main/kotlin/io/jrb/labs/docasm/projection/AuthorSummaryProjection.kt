package io.jrb.labs.docasm.projection

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface AuthorSummaryProjection {
    val guid: UUID?
    val name: String
}
