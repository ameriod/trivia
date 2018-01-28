package me.ameriod.trivia.ui.categories

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_categories.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.response.Category

class CategoriesController(args: Bundle) : Controller(args), CategoriesAdapter.OnItemClickListener {


    private val adapter: CategoriesAdapter by lazy {
        CategoriesAdapter(activity!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_categories, container, false)
        v.categoriesRecycler.layoutManager = LinearLayoutManager(v.context)
        v.categoriesRecycler.addItemDecoration(DividerItemDecoration(v.context,
                DividerItemDecoration.VERTICAL))
        v.categoriesRecycler.adapter = adapter
        return v
    }

    override fun onItemClicked(view: View, position: Int) {
        val category = adapter.getItem(position)
        // TODO send the item back to the adapter
    }

    companion object {

        @JvmStatic
        private fun newInstance(selected: Category) = CategoriesController(Bundle.EMPTY)
    }
}