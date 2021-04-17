package io.jrb.labs.docasm.resource

import com.fasterxml.jackson.annotation.JsonInclude
import io.jrb.labs.common.contract.FriendlyIdentifiable
import io.jrb.labs.common.contract.Nameable
import io.jrb.labs.common.contract.Trackable
import io.jrb.labs.docasm.model.Author
import java.time.Instant
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class AuthorResource(

    override val guid: UUID,
    override val name: String,
    override val createdBy: String?,
    override val createdOn: Instant?,
    override val modifiedBy: String?,
    override val modifiedOn: Instant?

) : FriendlyIdentifiable, Nameable, Trackable {

    constructor(author: Author) : this(
        guid = author.guid!!,
        name = author.name,
        createdBy = author.createdBy,
        createdOn = author.createdOn,
        modifiedBy = author.modifiedBy,
        modifiedOn = author.modifiedOn
    )

}

