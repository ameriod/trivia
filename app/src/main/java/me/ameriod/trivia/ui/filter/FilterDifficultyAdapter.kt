package me.ameriod.trivia.ui.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Difficulty

class FilterDifficultyAdapter(context: Context,
                              private var items: List<Difficulty> = listOf()) : BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            view = layoutInflater.inflate(R.layout.filter_diffuculty_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.tv.text = getItem(position).display

        return view!!
    }

    override fun getItem(position: Int): Difficulty = items[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = items.size

    fun getPositionForItem(item: Difficulty): Int {
        val index = items.lastIndexOf(item)
        return if (index < 0) 0 else index
    }

    fun setItems(items: List<Difficulty>) {
        this.items = items
        notifyDataSetChanged()
    }

    private inner class ViewHolder(view: View) {
        val tv: TextView = view as TextView
    }
}