package me.ameriod.trivia.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OtResponseQuestions(@Expose
                               @SerializedName("response_code")
                               val responseCode: Int,
                               @Expose
                               @SerializedName("results")
                               val results: List<OtQuestion>)