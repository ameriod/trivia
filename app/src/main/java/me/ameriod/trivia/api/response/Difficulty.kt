package me.ameriod.trivia.api.response

import android.os.Parcel
import android.os.Parcelable

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

        @JvmField
        val CREATOR: Parcelable.Creator<Difficulty> = object : Parcelable.Creator<Difficulty> {
            override fun createFromParcel(source: Parcel): Difficulty = Difficulty(source)
            override fun newArray(size: Int): Array<Difficulty?> = arrayOfNulls(size)
        }
    }
}