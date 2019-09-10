package me.ameriod.trivia.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.history_controller.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.mvvm.viewModel
import me.ameriod.trivia.mvvm.MvvmController
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.ResultController
import timber.log.Timber

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
        subscribeIo(viewModel.stateSubject, ::setItems)
        viewModel.getHistory()
    }

    private fun setItems(state: HistoryViewModel.State) {
        Timber.d("state: $state")
        view?.apply {
            when (state) {
                is HistoryViewModel.State.Loading -> {
                    historyLoading.isVisible = state.show
                    historyEmpty.isVisible = false
                }
                is HistoryViewModel.State.Empty -> {
                    historyEmpty.text = state.message
                    historyEmpty.isVisible = true
                }
                is HistoryViewModel.State.Error -> {
                    Snackbar.make(this, state.message, Snackbar.LENGTH_LONG)
                            .setAction(state.actionText) {
                                viewModel.getHistory()
                            }
                            .show()
                    historyEmpty.isVisible = false
                }
                is HistoryViewModel.State.Loaded -> {
                    adapter.setItems(state.items)
                    historyEmpty.isVisible = false
                }
            }
        }
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val result = adapter.getItem(position)
        router.pushController(RouterTransaction.with(ResultController.newInstance(result.id!!, false)))
    }

    companion object {
        fun newInstance() = HistoryController(Bundle.EMPTY)
    }
}