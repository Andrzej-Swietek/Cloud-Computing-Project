package com.friendly.recommender.api.food

import com.friendly.recommender.api.CrudService
import com.friendly.recommender.entities.Food
import com.friendly.recommender.entities.respositories.FoodRepository
import org.springframework.stereotype.Service

interface FoodService : CrudService<Food, String> {
    fun getByCuisine(cuisine: String): List<Food>
    fun getVegetarian(): List<Food>
}

@Service
class FoodServiceImpl(private val repository: FoodRepository) : FoodService {

    override fun getAll(): List<Food> = repository.findAll()
    override fun get(id: String): Food = repository.findById(id).orElseThrow { RuntimeException("Food not found") }
    override fun create(entity: Food): Food = repository.save(entity)
    override fun update(id: String, entity: Food): Food {
        val existing = get(id)
        existing.name = entity.name
        existing.cuisine = entity.cuisine
        existing.isVegetarian = entity.isVegetarian
        return repository.save(existing)
    }

    override fun delete(id: String) = repository.deleteById(id)

    override fun getByCuisine(cuisine: String): List<Food> = repository.findByCuisine(cuisine)
    override fun getVegetarian(): List<Food> = repository.findByIsVegetarian(true)
}
