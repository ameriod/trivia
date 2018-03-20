package me.ameriod.trivia.api

import android.arch.persistence.room.Room
import android.content.Context
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.api.db.TriviaDao
import me.ameriod.trivia.api.db.TriviaDatabase
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.ui.filter.Filter
import me.ameriod.trivia.ui.quiz.Quiz
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class OpenTriviaRepository(val context: Context) {

    private val triviaDb = Room.databaseBuilder(context.applicationContext,
            TriviaDatabase::class.java, "results").build()

    private val triviaDao: TriviaDao = triviaDb.resultDao()

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

    fun getQuiz(filter: Filter): Observable<Quiz> =
            service.getQuestions(filter.count, filter.difficulty.value, filter.category.id, null)
                    .map { response ->
                        Quiz(response.results.map { question ->
                            question.convert()
                        })
                    }

    fun getApiToken(): Observable<String> = service.getApiToken()
            .map { response ->
                response.token
            }

    fun getCategories(): Observable<List<OtCategory>> = service.getCategories()
            .map { response ->
                val categories = response.triviaCategories.toMutableList()
                // add all
                categories.add(0, OtCategory.createAll(context))
                categories
            }

    fun getDifficulties(): Observable<List<OtDifficulty>> = Observable.just(listOf(
            OtDifficulty.createDefault(context),
            OtDifficulty(context.getString(R.string.filter_difficulty_easy), "easy"),
            OtDifficulty(context.getString(R.string.filter_difficulty_medium), "medium"),
            OtDifficulty(context.getString(R.string.filter_difficulty_hard), "hard")))

    fun saveQuizAsResult(quiz: Quiz): Observable<Long> = Observable.just(quiz)
            .map { it.toResult() }
            .map { triviaDao.insert(it) }

    fun getResult(id: Long): Observable<Result> = triviaDao.byId(id)
            .distinctUntilChanged()
            .toObservable()

    fun getHistory(): Observable<List<Result>> = triviaDao.getAll()
            .distinctUntilChanged()
            .toObservable()

}