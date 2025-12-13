package com.friendly.recommender.api.personality

import com.friendly.recommender.entities.PersonalityTrait
import com.friendly.recommender.entities.PersonalityTraitType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/traits")
class PersonalityTraitController(private val service: PersonalityTraitService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = ResponseEntity.ok(service.get(id))

    @PostMapping
    fun create(@RequestBody trait: PersonalityTrait) = service.create(trait)

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody trait: PersonalityTrait) =
        ResponseEntity.ok(service.update(id, trait))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = ResponseEntity.ok(service.delete(id))

    @GetMapping("/type/{type}")
    fun getByType(@PathVariable type: PersonalityTraitType) = ResponseEntity.ok(service.getByType(type))
}