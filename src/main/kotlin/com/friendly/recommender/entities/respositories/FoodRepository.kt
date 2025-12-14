package com.friendly.recommender.entities.respositories

import com.friendly.recommender.entities.Food
import com.friendly.recommender.entities.FoodId
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface FoodRepository : Neo4jRepository<Food, FoodId> {
    fun findByCuisine(cuisine: String): List<Food>
    fun findByIsVegetarian(isVegetarian: Boolean): List<Food>
}
