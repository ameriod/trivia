package me.ameriod.trivia.ui.result

import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.api.response.Question

data class QuizResult(val answers: List<String>,
                      val questions: List<Question>) : Parcelable {
    constructor(source: Parcel) : this(
            source.createStringArrayList(),
            source.createTypedArrayList(Question.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeStringList(answers)
        writeTypedList(questions)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QuizResult> = object : Parcelable.Creator<QuizResult> {
            override fun createFromParcel(source: Parcel): QuizResult = QuizResult(source)
            override fun newArray(size: Int): Array<QuizResult?> = arrayOfNulls(size)
        }
    }
}