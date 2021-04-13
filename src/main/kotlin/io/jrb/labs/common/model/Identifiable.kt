package io.jrb.labs.common.model

import org.springframework.data.annotation.Id

interface Identifiable {

    @get:Id
    val id: Long?

}
