package me.ameriod.trivia.ui.filter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty

data class Filter(var count: Int = 10,
                  var difficulty: OtDifficulty,
                  var category: OtCategory) : Parcelable {

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readParcelable<OtDifficulty>(OtDifficulty::class.java.classLoader),
            source.readParcelable<OtCategory>(OtCategory::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(count)
        writeParcelable(difficulty, 0)
        writeParcelable(category, 0)
    }

    companion object {

        @JvmStatic
        fun createDefault(context: Context): Filter {
            return Filter(10, OtDifficulty.createDefault(context), OtCategory.createAll(context))
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Filter> = object : Parcelable.Creator<Filter> {
            override fun createFromParcel(source: Parcel): Filter = Filter(source)
            override fun newArray(size: Int): Array<Filter?> = arrayOfNulls(size)
        }
    }
}