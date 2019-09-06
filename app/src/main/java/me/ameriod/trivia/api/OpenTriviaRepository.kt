package me.ameriod.trivia.api

import android.content.Context
import io.reactivex.Observable
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.api.db.TriviaDao
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty
import me.ameriod.trivia.ui.filter.Filter
import me.ameriod.trivia.ui.quiz.Quiz


class OpenTriviaRepository(
        private val context: Context,
        private val service: OpenTriviaService,
        private val triviaDao: TriviaDao
) {

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