package io.jrb.labs.common.contract

import java.time.Instant

interface Trackable {
    val createdOn: Instant?
    val createdBy: String?
    val modifiedOn: Instant?
    val modifiedBy: String?
}
