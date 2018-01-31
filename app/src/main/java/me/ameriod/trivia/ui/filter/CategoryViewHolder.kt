package me.ameriod.trivia.ui.filter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.category_item.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category

class CategoryViewHolder(itemView: View,
                         private val clickListener: CategoryAdapter.OnItemClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

    fun bind(category: Category, selected: Boolean) {
        itemView.categoryTv.text = category.name
        if (selected) {
            itemView.categoryTv.setTextColor(Color.WHITE)
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
        } else {
            itemView.categoryTv.setTextColor(Color.BLACK)
            itemView.setBackgroundColor(Color.TRANSPARENT)
        }
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        clickListener.onItemClicked(v, adapterPosition)
    }
}