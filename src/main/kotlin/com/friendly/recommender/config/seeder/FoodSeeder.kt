package com.friendly.recommender.config.seeder

import com.friendly.recommender.api.food.FoodService
import com.friendly.recommender.entities.Food
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class FoodSeeder(private val foodService: FoodService) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (foodService.getAll().isEmpty()) {
            val foods = listOf(
                Food(name = "Pizza", cuisine = "Italian", isVegetarian = false),
                Food(name = "Pasta", cuisine = "Italian", isVegetarian = true),
                Food(name = "Sushi", cuisine = "Japanese", isVegetarian = false),
                Food(name = "Tofu Stir Fry", cuisine = "Chinese", isVegetarian = true),
                Food(name = "Burger", cuisine = "American", isVegetarian = false),
                Food(name = "Salad", cuisine = "American", isVegetarian = true),
                Food(name = "Curry", cuisine = "Indian", isVegetarian = true),
                Food(name = "Tacos", cuisine = "Mexican", isVegetarian = false),
                Food(name = "Paella", cuisine = "Spanish", isVegetarian = false),
                Food(name = "Falafel", cuisine = "Middle Eastern", isVegetarian = true)
            )
            foods.forEach { foodService.create(it) }
        }
    }
}