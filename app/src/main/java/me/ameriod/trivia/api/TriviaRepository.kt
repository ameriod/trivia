package me.ameriod.trivia.api

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.ResponseQuestions
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class TriviaRepository {

    private val service: OpenTriviaService by lazy {
        val logging = HttpLoggingInterceptor({ message ->
            Timber.tag("API").d(message)
        })
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

        val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        retrofit.create(OpenTriviaService::class.java)
    }

    fun getQuestions(amount: Int = 10,
                     token: String? = null,
                     difficulty: String? = null,
                     category: String? = null): Observable<ResponseQuestions> =
            service.getQuestions()

    fun getApiToken(): Observable<String> = service.getApiToken()
            .map { response ->
                response.token
            }

    fun getCategories(): Observable<List<Category>> = service.getCategories()
            .map { response ->
                response.triviaCategories
            }
}