package me.ameriod.trivia.ui.quiz.question

import android.os.Parcel
import android.os.Parcelable

data class Question(val text: String,
                    val answers: List<Answer>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(Answer.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(text)
        writeTypedList(answers)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Question> = object : Parcelable.Creator<Question> {
            override fun createFromParcel(source: Parcel): Question = Question(source)
            override fun newArray(size: Int): Array<Question?> = arrayOfNulls(size)
        }
    }
}