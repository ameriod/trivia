package me.ameriod.trivia.ui.filter

import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.api.response.Category

data class QuizFilter(val count: Int,
                      val difficulty: String?,
                      val category: Category?) : Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readParcelable<Category>(Category::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(count)
        writeString(difficulty)
        writeParcelable(category, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<QuizFilter> = object : Parcelable.Creator<QuizFilter> {
            override fun createFromParcel(source: Parcel): QuizFilter = QuizFilter(source)
            override fun newArray(size: Int): Array<QuizFilter?> = arrayOfNulls(size)
        }
    }
}