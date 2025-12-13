package com.friendly.recommender.config.seeder

import com.friendly.recommender.api.personality.PersonalityTraitService
import com.friendly.recommender.entities.PersonalityTrait
import com.friendly.recommender.entities.PersonalityTraitType
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class PersonalityTraitSeeder(private val traitService: PersonalityTraitService) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (traitService.getAll().isEmpty()) {
            val traits = listOf(
                PersonalityTrait(name = "Friendly", type = PersonalityTraitType.POSITIVE),
                PersonalityTrait(name = "Curious", type = PersonalityTraitType.POSITIVE),
                PersonalityTrait(name = "Patient", type = PersonalityTraitType.POSITIVE),
                PersonalityTrait(name = "Moody", type = PersonalityTraitType.NEGATIVE),
                PersonalityTrait(name = "Impulsive", type = PersonalityTraitType.NEGATIVE),
                PersonalityTrait(name = "Calm", type = PersonalityTraitType.POSITIVE),
                PersonalityTrait(name = "Shy", type = PersonalityTraitType.NEUTRAL),
                PersonalityTrait(name = "Adventurous", type = PersonalityTraitType.POSITIVE),
                PersonalityTrait(name = "Stubborn", type = PersonalityTraitType.NEGATIVE),
                PersonalityTrait(name = "Optimistic", type = PersonalityTraitType.POSITIVE)
            )
            traits.forEach { traitService.create(it) }
        }
    }
}