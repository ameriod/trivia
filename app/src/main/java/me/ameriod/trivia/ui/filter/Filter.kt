package me.ameriod.trivia.ui.filter

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.api.response.OtCategory
import me.ameriod.trivia.api.response.OtDifficulty

@Parcelize
data class Filter(
        var count: Int = 10,
        var difficulty: OtDifficulty,
        var category: OtCategory
) : Parcelable {

    companion object {

        @JvmStatic
        fun createDefault(context: Context): Filter {
            return Filter(10, OtDifficulty.createDefault(context), OtCategory.createAll(context))
        }

    }
}