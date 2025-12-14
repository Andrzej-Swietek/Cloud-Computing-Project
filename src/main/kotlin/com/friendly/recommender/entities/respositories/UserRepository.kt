package com.friendly.recommender.entities.respositories

import com.friendly.recommender.entities.Food
import com.friendly.recommender.entities.FoodId
import com.friendly.recommender.entities.FoodRelationship
import com.friendly.recommender.entities.Hobby
import com.friendly.recommender.entities.HobbyId
import com.friendly.recommender.entities.Movie
import com.friendly.recommender.entities.MovieGenre
import com.friendly.recommender.entities.MovieId
import com.friendly.recommender.entities.MovieRelationship
import com.friendly.recommender.entities.PersonalityTrait
import com.friendly.recommender.entities.PersonalityTraitId
import com.friendly.recommender.entities.PersonalityTraitType
import com.friendly.recommender.entities.User
import com.friendly.recommender.entities.UserId
import org.neo4j.driver.Driver
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.neo4j.driver.types.Node


data class UserWithRelationsDTO(
    val user: User,
    val likedMovies: List<MovieRelationship>,
    val dislikedMovies: List<MovieRelationship>,
    val foods: List<FoodRelationship>,
    val dislikeFood: List<FoodRelationship>,
    val hobbies: List<Hobby>,
    val traits: List<PersonalityTrait>,
    val likedUsers: List<User>,
    val dislikedUsers: List<User>
) {
    fun toUser(): User = user.copy(
        likedMovies = likedMovies.toMutableList(),
        dislikedMovies = dislikedMovies.toMutableList(),
        foods = foods.toMutableList(),
        dislikeFood = dislikeFood.toMutableList(),
        hobbies = hobbies.toMutableList(),
        traits = traits.toMutableList(),
        likedUsers = likedUsers.toMutableList(),
        dislikedUsers = dislikedUsers.toMutableList()
    )
}

@Repository
interface UserRepository : Neo4jRepository<User, String> {
    fun findByEmail(email: String): User?

    @Query(
        "MATCH (u:User {email: \$userId}), (m:Movie {title: \$movieId}) " +
                "MERGE (u)-[r:LIKES_MOVIE]->(m) " +
                "SET r.score = \$score " +
                "RETURN u"
    )
    fun addLikedMovie(
        @Param("userId") userId: UserId,
        @Param("movieId") movieId: MovieId,
        @Param("score") score: Int
    ): User

    @Query(
        "MATCH (u:User {email: \$userId})-[r:LIKES_MOVIE]->(m:Movie {title: \$movieId}) " +
                "DELETE r"
    )
    fun removeLikedMovie(
        @Param("userId") userId: UserId,
        @Param("movieId") movieId: MovieId
    )

    @Query(
        "MATCH (u:User {email: \$userId}), (m:Movie {title: \$movieId}) " +
                "MERGE (u)-[r:DISLIKES_MOVIE]->(m) " +
                "RETURN u"
    )
    fun addDislikedMovie(
        @Param("userId") userId: UserId,
        @Param("movieId") movieId: MovieId
    ): User

    @Query(
        "MATCH (u:User {email: \$userId})-[r:DISLIKES_MOVIE]->(m:Movie {title: \$movieId}) " +
                "DELETE r"
    )
    fun removeDislikedMovie(
        @Param("userId") userId: UserId,
        @Param("movieId") movieId: MovieId
    )

    @Query(
        "MATCH (u:User {email: \$userId}), (f:Food {name: \$foodId}) " +
                "MERGE (u)-[r:LIKES_FOOD]->(f) " +
                "SET r.score = \$score " +
                "RETURN u"
    )
    fun addLikedFood(
        @Param("userId") userId: UserId,
        @Param("foodId") foodId: FoodId,
        @Param("score") score: Int
    ): User

    @Query(
        "MATCH (u:User {email: \$userId})-[r:LIKES_FOOD]->(f:Food {name: \$foodId}) " +
                "DELETE r"
    )
    fun removeLikedFood(
        @Param("userId") userId: UserId,
        @Param("foodId") foodId: FoodId
    )

    @Query(
        "MATCH (u:User {email: \$userId}), (f:Food {name: \$foodId}) " +
                "MERGE (u)-[r:DISLIKES_FOOD]->(f) " +
                "RETURN u"
    )
    fun addDislikedFood(
        @Param("userId") userId: UserId,
        @Param("foodId") foodId: FoodId
    ): User

