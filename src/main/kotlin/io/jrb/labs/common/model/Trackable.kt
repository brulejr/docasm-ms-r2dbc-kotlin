package io.jrb.labs.common.model

import java.time.Instant
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate

interface Trackable {

    @get:CreatedDate
    val createdOn: Instant?

    @get:CreatedBy
    val createdBy: String?

    @get:LastModifiedDate
    val modifiedOn: Instant?

    @get:LastModifiedBy
    val modifiedBy: String?

}
