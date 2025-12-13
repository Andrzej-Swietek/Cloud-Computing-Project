package com.friendly.recommender.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Movie(
    @Id
    var title: String,
    var genre: MovieGenre,
    var director: String,
    var releaseYear: Int
)

enum class MovieGenre { ANIME, ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCI_FI, FANTASY, THRILLER, ANIMATION, DOCUMENTARY }
