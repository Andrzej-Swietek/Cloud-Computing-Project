package com.friendly.recommender.config.seeder

import com.friendly.recommender.entities.User
import com.friendly.recommender.entities.respositories.HobbyRepository
import com.friendly.recommender.entities.respositories.PersonalityTraitRepository
import com.friendly.recommender.entities.respositories.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(100)
@Component
class UserSeeder(
    private val userRepository: UserRepository,
    private val traitRepository: PersonalityTraitRepository,
    private val hobbyRepository: HobbyRepository
) {

    @EventListener(ApplicationReadyEvent::class)
    fun run() {
        println(">>> UserSeeder starting...")

        if (userRepository.count() > 0) {
            println(">>> Users already exist, skipping seeding")
            return
        }

        val extrovert = traitRepository.findById("Friendly").orElseThrow {
            RuntimeException("Trait Extrovert not found")
        }
        val introvert = traitRepository.findById("Calm").orElseThrow {
            RuntimeException("Trait Introvert not found")
        }
        val reading = hobbyRepository.findByName("Reading")
        val cooking = hobbyRepository.findByName("Cooking")

        val alice = User("alice@example.com", "password", "Alice", "Smith")
        val bob = User("bob@example.com", "password", "Bob", "Johnson")
        val charlie = User("charlie@example.com", "password", "Charlie", "Brown")

        alice.traits.add(extrovert)
        bob.traits.add(introvert)
        charlie.traits.addAll(listOf(extrovert, introvert))

        alice.hobbies.addAll(listOf(reading, cooking).flatten())
        bob.hobbies.addAll(listOf(reading, cooking).flatten())
        charlie.hobbies.addAll(listOf(reading, cooking).flatten())

        alice.likedUsers.add(bob)
        bob.likedUsers.add(charlie)
        charlie.dislikedUsers.add(alice)

        userRepository.saveAll(listOf(alice, bob, charlie))
        println(">>> Users seeded")
    }
}
