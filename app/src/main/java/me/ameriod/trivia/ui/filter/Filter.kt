package me.ameriod.trivia.ui.filter

import me.ameriod.trivia.api.response.Category

data class Filter(val count: Int,
                  val difficulty: String,
                  val category: Category) {
}