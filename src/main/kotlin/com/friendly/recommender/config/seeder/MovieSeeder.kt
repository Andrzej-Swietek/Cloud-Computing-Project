package com.friendly.recommender.config.seeder


import com.friendly.recommender.api.movie.MovieService
import com.friendly.recommender.entities.Movie
import com.friendly.recommender.entities.MovieGenre
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class MovieSeeder(private val movieService: MovieService) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (movieService.getAll().isEmpty()) {
            val movies = listOf(
                Movie(title = "Spirited Away", genre = MovieGenre.ANIME, director = "Hayao Miyazaki", releaseYear = 2001),
                Movie(title = "Inception", genre = MovieGenre.SCI_FI, director = "Christopher Nolan", releaseYear = 2010),
                Movie(title = "The Godfather", genre = MovieGenre.DRAMA, director = "Francis Ford Coppola", releaseYear = 1972),
                Movie(title = "Avengers: Endgame", genre = MovieGenre.ACTION, director = "Anthony Russo", releaseYear = 2019),
                Movie(title = "Parasite", genre = MovieGenre.THRILLER, director = "Bong Joon-ho", releaseYear = 2019),
                Movie(title = "Toy Story", genre = MovieGenre.ANIMATION, director = "John Lasseter", releaseYear = 1995),
                Movie(title = "La La Land", genre = MovieGenre.ROMANCE, director = "Damien Chazelle", releaseYear = 2016),
                Movie(title = "The Shining", genre = MovieGenre.HORROR, director = "Stanley Kubrick", releaseYear = 1980),
                Movie(title = "Interstellar", genre = MovieGenre.SCI_FI, director = "Christopher Nolan", releaseYear = 2014),
                Movie(title = "Coco", genre = MovieGenre.ANIMATION, director = "Lee Unkrich", releaseYear = 2017)
            )
            movies.forEach { movieService.create(it) }
        }
    }
}