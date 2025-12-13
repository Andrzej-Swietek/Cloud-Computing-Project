package com.friendly.recommender.exceptions

import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
class ValidationException : RuntimeException() {
    val errors: MutableList<String?>? = null

    override val message: String
        get() = java.lang.String.join("\n", errors)
}