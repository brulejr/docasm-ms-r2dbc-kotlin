package io.jrb.labs.docasm.model

import io.jrb.labs.common.model.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table(value = "t_author")
data class Author(

    @Id
    @Column(value = "au_id")
    override val id: Long? = null,

    @Column(value = "au_guid")
    override val guid: UUID? = null,
    
    @Column(value = "au_name")
    val name: String,

    @Column(value = "au_created_by")
    override val createdBy: String?,

    @Column(value = "au_created_on")
    override val createdOn: Instant?,

    @Column(value = "au_modified_by")
    override val modifiedBy: String?,

    @Column(value = "au_modified_on")
    override val modifiedOn: Instant?

) : Entity {

    data class Builder(
        var id: Long? = null,
        var guid: UUID? = null,
        var name: String? = null,
        var createdBy: String? = null,
        var createdOn: Instant? = null,
        var modifiedBy: String? = null,
        var modifiedOn: Instant? = null
    ) {
        constructor(author: Author) : this() {
            this.id = author.id
            this.guid = author.guid
            this.name = author.name
            this.createdOn = author.createdOn
            this.createdBy = author.createdBy
            this.modifiedOn = author.modifiedOn
            this.modifiedBy = author.modifiedBy
        }

        fun id(id: Long) = apply { this.id = id }
        fun guid(guid: UUID) = apply { this.guid = guid }
        fun name(name: String) = apply { this.name = name }
        fun createdBy(createdBy: String) = apply { this.createdBy = createdBy }
        fun createdOn(createdOn: Instant) = apply { this.createdOn = createdOn }
        fun modifiedBy(modifiedBy: String) = apply { this.modifiedBy = modifiedBy }
        fun modifiedOn(modifiedOn: Instant) = apply { this.modifiedOn = modifiedOn }

        fun build() = Author(
            id = this.id,
            guid = this.guid,
            name = this.name!!,
            createdOn = this.createdOn,
            createdBy = this.createdBy,
            modifiedOn = this.modifiedOn,
            modifiedBy = this.modifiedBy
        )
    }

}
