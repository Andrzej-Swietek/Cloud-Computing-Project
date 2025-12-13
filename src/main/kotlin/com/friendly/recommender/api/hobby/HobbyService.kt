package com.friendly.recommender.api.hobby

import com.friendly.recommender.api.CrudService
import com.friendly.recommender.entities.Hobby
import com.friendly.recommender.entities.respositories.HobbyRepository
import org.springframework.stereotype.Service

interface HobbyService : CrudService<Hobby, String> {
    fun getByName(name: String): List<Hobby>
    fun getByDescription(keyword: String): List<Hobby>
}

@Service
class HobbyServiceImpl(private val repository: HobbyRepository) : HobbyService {

    override fun getAll(): List<Hobby> = repository.findAll()
    override fun get(id: String): Hobby = repository.findById(id).orElseThrow { RuntimeException("Hobby not found") }
    override fun create(entity: Hobby): Hobby = repository.save(entity)
    override fun update(id: String, entity: Hobby): Hobby {
        val existing = get(id)
        existing.name = entity.name
        existing.description = entity.description
        return repository.save(existing)
    }

    override fun delete(id: String) = repository.deleteById(id)

    override fun getByName(name: String): List<Hobby> = repository.findByName(name)
    override fun getByDescription(keyword: String): List<Hobby> = repository.findByDescriptionContains(keyword)
}