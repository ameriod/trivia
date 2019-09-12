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
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.history_controller.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.mvvm.MvvmController
import me.ameriod.trivia.mvvm.viewModel
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.ResultController
import timber.log.Timber

class HistoryController(args: Bundle) : MvvmController(args),
        TriviaBaseAdapter.OnItemClickListener {

    private val viewModel: HistoryViewModel by viewModel()
    private var snackbar: Snackbar? = null

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
        subscribeIo(viewModel.getStateObservable(), Consumer { setItems((it)) })
        viewModel.getHistory()
    }

    private fun setItems(state: HistoryViewModel.State) {
        view?.apply {
            when (state) {
                is HistoryViewModel.State.Loading -> {
                    historyLoading.isVisible = state.show
                }
                is HistoryViewModel.State.Empty -> {
                    historyEmpty.isVisible = state.show
                }
                is HistoryViewModel.State.Error -> {
                    snackbar = Snackbar.make(this, state.message, Snackbar.LENGTH_INDEFINITE)
                            .setAction(state.actionText) {
                                state.action
                            }
                    snackbar?.show()
                }
                is HistoryViewModel.State.Loaded -> {
                    adapter.setItems(state.items)
                }
            }
        }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        snackbar?.show()
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val result = adapter.getItem(position)
        router.pushController(RouterTransaction.with(ResultController.newInstance(result.id!!, false)))
    }

    companion object {
        fun newInstance() = HistoryController(Bundle.EMPTY)
    }
}