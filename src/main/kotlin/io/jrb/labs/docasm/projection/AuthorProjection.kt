package io.jrb.labs.docasm.projection

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
interface AuthorProjection {
    val guid: UUID?
    val name: String
    val createdBy: String?
    val createdOn: Instant?
    val modifiedBy: String?
    val modifiedOn: Instant?
}
