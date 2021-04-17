package io.jrb.labs.docasm.rest

import io.jrb.labs.docasm.resource.AuthorRequest
import io.jrb.labs.docasm.resource.AuthorResource
import io.jrb.labs.docasm.service.AuthorService
import mu.KotlinLogging
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/api/authors")
class AuthorController(
    val authorService: AuthorService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody author: AuthorRequest): Mono<EntityModel<AuthorResource>> {
        return authorService.create(author).map {
            EntityModel.of(it)
                .add(selfLink(it.guid))
                .add(collectionLink())
        }
    }

    @GetMapping("/{authorGuid}")
    fun getById(@PathVariable authorGuid: UUID): Mono<EntityModel<AuthorResource>> {
        return authorService.findByGuid(authorGuid).map {
            EntityModel.of(it)
                .add(selfLink(authorGuid))
                .add(collectionLink())
        }
    }

    @GetMapping
    fun list(): Flux<EntityModel<AuthorResource>> {
        return authorService.listAll().map {
            EntityModel.of(it)
                .add(selfLink(it.guid))
        }
    }

    private fun collectionLink(): Link {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(javaClass).list()).withRel("collection")
    }

    private fun selfLink(guid: UUID): Link {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(javaClass).getById(guid)).withSelfRel()
    }

}
