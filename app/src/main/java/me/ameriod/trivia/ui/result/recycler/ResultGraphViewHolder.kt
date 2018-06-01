package me.ameriod.trivia.ui.result.recycler

import androidx.core.content.ContextCompat
import android.view.View
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.result_item_graph.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class ResultGraphViewHolder(view: View) : TriviaBaseViewHolder<ResultGraphItem>(view) {

    private val context = itemView.context

    override fun bindItem(item: ResultGraphItem) {
        itemView.resultItemTvResults.text = context.getString(R.string.result_title, item.correct.toString(), item.total.toString())

        itemView.resultItemPieChart.description.isEnabled = false
        itemView.resultItemPieChart.legend.isEnabled = false

        val entries = mutableListOf<PieEntry>()

        entries.add(PieEntry(item.correct.toFloat(), ""))
        entries.add(PieEntry(item.incorrect.toFloat(), ""))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(ContextCompat.getColor(context, R.color.correct_answer),
                ContextCompat.getColor(context, R.color.incorrect_answer))

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f


        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setDrawValues(false)

        itemView.resultItemPieChart.data = data
        itemView.resultItemPieChart.invalidate()
    }
}