package com.friendly.recommender.api.movie

import com.friendly.recommender.api.CrudService
import com.friendly.recommender.entities.Movie
import com.friendly.recommender.entities.MovieGenre
import com.friendly.recommender.entities.respositories.MovieRepository
import org.springframework.stereotype.Service

interface MovieService : CrudService<Movie, String> {
    fun getByGenre(genre: MovieGenre): List<Movie>
    fun getByDirector(director: String): List<Movie>
}

@Service
final class MovieServiceImpl(
    private val repository: MovieRepository
) : MovieService {

    override fun getAll(): List<Movie> = repository.findAll()
    override fun get(id: String): Movie = repository.findById(id).orElseThrow { RuntimeException("Movie not found") }
    override fun create(entity: Movie): Movie = repository.save(entity)
    override fun update(id: String, entity: Movie): Movie {
        val existing = get(id)
        existing.title = entity.title
        existing.genre = entity.genre
        existing.director = entity.director
        existing.releaseYear = entity.releaseYear
        return repository.save(existing)
    }

    override fun delete(id: String) = repository.deleteById(id)

    override fun getByGenre(genre: MovieGenre): List<Movie> = repository.findByGenre(genre)
    override fun getByDirector(director: String): List<Movie> = repository.findByDirector(director)
}