    @Query(
        "MATCH (u:User {email: \$userId})-[r:DISLIKES_FOOD]->(f:Food {name: \$foodId}) " +
                "DELETE r"
    )
    fun removeDislikedFood(
        @Param("userId") userId: UserId,
        @Param("foodId") foodId: FoodId
    )

    @Query(

        "MATCH (u:User {email: \$userId}), (h:Hobby {name: \$hobbyId}) " +
                "MERGE (u)-[:HAS_HOBBY]->(h) " +
                "RETURN u"

    )
    fun addHobby(
        @Param("userId") userId: UserId,
        @Param("hobbyId") hobbyId: HobbyId
    ): User

    @Query(
        "MATCH (u:User {email: \$userId}), (t:PersonalityTrait {name: \$traitId}) " +
                "MERGE (u)-[:HAS_TRAIT]->(t) " +
                "RETURN u"
    )
    fun addTrait(
        @Param("userId") userId: UserId,
        @Param("traitId") traitId: PersonalityTraitId
    ): User

    @Query(
        "MATCH (u:User {email: \$userId})-[r:HAS_HOBBY]->(h:Hobby {name: \$hobbyId}) " +
                "DELETE r"
    )
    fun removeHobby(
        @Param("userId") userId: UserId,
        @Param("hobbyId") hobbyId: HobbyId
    )

    @Query(
        "MATCH (u:User {email: \$userId})-[r:HAS_TRAIT]->(t:PersonalityTrait {name: \$traitId}) " +
                "DELETE r"
    )
    fun removeTrait(
        @Param("userId") userId: UserId,
        @Param("traitId") traitId: PersonalityTraitId
    )

    @Query(
        "MATCH (u:User {email: \$userId})-[:LIKES_FOOD|:LIKES_MOVIE|:HAS_HOBBY|:HAS_TRAIT]->(x)<-[:LIKES_FOOD|:LIKES_MOVIE|:HAS_HOBBY|:HAS_TRAIT]-(other:User) " +
                "WHERE u <> other " +
                "RETURN other, count(x) AS similarity " +
                "ORDER BY similarity DESC " +
                "LIMIT 10"
    )
    fun findTop10SimilarUsers(
        @Param("userId") userId: UserId
    ): List<User>

    @Query(
        "MATCH (u:User {email: \$userId})-[r]->(x)<-[r2]-(other:User) " +
                "WHERE type(r) = \$relationshipType AND type(r2) = \$relationshipType AND u <> other " +
                "RETURN other, count(x) AS similarity " +
                "ORDER BY similarity DESC " +
                "LIMIT 10"
    )
    fun findTop10SimilarUsersByRelationship(
        @Param("userId") userId: UserId,
        @Param("relationshipType") relationshipType: String
    ): List<User>

    @Query(
        "MATCH (u:User {email: \$userId}), (other:User {email: \$otherId}) " +
                "MERGE (u)-[:LIKES_USER]->(other) " +
                "RETURN u"
    )
    fun addLikedUser(
        @Param("userId") userId: UserId,
        @Param("otherId") otherId: UserId
    ): User

    @Query(
        "MATCH (u:User {email: \$userId})-[r:LIKES_USER]->(other:User {email: \$otherId}) " +
                "DELETE r"
    )
    fun removeLikedUser(
        @Param("userId") userId: UserId,
        @Param("otherId") otherId: UserId
    ): User

    @Query(
        "MATCH (u:User {email: \$userId}), (other:User {email: \$otherId}) " +
                "MERGE (u)-[:DISLIKES_USER]->(other) " +
                "RETURN u"
    )
    fun addDislikedUser(
        @Param("userId") userId: UserId,
        @Param("otherId") otherId: UserId
    ): User


    @Query(
        "MATCH (u:User {email: \$userId})-[:LIKES_USER]->(other:User) " +
                "RETURN other " +
                "LIMIT 10"
    )
    fun findTopLikedUsers(
        @Param("userId") userId: UserId
    ): List<User>
}

@Repository
class ManualUserRepository(private val driver: Driver) {

