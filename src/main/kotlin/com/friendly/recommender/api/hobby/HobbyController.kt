package com.friendly.recommender.api.hobby

import com.friendly.recommender.entities.Hobby
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hobbies")
class HobbyController(private val service: HobbyService) {

    @GetMapping
    fun getAll() = service.getAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = service.get(id)

    @PostMapping
    fun create(@RequestBody hobby: Hobby) = service.create(hobby)

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody hobby: Hobby) = service.update(id, hobby)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = service.delete(id)

    @GetMapping("/name/{name}")
    fun getByName(@PathVariable name: String) = service.getByName(name)

    @GetMapping("/search/{keyword}")
    fun getByDescription(@PathVariable keyword: String) = service.getByDescription(keyword)
}
