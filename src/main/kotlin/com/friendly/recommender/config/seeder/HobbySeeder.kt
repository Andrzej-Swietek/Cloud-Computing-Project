package com.friendly.recommender.config.seeder

import com.friendly.recommender.api.hobby.HobbyService
import com.friendly.recommender.entities.Hobby
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class HobbySeeder(private val hobbyService: HobbyService) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (hobbyService.getAll().isEmpty()) {
            val hobbies = listOf(
                Hobby(name = "Painting", description = "Creating art with paints"),
                Hobby(name = "Cycling", description = "Riding bicycles for leisure or sport"),
                Hobby(name = "Gardening", description = "Growing and maintaining plants"),
                Hobby(name = "Cooking", description = "Preparing and experimenting with food"),
                Hobby(name = "Photography", description = "Capturing moments using a camera"),
                Hobby(name = "Writing", description = "Expressing ideas through written words"),
                Hobby(name = "Dancing", description = "Performing dance routines"),
                Hobby(name = "Hiking", description = "Walking long distances in nature"),
                Hobby(name = "Swimming", description = "Moving through water using limbs"),
                Hobby(name = "Gaming", description = "Playing video or board games")
            )
            hobbies.forEach { hobbyService.create(it) }
        }
    }
}
