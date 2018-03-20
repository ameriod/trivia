package me.ameriod.trivia.ui.history

import android.view.View
import kotlinx.android.synthetic.main.history_item.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class HistoryViewHolder(view: View) : TriviaBaseViewHolder<Result>(view), View.OnClickListener {

    private val dateFormatter = SimpleDateFormat("MM/dd/yy hh:mm aaa", Locale.getDefault())

    override fun bindItem(item: Result) {
        itemView.setOnClickListener(this)
        itemView.historyItemTvResult.text = itemView.context.getString(R.string.history_result, item.correctQuestions, item.totalQuestions)
        itemView.historyItemTvDate.text = dateFormatter.format(item.date)
    }

    override fun onClick(v: View) {
        clickListener?.onItemClicked(this, adapterPosition)
    }
}