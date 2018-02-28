package me.ameriod.trivia.api.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.ameriod.trivia.ui.quiz.question.Answer
import me.ameriod.trivia.ui.quiz.question.Question

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

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(category)
        writeString(apiType)
        writeString(difficulty)
        writeString(question)
        writeString(correctAnswer)
        writeStringList(incorrectAnswers)
    }

    enum class Type {
        MULTIPLE, BOOLEAN
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OtQuestion> = object : Parcelable.Creator<OtQuestion> {
            override fun createFromParcel(source: Parcel): OtQuestion = OtQuestion(source)
            override fun newArray(size: Int): Array<OtQuestion?> = arrayOfNulls(size)
        }
    }
}