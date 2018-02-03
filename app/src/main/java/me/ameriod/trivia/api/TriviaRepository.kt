package me.ameriod.trivia.api

import android.content.Context
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty
import me.ameriod.trivia.api.response.ResponseQuestions
import me.ameriod.trivia.ui.filter.Filter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class TriviaRepository(val context: Context) {

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

    fun getQuestions(filter: Filter): Observable<ResponseQuestions> =
            service.getQuestions(filter.count, filter.difficulty.value, filter.category.id, null)

    fun getApiToken(): Observable<String> = service.getApiToken()
            .map { response ->
                response.token
            }

    fun getCategories(): Observable<List<Category>> = service.getCategories()
            .map { response ->
                val categories = response.triviaCategories.toMutableList()
                // add all
                categories.add(0, Category.createAll(context))
                categories
            }

    fun getDifficulties(): Observable<List<Difficulty>> = Observable.just(listOf(
            Difficulty.createDefault(context),
            Difficulty(context.getString(R.string.filter_difficulty_easy), "easy"),
            Difficulty(context.getString(R.string.filter_difficulty_medium), "medium"),
            Difficulty(context.getString(R.string.filter_difficulty_hard), "hard")))
}