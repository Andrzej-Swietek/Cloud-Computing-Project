package com.friendly.recommender.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class PersonalityTrait(
    @Id
    var name: String,
    var type : PersonalityTraitType,
)

enum class PersonalityTraitType { POSITIVE, NEUTRAL, NEGATIVE }
