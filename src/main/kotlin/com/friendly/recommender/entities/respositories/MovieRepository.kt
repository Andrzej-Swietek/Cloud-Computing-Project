package com.friendly.recommender.entities.respositories

import com.friendly.recommender.entities.Movie
import com.friendly.recommender.entities.MovieGenre
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : Neo4jRepository<Movie, String> {
    fun findByGenre(genre: MovieGenre): List<Movie>
    fun findByDirector(director: String): List<Movie>
}
