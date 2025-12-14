package com.friendly.recommender.entities
import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.annotation.Transient

@Node("User")
data class User(
    @Id
    var email: String,
    var password: String,

    var firstname: String,
    var lastname: String,

    @Relationship(type = "HAS_TRAIT", direction = Relationship.Direction.OUTGOING)
    var traits: MutableList<PersonalityTrait> = mutableListOf(),

    @Relationship(type = "HAS_HOBBY", direction = Relationship.Direction.OUTGOING)
    var hobbies: MutableList<Hobby> = mutableListOf(),

    @Relationship(type = "LIKES_MOVIE", direction = Relationship.Direction.OUTGOING)
    var likedMovies: MutableList<MovieRelationship> = mutableListOf(),

    @Relationship(type = "DISLIKES_MOVIE", direction = Relationship.Direction.OUTGOING)
    var dislikedMovies: List<MovieRelationship> = mutableListOf(),

    @Relationship(type = "LIKES_FOOD", direction = Relationship.Direction.OUTGOING)
    var foods: MutableList<FoodRelationship> = mutableListOf(),

    @Relationship(type = "DISLIKES_FOOD", direction = Relationship.Direction.OUTGOING)
    var dislikeFood: List<FoodRelationship> = mutableListOf(),

    @Transient
    @Relationship(type = "LIKES_USER", direction = Relationship.Direction.OUTGOING)
    var likedUsers: MutableList<User> = mutableListOf(),

    @Transient
    @Relationship(type = "DISLIKES_USER", direction = Relationship.Direction.OUTGOING)
    var dislikedUsers: MutableList<User> = mutableListOf()
)
