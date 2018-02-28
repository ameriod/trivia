package me.ameriod.trivia.ui.result

import android.os.Parcel
import android.os.Parcelable

data class Result(val items: List<ResultItem>,
                  val totalQuestions: Int,
                  val correctQuestions: Int,
                  val incorrectQuestions: Int,
                  val totalTime: Long,
                  val date: Long) : Parcelable {

    constructor(source: Parcel) : this(
            source.createTypedArrayList(ResultItem.CREATOR),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readLong(),
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(items)
        writeInt(totalQuestions)
        writeInt(correctQuestions)
        writeInt(incorrectQuestions)
        writeLong(totalTime)
        writeLong(date)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Result> = object : Parcelable.Creator<Result> {
            override fun createFromParcel(source: Parcel): Result = Result(source)
            override fun newArray(size: Int): Array<Result?> = arrayOfNulls(size)
        }
    }
}