    fun createUser(user: User): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                CREATE (u:User {
                    email: $email,
                    password: $password,
                    firstname: $firstname,
                    lastname: $lastname
                })
                RETURN u
                """,
                mapOf(
                    "email" to user.email,
                    "password" to user.password,
                    "firstname" to user.firstname,
                    "lastname" to user.lastname
                )
            ).single()

            println("Created user record: $record")
            mapToUser(record["u"].asNode())
        }

    fun findByEmail(email: String): User? =
        driver.session().use { session ->
            session.run(
                $$"MATCH (u:User {email: $email}) RETURN u",
                mapOf("email" to email)
            ).list().singleOrNull()?.let {
                mapToUser(it["u"].asNode())
            }
        }

    fun findAllUsersWithRelations(): List<UserWithRelationsDTO> {
        driver.session().use { session ->
            val result = session.run(
                """
                MATCH (user:User)
                OPTIONAL MATCH (user)-[r_lm:LIKES_MOVIE]->(m:Movie)
                OPTIONAL MATCH (user)-[r_dm:DISLIKES_MOVIE]->(m2:Movie)
                OPTIONAL MATCH (user)-[r_lf:LIKES_FOOD]->(f:Food)
                OPTIONAL MATCH (user)-[r_df:DISLIKES_FOOD]->(f2:Food)
                OPTIONAL MATCH (user)-[:HAS_HOBBY]->(h:Hobby)
                OPTIONAL MATCH (user)-[:HAS_TRAIT]->(t:PersonalityTrait)
                OPTIONAL MATCH (user)-[:LIKES_USER]->(lu:User)
                OPTIONAL MATCH (user)-[:DISLIKES_USER]->(du:User)

                RETURN user,
                    collect({movie: m, score: r_lm.score}) AS likedMovies,
                    collect({movie: m2, score: r_dm.score}) AS dislikedMovies,
                    collect({food: f, score: r_lf.score}) AS foods,
                    collect({food: f2, score: r_df.score}) AS dislikeFood,
                    collect(DISTINCT h) AS hobbies,
                    collect(DISTINCT t) AS traits,
                    collect(DISTINCT lu) AS likedUsers,
                    collect(DISTINCT du) AS dislikedUsers
            """.trimIndent()
            )

            return result.list { record -> mapRecordToDTO(record) }
        }
    }

    fun findByIdWithRelations(userId: UserId): UserWithRelationsDTO? {
        driver.session().use { session ->
            val result = session.run(
                """
                MATCH (user:User {email: ${'$'}userId})
                
                OPTIONAL MATCH (user)-[r_lm:LIKES_MOVIE]->(m:Movie)
                OPTIONAL MATCH (user)-[r_dm:DISLIKES_MOVIE]->(m2:Movie)
                OPTIONAL MATCH (user)-[r_lf:LIKES_FOOD]->(f:Food)
                OPTIONAL MATCH (user)-[r_df:DISLIKES_FOOD]->(f2:Food)
                
                OPTIONAL MATCH (user)-[:HAS_HOBBY]->(h:Hobby)
                OPTIONAL MATCH (user)-[:HAS_TRAIT]->(t:PersonalityTrait)
                OPTIONAL MATCH (user)-[:LIKES_USER]->(lu:User)
                OPTIONAL MATCH (user)-[:DISLIKES_USER]->(du:User)

