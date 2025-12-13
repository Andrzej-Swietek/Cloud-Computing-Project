package com.friendly.recommender.api.personality

import com.friendly.recommender.api.CrudService
import com.friendly.recommender.entities.PersonalityTrait
import com.friendly.recommender.entities.PersonalityTraitType
import com.friendly.recommender.entities.respositories.PersonalityTraitRepository
import org.springframework.stereotype.Service

interface PersonalityTraitService : CrudService<PersonalityTrait, String> {
    fun getByType(type: PersonalityTraitType): List<PersonalityTrait>
}

@Service
class PersonalityTraitServiceImpl(private val repository: PersonalityTraitRepository) : PersonalityTraitService {

    override fun getAll(): List<PersonalityTrait> = repository.findAll()
    override fun get(id: String): PersonalityTrait =
        repository.findById(id).orElseThrow { RuntimeException("Trait not found") }

    override fun create(entity: PersonalityTrait): PersonalityTrait = repository.save(entity)
    override fun update(id: String, entity: PersonalityTrait): PersonalityTrait {
        val existing = get(id)
        existing.name = entity.name
        existing.type = entity.type
        return repository.save(existing)
    }

    override fun delete(id: String) = repository.deleteById(id)

    override fun getByType(type: PersonalityTraitType): List<PersonalityTrait> = repository.findByType(type)
}