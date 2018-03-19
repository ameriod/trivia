package me.ameriod.trivia.ui.quiz

import io.reactivex.Observable
import me.ameriod.trivia.api.OpenTriviaRepository

class QuizInteractor(private val repository: OpenTriviaRepository) : QuizContract.Interactor {

    override fun saveResults(quiz: Quiz): Observable<Long> = repository.saveQuizAsResult(quiz)

}