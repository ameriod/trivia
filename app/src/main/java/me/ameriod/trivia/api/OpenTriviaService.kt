package me.ameriod.trivia.api

import io.reactivex.Observable
import me.ameriod.trivia.api.response.OtResponseCategories
import me.ameriod.trivia.api.response.OtResponseQuestions
import me.ameriod.trivia.api.response.OtResponseToken
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API from https://opentdb.com/
 */
interface OpenTriviaService {

    @GET("/api_token.php")
    fun getApiToken(): Observable<OtResponseToken>

    @GET("/api_category.php")
    fun getCategories(): Observable<OtResponseCategories>

    @GET("/api.php")
    fun getQuestions(@Query("amount") amount: Int = 10,
                     @Query("difficulty") difficulty: String? = null,
                     @Query("category") category: String? = null,
                     @Query("token") token: String? = null): Observable<OtResponseQuestions>
}