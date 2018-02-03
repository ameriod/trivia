package me.ameriod.trivia.ui.quiz.question

import android.os.Parcel
import android.os.Parcelable

data class QuizQuestion(private val display: String,
                        private val correct: Boolean,
                        private var checked: Boolean) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(display)
        writeInt((if (correct) 1 else 0))
        writeInt((if (checked) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QuizQuestion> = object : Parcelable.Creator<QuizQuestion> {
            override fun createFromParcel(source: Parcel): QuizQuestion = QuizQuestion(source)
            override fun newArray(size: Int): Array<QuizQuestion?> = arrayOfNulls(size)
        }
    }
}