package me.ameriod.trivia.ui.quiz

import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.api.response.Question
import me.ameriod.trivia.ui.quiz.question.Answer

data class Quiz(private val questions: List<Question>,
                private val answers: MutableList<Answer> = mutableListOf(),
                private var position: Int = 0,
                private val startTime: Long = System.currentTimeMillis()) : Parcelable {
    fun getCurrentQuestion(): Question = questions[position]

    fun isLastQuestion(): Boolean = answers.size == questions.size - 1

    fun isQuizDone(): Boolean = answers.size == questions.size

    fun getNextQuestion(): Question {
        position++
        if (position > answers.size) throw IllegalAccessException("Error need to call setAnswer before getting the next question")
        if (isQuizDone()) throw IllegalAccessException("Error on last question, you need to check....")
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

    constructor(source: Parcel) : this(
            source.createTypedArrayList(Question.CREATOR),
            source.createTypedArrayList(Answer.CREATOR),
            source.readInt(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(questions)
        writeTypedList(answers)
        writeInt(position)
        writeLong(startTime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Quiz> = object : Parcelable.Creator<Quiz> {
            override fun createFromParcel(source: Parcel): Quiz = Quiz(source)
            override fun newArray(size: Int): Array<Quiz?> = arrayOfNulls(size)
        }
    }
}