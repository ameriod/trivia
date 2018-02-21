package me.ameriod.trivia.ui.result

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultItem(@Expose
                 @SerializedName("question")
                 val questionText: String,
                 @Expose
                 @SerializedName("answer")
                 val selectedAnswer: String,
                 @Expose
                 @SerializedName("correctAnswers")
                 val correctAnswers: List<String>,
                 @Expose
                 @SerializedName("correct")
                 val isCorrect: Boolean) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.createStringArrayList(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(questionText)
        writeString(selectedAnswer)
        writeStringList(correctAnswers)
        writeInt((if (isCorrect) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ResultItem> = object : Parcelable.Creator<ResultItem> {
            override fun createFromParcel(source: Parcel): ResultItem = ResultItem(source)
            override fun newArray(size: Int): Array<ResultItem?> = arrayOfNulls(size)
        }
    }
}
