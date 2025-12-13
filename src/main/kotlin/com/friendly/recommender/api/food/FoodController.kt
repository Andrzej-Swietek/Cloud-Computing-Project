package com.friendly.recommender.api.food

import com.friendly.recommender.entities.Food
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/foods")
class FoodController(private val service: FoodService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) =
        ResponseEntity.ok(service.get(id))

    @PostMapping
    fun create(@RequestBody food: Food) =
        ResponseEntity.ok(service.create(food))

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody food: Food) =
        ResponseEntity.ok(service.update(id, food))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) =
        ResponseEntity.ok(service.delete(id))

    @GetMapping("/cuisine/{cuisine}")
    fun getByCuisine(@PathVariable cuisine: String) =
        ResponseEntity.ok(service.getByCuisine(cuisine))

    @GetMapping("/vegetarian")
    fun getVegetarian() =
        ResponseEntity.ok(service.getVegetarian())
}
