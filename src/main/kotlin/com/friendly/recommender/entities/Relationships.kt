package com.friendly.recommender.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode


@RelationshipProperties
data class MovieRelationship(
    @Id @GeneratedValue var id: Long? = null,
    @TargetNode val movie: Movie,
    var score: Int
)

@RelationshipProperties
data class FoodRelationship(
    @Id @GeneratedValue var id: Long? = null,
    @TargetNode val food: Food,
    var score: Int
)