                RETURN user,
                    collect({movie: m, score: r_lm.score}) AS likedMovies,
                    collect({movie: m2, score: r_dm.score}) AS dislikedMovies,
                    collect({food: f, score: r_lf.score}) AS foods,
                    collect({food: f2, score: r_df.score}) AS dislikeFood,
                    collect(DISTINCT h) AS hobbies,
                    collect(DISTINCT t) AS traits,
                    collect(DISTINCT lu) AS likedUsers,
                    collect(DISTINCT du) AS dislikedUsers
            """.trimIndent(), mapOf("userId" to userId)
            )

            return result.list().singleOrNull()?.let { mapRecordToDTO(it) }
        }
    }

    // =======================
    // LIKE / DISLIKE MOVIE
    // =======================
    fun addLikedMovie(userId: UserId, movieId: MovieId, score: Int): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (m:Movie {title: $movieId})
                MERGE (u)-[r:LIKES_MOVIE]->(m)
                SET r.score = $score
                RETURN u
                """,
                mapOf("userId" to userId, "movieId" to movieId, "score" to score)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeLikedMovie(userId: UserId, movieId: MovieId) =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:LIKES_MOVIE]->(m:Movie {title: $movieId})
                DELETE r
                """,
                mapOf("userId" to userId, "movieId" to movieId)
            )
        }

    fun addDislikedMovie(userId: UserId, movieId: MovieId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (m:Movie {title: $movieId})
                MERGE (u)-[r:DISLIKES_MOVIE]->(m)
                RETURN u
                """,
                mapOf("userId" to userId, "movieId" to movieId)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeDislikedMovie(userId: UserId, movieId: MovieId) =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:DISLIKES_MOVIE]->(m:Movie {title: $movieId})
                DELETE r
                """,
                mapOf("userId" to userId, "movieId" to movieId)
            )
        }

    // =======================
    // LIKE / DISLIKE FOOD
    // =======================
    fun addLikedFood(userId: UserId, foodId: FoodId, score: Int): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (f:Food {name: $foodId})
                MERGE (u)-[r:LIKES_FOOD]->(f)
                SET r.score = $score
                RETURN u
                """,
                mapOf("userId" to userId, "foodId" to foodId, "score" to score)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeLikedFood(userId: UserId, foodId: FoodId) =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:LIKES_FOOD]->(f:Food {name: $foodId})
                DELETE r
                """,
                mapOf("userId" to userId, "foodId" to foodId)
            )
        }

    fun addDislikedFood(userId: UserId, foodId: FoodId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (f:Food {name: $foodId})
                MERGE (u)-[r:DISLIKES_FOOD]->(f)
                RETURN u
                """,
                mapOf("userId" to userId, "foodId" to foodId)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeDislikedFood(userId: UserId, foodId: FoodId) =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:DISLIKES_FOOD]->(f:Food {name: $foodId})
                DELETE r
                """,
                mapOf("userId" to userId, "foodId" to foodId)
            )
        }

    // =======================
    // HOBBY / TRAIT
    // =======================
    fun addHobby(userId: UserId, hobbyId: HobbyId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (h:Hobby {name: $hobbyId})
                MERGE (u)-[:HAS_HOBBY]->(h)
                RETURN u
                """,
                mapOf("userId" to userId, "hobbyId" to hobbyId)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeHobby(userId: UserId, hobbyId: HobbyId) =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:HAS_HOBBY]->(h:Hobby {name: $hobbyId})
                DELETE r
                """,
                mapOf("userId" to userId, "hobbyId" to hobbyId)
            )
        }

    fun addTrait(userId: UserId, traitId: PersonalityTraitId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (t:PersonalityTrait {name: $traitId})
                MERGE (u)-[:HAS_TRAIT]->(t)
                RETURN u
                """,
                mapOf("userId" to userId, "traitId" to traitId)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeTrait(userId: UserId, traitId: PersonalityTraitId) =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:HAS_TRAIT]->(t:PersonalityTrait {name: $traitId})
                DELETE r
                """,
                mapOf("userId" to userId, "traitId" to traitId)
            )
        }

    // =======================
    // USER RELATIONS
    // =======================
    fun addLikedUser(userId: UserId, otherId: UserId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (other:User {email: $otherId})
                MERGE (u)-[:LIKES_USER]->(other)
                RETURN u
                """,
                mapOf("userId" to userId, "otherId" to otherId)
            ).single()
            mapToUser(record["u"].asNode())
        }

    fun removeLikedUser(userId: UserId, otherId: UserId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r:LIKES_USER]->(other:User {email: $otherId})
                DELETE r
                RETURN u
                """,
                mapOf("userId" to userId, "otherId" to otherId)
            ).single()
            (record?.let { mapToUser(it["u"].asNode()) } ?: findByEmail(userId)) as User
        }

    fun addDislikedUser(userId: UserId, otherId: UserId): User =
        driver.session().use { session ->
            val record = session.run(
                $$"""
                MATCH (u:User {email: $userId}), (other:User {email: $otherId})
                MERGE (u)-[:DISLIKES_USER]->(other)
                RETURN u
                """,
                mapOf("userId" to userId, "otherId" to otherId)
            ).single()
            mapToUser(record["u"].asNode())
        }


    // =======================
    // SIMILAR USERS
    // =======================
    fun findTop10SimilarUsers(userId: UserId): List<User> =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[:LIKES_FOOD|:LIKES_MOVIE|:HAS_HOBBY|:HAS_TRAIT]->(x)<-[:LIKES_FOOD|:LIKES_MOVIE|:HAS_HOBBY|:HAS_TRAIT]-(other:User)
                WHERE u <> other
                RETURN other, count(x) AS similarity
                ORDER BY similarity DESC
                LIMIT 10
                """,
                mapOf("userId" to userId)
            ).list { mapToUser(it["other"].asNode()) }
        }

    fun findTop10SimilarUsersByRelationship(userId: UserId, relationshipType: String): List<User> =
        driver.session().use { session ->
            session.run(
                $$"""
                MATCH (u:User {email: $userId})-[r]->(x)<-[r2]-(other:User)
                WHERE type(r) = $relationshipType AND type(r2) = $relationshipType AND u <> other
                RETURN other, count(x) AS similarity
                ORDER BY similarity DESC
                LIMIT 10
                """,
                mapOf("userId" to userId, "relationshipType" to relationshipType)
            ).list { mapToUser(it["other"].asNode()) }
        }

    fun findTopLikedUsers(userId: UserId): List<User> =
        driver.session().use { session ->
            session.run(
                """
                MATCH (u:User {email: \$userId})-[:LIKES_USER]->(other:User)
                RETURN other
                LIMIT 10
                """,
                mapOf("userId" to userId)
            ).list { mapToUser(it["other"].asNode()) }
        }

    ////////////////////////

    private fun mapRecordToDTO(record: org.neo4j.driver.Record): UserWithRelationsDTO {
        val userNode = record["user"].asNode()

        return UserWithRelationsDTO(
            user = mapToUser(userNode.asMap()),

            likedMovies = mapMovieRelations(record["likedMovies"]),
            dislikedMovies = mapMovieRelations(record["dislikedMovies"]),

            foods = mapFoodRelations(record["foods"]),
            dislikeFood = mapFoodRelations(record["dislikeFood"]),

            hobbies = mapSimpleNodeCollection(record["hobbies"], ::mapToHobby),
            traits = mapSimpleNodeCollection(record["traits"], ::mapToTrait),
            likedUsers = mapSimpleNodeCollection(record["likedUsers"], ::mapToUser),
            dislikedUsers = mapSimpleNodeCollection(record["dislikedUsers"], ::mapToUser)
        )
    }

    private fun mapMovieRelations(value: org.neo4j.driver.Value): List<MovieRelationship> {
        return value.asList { v ->
            val map = v.asMap()
            val movieNode = map["movie"] as? Node
            val score = (map["score"] as? Long)?.toInt()

            if (movieNode != null && score != null) MovieRelationship(
                movie = mapToMovie(movieNode.asMap()),
                score = score
            ) else null
        }.filterNotNull()
    }

    private fun mapFoodRelations(value: org.neo4j.driver.Value): List<FoodRelationship> {
        return value.asList { v ->
            val map = v.asMap()
            val foodNode = map["food"] as? Node
            val score = (map["score"] as? Long)?.toInt()

            if (foodNode != null && score != null) FoodRelationship(
                food = mapToFood(foodNode.asMap()),
                score = score
            ) else null
        }.filterNotNull()
    }

    private fun <T> mapSimpleNodeCollection(
        value: org.neo4j.driver.Value,
        mapper: (Map<String, Any>) -> T
    ): List<T> {
        return value.asList {
            mapper(it.asNode().asMap())
        }
    }

    private fun mapToUser(map: Map<String, Any>) = User(
        email = map["email"] as String,
        password = map["password"] as String,
        firstname = map["firstname"] as String,
        lastname = map["lastname"] as String
    )

    private fun mapToMovie(map: Map<String, Any>) = Movie(
        title = map["title"] as String,
        genre = MovieGenre.valueOf(map["genre"] as String),
        director = map["director"] as String,
        releaseYear = (map["releaseYear"] as Long).toInt()
    )

    private fun mapToFood(map: Map<String, Any>) = Food(
        name = map["name"] as String,
        cuisine = map["cuisine"] as String,
        isVegetarian = map["isVegetarian"] as Boolean
    )

    private fun mapToHobby(map: Map<String, Any>) = Hobby(
        name = map["name"] as String,
        description = map["description"] as String
    )

    private fun mapToTrait(map: Map<String, Any>) = PersonalityTrait(
        name = map["name"] as String,
        type = PersonalityTraitType.valueOf(map["type"] as String)
    )

    private fun mapToUser(node: Node) = User(
        email = node["email"].asString(),
        password = node["password"].asString(),
        firstname = node["firstname"].asString(),
        lastname = node["lastname"].asString()
    )

    private fun mapToMovie(node: Node) = Movie(
        title = node["title"].asString(),
        genre = MovieGenre.valueOf(node["genre"].asString()),
        director = node["director"].asString(),
        releaseYear = node["releaseYear"].asInt()
    )

    private fun mapToFood(node: Node) = Food(
        name = node["name"].asString(),
        cuisine = node["cuisine"].asString(),
        isVegetarian = node["isVegetarian"].asBoolean()
    )

    private fun mapToHobby(node: Node) = Hobby(
        name = node["name"].asString(),
        description = node["description"].asString()
    )

    private fun mapToTrait(node: Node) = PersonalityTrait(
        name = node["name"].asString(),
        type = PersonalityTraitType.valueOf(node["type"].asString())
    )
}