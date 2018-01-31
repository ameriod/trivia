package me.ameriod.trivia.api.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.R

data class Difficulty(val display: String,
                      val value: String?) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(display)
        writeString(value)
    }

    companion object {
        @JvmStatic
        fun createDefault(context: Context) =
                Difficulty(context.getString(R.string.filter_difficulty_all), null)

        @JvmField
        val CREATOR: Parcelable.Creator<Difficulty> = object : Parcelable.Creator<Difficulty> {
            override fun createFromParcel(source: Parcel): Difficulty = Difficulty(source)
            override fun newArray(size: Int): Array<Difficulty?> = arrayOfNulls(size)
        }
    }
}