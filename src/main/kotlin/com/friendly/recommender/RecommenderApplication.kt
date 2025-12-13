package com.friendly.recommender

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RecommenderApplication

fun main(args: Array<String>) {
	runApplication<RecommenderApplication>(*args)
}
