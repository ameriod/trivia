package me.ameriod.trivia.ui.quiz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question
import me.ameriod.trivia.ui.result.ResultItem

@Parcelize
data class Quiz(
        private val questions: List<Question>,
        private val answers: MutableList<Answer> = mutableListOf(),
        private var position: Int = 0,
        val startTime: Long = System.currentTimeMillis()
) : Parcelable {

    fun getCurrentQuestion(): Question = questions[position]

    fun isLastQuestion(): Boolean = answers.size == questions.size - 1

    fun isQuizDone(): Boolean = answers.size == questions.size

    fun getNextQuestion(): Question {
        position++
        if (position > answers.size) throw IllegalAccessException("Error need to call setAnswer before getting the next text")
        if (isQuizDone()) throw IllegalAccessException("Error on last text, you need to check....")
        return questions[position]
    }

    fun getCurrentPosition(): Int {
        return position + 1
    }

    fun getNumberOfQuestions(): Int {
        return questions.size
    }

    fun setAnswer(answer: Answer) {
        answers.add(answer)
    }

    fun toResult(): Result {
        val date = System.currentTimeMillis()
        val totalTime = date - startTime
        val results = questions.mapIndexed { index, question ->
            val answer = answers[index]
            ResultItem(question.text, answer.display, question.answers
                    .filter { check ->
                        check.correct
                    }
                    .map { correct ->
                        correct.display
                    }, answer.correct)
        }
        val total = results.size
        val correct = results.filter { result -> result.isCorrect }.size
        val incorrect = total - correct
        return Result(null, Result.gson.toJson(results, Result.type), total, correct, incorrect, totalTime, date)
    }

}