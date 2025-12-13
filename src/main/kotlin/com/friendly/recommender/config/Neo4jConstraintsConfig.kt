package com.friendly.recommender.config

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.Neo4jClient

@Configuration
class Neo4jConstraintsConfig(private val neo4jClient: Neo4jClient) {

    @PostConstruct
    fun createConstraints() {
        neo4jClient.query("CREATE CONSTRAINT food_id_unique IF NOT EXISTS FOR (f:Food) REQUIRE f.name IS UNIQUE")
            .run()
        neo4jClient.query("CREATE CONSTRAINT hobby_id_unique IF NOT EXISTS FOR (h:Hobby) REQUIRE h.name IS UNIQUE")
            .run()
        neo4jClient.query("CREATE CONSTRAINT movie_id_unique IF NOT EXISTS FOR (m:Movie) REQUIRE m.title IS UNIQUE")
            .run()
        neo4jClient.query("CREATE CONSTRAINT trait_id_unique IF NOT EXISTS FOR (t:PersonalityTrait) REQUIRE t.name IS UNIQUE")
            .run()
        neo4jClient.query("CREATE CONSTRAINT user_id_unique IF NOT EXISTS FOR (u:User) REQUIRE u.email IS UNIQUE")
            .run()
    }
}