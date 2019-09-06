package me.ameriod.trivia.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.history_controller.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.ui.MvvmController
import me.ameriod.trivia.di.viewModel
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.ResultController

class HistoryController(args: Bundle) : MvvmController(args),
        TriviaBaseAdapter.OnItemClickListener {

    private val viewModel: HistoryViewModel by viewModel()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TriviaBaseAdapter<Result>(activity!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.history_controller, container, false)
                    .apply {
                        historyRecycler.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = this@HistoryController.adapter
                            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                        }
                    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        subscribeIo(viewModel.getHistory(), ::setItems)
    }

    private fun setItems(items : List<Result>) {
        adapter.setItems(items)
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val result = adapter.getItem(position)
        router.pushController(RouterTransaction.with(ResultController.newInstance(result.id!!, false)))
    }

    companion object {
        fun newInstance() = HistoryController(Bundle.EMPTY)
    }
}