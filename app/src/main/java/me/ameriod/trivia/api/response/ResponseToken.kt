package me.ameriod.trivia.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseToken(@Expose
                         @SerializedName("token")
                         val token: String)