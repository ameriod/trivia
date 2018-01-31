package me.ameriod.trivia.ui.quiz

import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.api.response.Question

data class Quiz(private val questions: List<Question>,
                private val answers: MutableList<String> = mutableListOf(),
                private var position: Int = 0) : Parcelable {

    constructor(source: Parcel) : this(
            source.createTypedArrayList(Question.CREATOR),
            source.createStringArrayList(),
            source.readInt()
    )

    fun getCurrentQuestion() : Question = questions[position]

    fun isLastQuestion() : Boolean = position > questions.size

    fun isQuizDone() : Boolean = position >- questions.size

    fun getNextQuestion() : Question {
        if (isLastQuestion())
        position++
        return  questions[0]
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(questions)
        writeStringList(answers)
        writeInt(position)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Quiz> = object : Parcelable.Creator<Quiz> {
            override fun createFromParcel(source: Parcel): Quiz = Quiz(source)
            override fun newArray(size: Int): Array<Quiz?> = arrayOfNulls(size)
        }
    }
}