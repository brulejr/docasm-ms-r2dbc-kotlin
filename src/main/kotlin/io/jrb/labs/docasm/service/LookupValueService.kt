package io.jrb.labs.docasm.service

import io.jrb.labs.docasm.model.LookupValue
import io.jrb.labs.docasm.repository.LookupValueRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class LookupValueService(val lookupValueRepository: LookupValueRepository) {

    fun createLookupValues(entityType: String, entityId: Long, valueType: String, values: List<String>): Mono<List<String>> {
        return Flux.fromIterable(values)
            .map { value -> LookupValue(null, entityType, entityId, valueType, value) }
            .flatMap { lookupValueRepository.save(it) }
            .map(LookupValue::value)
            .collectList()
    }

    fun deleteLookupValues(entityType: String, entityId: Long): Mono<Void> {
        return lookupValueRepository.deleteByEntityTypeAndEntityId(entityType, entityId)
    }

    fun findLookupValueList(entityType: String, entityId: Long): Mono<List<String>> {
        return lookupValueRepository.findByEntityTypeAndEntityId(entityType, entityId)
            .map { it.value }
            .collectList()
    }

}
