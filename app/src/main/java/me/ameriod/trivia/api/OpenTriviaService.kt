package me.ameriod.trivia.api

import io.reactivex.Observable
import me.ameriod.trivia.api.response.ResponseCategory
import me.ameriod.trivia.api.response.ResponseQuestions
import me.ameriod.trivia.api.response.ResponseToken
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTriviaService {

    @GET("/api_token.php")
    fun getApiToken(): Observable<ResponseToken>

    @GET("/api_category.php")
    fun getCategories(): Observable<ResponseCategory>

    @GET("/api.php")
    fun getQuestions(@Query("amount") amount: Int = 10,
                     @Query("token") token: String? = null,
                     @Query("difficulty") difficulty: String? = null,
                     @Query("category") category: String? = null): Observable<ResponseQuestions>
}