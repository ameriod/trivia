package me.ameriod.trivia.ui.result

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.recycler.ResultItemViewHolder

@Parcelize
data class ResultItem(@Expose
                      @SerializedName("question")
                      val questionText: String,
                      @Expose
                      @SerializedName("answer")
                      val selectedAnswer: String,
                      @Expose
                      @SerializedName("correctAnswers")
                      private val correctAnswersList: List<String>,
                      @Expose
                      @SerializedName("correct")
                      val isCorrect: Boolean) : Parcelable, TriviaAdapterItem {

    val correctAnswers: String
        get() {
            var display = ""
            correctAnswersList.map { display = it + "\n" }
            return display.trim()
        }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup?, attachToRoot: Boolean): TriviaBaseViewHolder<*> =
            ResultItemViewHolder(inflater.inflate(R.layout.result_item, parent, false))

    override fun getViewType() = R.layout.result_item

    override fun getRecyclerItemId() = questionText

}
