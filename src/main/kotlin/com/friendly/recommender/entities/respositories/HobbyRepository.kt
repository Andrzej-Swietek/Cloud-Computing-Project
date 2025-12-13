package com.friendly.recommender.entities.respositories

import com.friendly.recommender.entities.Hobby
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface HobbyRepository : Neo4jRepository<Hobby, String> {
    fun findByName(name: String): List<Hobby>
    fun findByDescriptionContains(keyword: String): List<Hobby>
}
