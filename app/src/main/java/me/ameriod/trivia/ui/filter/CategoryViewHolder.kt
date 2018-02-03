package me.ameriod.trivia.ui.filter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.category_item.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder

class CategoryViewHolder(itemView: View) :
        TriviaBaseViewHolder<Category>(itemView), View.OnClickListener {

    override fun bindItem(item: Category) {
        itemView.categoryTv.text = item.name
        if (item.selected) {
            itemView.categoryTv.setTextColor(Color.WHITE)
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorAccent))
        } else {
            itemView.categoryTv.setTextColor(Color.BLACK)
            itemView.setBackgroundColor(Color.TRANSPARENT)
        }
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        clickListener?.onItemClicked(this, adapterPosition)
    }

}