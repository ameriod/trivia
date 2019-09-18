package me.ameriod.trivia.api.db

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.history.HistoryViewHolder
import me.ameriod.trivia.ui.result.ResultItem

@Parcelize
@Entity(tableName = "results")
data class Result(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long?,
        @ColumnInfo(name = "items")
        var jsonItems: String,
        @ColumnInfo(name = "total_questions")
        var totalQuestions: Int,
        @ColumnInfo(name = "correct_questions")
        var correctQuestions: Int,
        @ColumnInfo(name = "incorrect_questions")
        var incorrectQuestions: Int,
        @ColumnInfo(name = "total_time")
        var totalTime: Long,
        @ColumnInfo(name = "date")
        var date: Long
) : TriviaAdapterItem, Parcelable {

    constructor() : this(null, "[]", 0, 0, 0, 0, 0)

    @Ignore
    private var items: List<ResultItem>? = null

    fun getItems(): List<ResultItem> = if (items == null) {
        items = gson.fromJson<List<ResultItem>>(jsonItems, type)
        items!!
    } else {
        items!!
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> =
            HistoryViewHolder(inflater.inflate(R.layout.history_item, parent, attachToRoot))

    override fun getViewType() = R.layout.history_item

    override fun getRecyclerItemId() = id.toString()

    companion object {
        @JvmStatic
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        @JvmStatic
        val type = object : TypeToken<List<ResultItem>>() {}.type
    }
}