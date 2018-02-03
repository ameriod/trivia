package me.ameriod.trivia.api.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import me.ameriod.trivia.R

data class OtDifficulty(val display: String,
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
                OtDifficulty(context.getString(R.string.filter_difficulty_all), null)

        @JvmField
        val CREATOR: Parcelable.Creator<OtDifficulty> = object : Parcelable.Creator<OtDifficulty> {
            override fun createFromParcel(source: Parcel): OtDifficulty = OtDifficulty(source)
            override fun newArray(size: Int): Array<OtDifficulty?> = arrayOfNulls(size)
        }
    }
}