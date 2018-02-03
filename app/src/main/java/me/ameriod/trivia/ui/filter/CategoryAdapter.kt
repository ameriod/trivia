package me.ameriod.trivia.ui.filter

import android.content.Context
import me.ameriod.trivia.api.response.Category
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter

class CategoryAdapter(context: Context,
                      itemClickListener: OnItemClickListener) :
        TriviaBaseAdapter<Category>(context, itemClickListener)