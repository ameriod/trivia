package me.ameriod.trivia.api.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.ameriod.trivia.ui.result.ResultItem

@Entity(tableName = "results")
data class Result(@PrimaryKey(autoGenerate = true)
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
                  var date: Long) {

    constructor() : this(null, "[]", 0, 0, 0, 0, 0)

    @Ignore
    private var items: List<ResultItem>? = null

    fun getItems(): List<ResultItem> = if (items == null) {
        items = gson.fromJson<List<ResultItem>>(jsonItems, type)
        items!!
    } else {
        items!!
    }

    companion object {
        @JvmStatic
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        @JvmStatic
        val type = object : TypeToken<List<ResultItem>>() {}.type
    }
}