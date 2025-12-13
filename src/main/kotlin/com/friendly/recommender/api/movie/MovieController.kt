package com.friendly.recommender.api.movie

import com.friendly.recommender.entities.Movie
import com.friendly.recommender.entities.MovieGenre
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
@RequestMapping("/api/movies")
class MovieController(private val service: MovieService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = ResponseEntity.ok(service.get(id))

    @GetMapping("/genres")
    fun getGenres() =
        ResponseEntity.ok(
            MovieGenre.entries
                .map { it.name }
                .toCollection(ArrayList())
        )

    @PostMapping
    fun create(@RequestBody movie: Movie) =
        ResponseEntity.ok(service.create(movie))

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody movie: Movie) =
        ResponseEntity.ok(service.update(id, movie))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) =
        ResponseEntity.ok(service.delete(id))

    @GetMapping("/genre/{genre}")
    fun getByGenre(@PathVariable genre: MovieGenre) =
        ResponseEntity.ok(service.getByGenre(genre))

    @GetMapping("/director/{director}")
    fun getByDirector(@PathVariable director: String) =
        ResponseEntity.ok(service.getByDirector(director))
}