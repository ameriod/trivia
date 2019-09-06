package me.ameriod.trivia.api.response

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R

@Parcelize
data class OtDifficulty(val display: String,
                        val value: String?) : Parcelable {

    companion object {
        @JvmStatic
        fun createDefault(context: Context) =
                OtDifficulty(context.getString(R.string.filter_difficulty_all), null)
    }
}