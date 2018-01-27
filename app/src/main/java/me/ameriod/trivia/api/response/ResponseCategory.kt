package me.ameriod.trivia.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseCategory(@Expose
                            @SerializedName("trivia_categories")
                            val triviaCategories: List<Category>)