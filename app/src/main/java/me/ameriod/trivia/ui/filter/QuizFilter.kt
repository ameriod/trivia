package me.ameriod.trivia.ui.filter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.api.response.Difficulty

data class QuizFilter(val count: Int = 10,
                      val difficulty: Difficulty,
                      val category: Category) : Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readParcelable<Difficulty>(Difficulty::class.java.classLoader),
            source.readParcelable<Category>(Category::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(count)
        writeParcelable(difficulty, 0)
        writeParcelable(category, 0)
    }

    companion object {

        @JvmStatic
        fun createDefault(context: Context): QuizFilter {
            return QuizFilter(10, Difficulty.createDefault(context), Category.createAll(context))
        }

        @JvmField
        val CREATOR: Parcelable.Creator<QuizFilter> = object : Parcelable.Creator<QuizFilter> {
            override fun createFromParcel(source: Parcel): QuizFilter = QuizFilter(source)
            override fun newArray(size: Int): Array<QuizFilter?> = arrayOfNulls(size)
        }
    }
}