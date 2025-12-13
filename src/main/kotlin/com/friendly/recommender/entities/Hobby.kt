package com.friendly.recommender.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Hobby(
    @Id
    var name: String,
    var description: String,
)