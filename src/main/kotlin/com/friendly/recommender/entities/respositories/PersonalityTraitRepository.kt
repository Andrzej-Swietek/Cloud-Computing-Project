package com.friendly.recommender.entities.respositories

import com.friendly.recommender.entities.PersonalityTrait
import com.friendly.recommender.entities.PersonalityTraitId
import com.friendly.recommender.entities.PersonalityTraitType
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonalityTraitRepository : Neo4jRepository<PersonalityTrait, PersonalityTraitId> {
    fun findByType(type: PersonalityTraitType): List<PersonalityTrait>
}
