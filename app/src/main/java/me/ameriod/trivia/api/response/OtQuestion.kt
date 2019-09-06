package me.ameriod.trivia.api.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question

@Parcelize
data class OtQuestion(@Expose
                      @SerializedName("category")
                      val category: String,
                      @Expose
                      @SerializedName("type")
                      val apiType: String,
                      @Expose
                      @SerializedName("difficulty")
                      val difficulty: String,
                      @Expose
                      @SerializedName("question")
                      val question: String,
                      @Expose
                      @SerializedName("correct_answer")
                      val correctAnswer: String,
                      @Expose
                      @SerializedName("incorrect_answers")
                      val incorrectAnswers: List<String>) : Parcelable {

    fun convert(): Question {
        // convert
        val answers = incorrectAnswers.map { answer ->
            Answer(answer, false, false)
        }.toMutableList()
        answers.add(Answer(correctAnswer, true, false))
        // mix them up
        answers.shuffle()
        return Question(question, answers)
    }

    enum class Type {
        MULTIPLE, BOOLEAN
    }
}