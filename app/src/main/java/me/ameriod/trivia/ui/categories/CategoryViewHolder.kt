package me.ameriod.trivia.ui.categories

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import me.ameriod.trivia.api.response.Category

class CategoryViewHolder(itemView: View,
                         private val clickListener: CategoriesAdapter.OnItemClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

    fun bind(category: Category) {
        (itemView as TextView).text = category.name
    }

    override fun onClick(v: View) {
        clickListener.onItemClicked(v, adapterPosition)
    }
}