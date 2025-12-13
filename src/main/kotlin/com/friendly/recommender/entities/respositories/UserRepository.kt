package com.friendly.recommender.entities.respositories

import com.friendly.recommender.entities.User
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, String> {
    fun findByEmail(email: String): User?

    @Query(
        "MATCH (u:User {id: \$userId}), (m:Movie {id: \$movieId})" +
                "MERGE (u)-[r:LIKES]->(m)" +
                "SET r.score = \$score" +
                "RETURN u"
    )
    fun addLikedMovie(
        @Param("userId") userId: String,
        @Param("movieId") movieId: Long,
        @Param("score") score: Int
    ): User

    @Query(
        "MATCH (u:User {id: \$userId})-[r:LIKES]->(m:Movie {id: \$movieId})" +
                "DELETE r"
    )
    fun removeLikedMovie(
        @Param("userId") userId: String,
        @Param("movieId") movieId: Long
    )

    @Query(
        "MATCH (u:User {id: \$userId}), (m:Movie {id: \$movieId})" +
                "MERGE (u)-[r:DISLIKES]->(m)" +
                "RETURN u"
    )
    fun addDislikedMovie(
        @Param("userId") userId: String,
        @Param("movieId") movieId: Long
    ): User

    @Query(
        "MATCH (u:User {id: \$userId})-[r:DISLIKES]->(m:Movie {id: \$movieId})" +
                "DELETE r"
    )
    fun removeDislikedMovie(
        @Param("userId") userId: String,
        @Param("movieId") movieId: Long
    )

    @Query(
        "MATCH (u:User {id: \$userId}), (f:Food {id: \$foodId})" +
                "MERGE (u)-[r:LIKES]->(f)" +
                "SET r.score = \$score" +
                "RETURN u"
    )
    fun addLikedFood(
        @Param("userId") userId: String,
        @Param("foodId") foodId: Long,
        @Param("score") score: Int
    ): User

    @Query(
        "MATCH (u:User {id: \$userId})-[r:LIKES]->(f:Food {id: \$foodId})" +
                "DELETE r"
    )
    fun removeLikedFood(
        @Param("userId") userId: String,
        @Param("foodId") foodId: Long
    )

    @Query(
        "MATCH (u:User {id: \$userId}), (f:Food {id: \$foodId})" +
                "MERGE (u)-[r:DISLIKES]->(f)" +
                "RETURN u"
    )
    fun addDislikedFood(
        @Param("userId") userId: String,
        @Param("foodId") foodId: Long
    ): User

    @Query(
        "MATCH (u:User {id: \$userId})-[r:DISLIKES]->(f:Food {id: \$foodId})" +
                "DELETE r"
    )
    fun removeDislikedFood(
        @Param("userId") userId: String,
        @Param("foodId") foodId: Long
    )

    @Query(

        "MATCH (u:User {id: \$userId}), (h:Hobby {id: \$hobbyId})" +
                "MERGE (u)-[:HAS_HOBBY]->(h)" +
                "RETURN u"

    )
    fun addHobby(
        @Param("userId") userId: String,
        @Param("hobbyId") hobbyId: Long
    ): User

    @Query(
        "MATCH (u:User {id: \$userId}), (t:PersonalityTrait {id: \$traitId})" +
                "MERGE (u)-[:HAS_TRAIT]->(t)" +
                "RETURN u"
    )
    fun addTrait(
        @Param("userId") userId: String,
        @Param("traitId") traitId: Long
    ): User

    @Query(
        "MATCH (u:User {id: \$userId})-[r:HAS_HOBBY]->(h:Hobby {id: \$hobbyId})" +
                "DELETE r"
    )
    fun removeHobby(
        @Param("userId") userId: String,
        @Param("hobbyId") hobbyId: Long
    )

    @Query(
        "MATCH (u:User {id: \$userId})-[r:HAS_TRAIT]->(t:PersonalityTrait {id: \$traitId})" +
                "DELETE r"
    )
    fun removeTrait(
        @Param("userId") userId: String,
        @Param("traitId") traitId: Long
    )

    @Query(
        "MATCH (u:User {id: \$userId})-[:LIKES|:HAS_HOBBY|:HAS_TRAIT]->(x)<-[:LIKES|:HAS_HOBBY|:HAS_TRAIT]-(other:User) " +
                "WHERE u <> other " +
                "RETURN other, count(x) AS similarity " +
                "ORDER BY similarity DESC " +
                "LIMIT 10"
    )
    fun findTop10SimilarUsers(
        @Param("userId") userId: String
    ): List<User>

    @Query(
        "MATCH (u:User {id: \$userId})-[r]->(x)<-[r2]-(other:User) " +
                "WHERE type(r) = \$relationshipType AND type(r2) = \$relationshipType AND u <> other " +
                "RETURN other, count(x) AS similarity " +
                "ORDER BY similarity DESC " +
                "LIMIT 10"
    )
    fun findTop10SimilarUsersByRelationship(
        @Param("userId") userId: String,
        @Param("relationshipType") relationshipType: String
    ): List<User>

    @Query(
        "MATCH (u:User {id: \$userId}), (other:User {id: \$otherId}) " +
                "MERGE (u)-[:LIKES_USER]->(other) " +
                "RETURN u"
    )
    fun addLikedUser(
        @Param("userId") userId: String,
        @Param("otherId") otherId: Long
    ): User

    @Query(
        "MATCH (u:User {id: \$userId})-[r:LIKES_USER]->(other:User {id: \$otherId}) " +
                "DELETE r"
    )
    fun removeLikedUser(
        @Param("userId") userId: String,
        @Param("otherId") otherId: Long
    ): User

    @Query(
        "MATCH (u:User {id: \$userId}), (other:User {id: \$otherId}) " +
                "MERGE (u)-[:DISLIKES_USER]->(other) " +
                "RETURN u"
    )
    fun addDislikedUser(
        @Param("userId") userId: String,
        @Param("otherId") otherId: Long
    ): User


    @Query(
        "MATCH (u:User {id: \$userId})-[:LIKES_USER]->(other:User)" +
                "RETURN other" +
                "LIMIT 10"
    )
    fun findTopLikedUsers(
        @Param("userId") userId: String
    ): List<User>
}
