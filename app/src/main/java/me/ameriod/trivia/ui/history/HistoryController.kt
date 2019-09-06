package me.ameriod.trivia.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import io.sellmair.disposer.Disposer
import io.sellmair.disposer.disposeBy
import kotlinx.android.synthetic.main.history_controller.view.*
import me.ameriod.lib.mvp.presenter.rx2.IObservableSchedulerRx2
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.di.inject
import me.ameriod.trivia.di.viewModel
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.ResultController
import timber.log.Timber
import me.ameriod.trivia.di.MvvmController

class HistoryController(args: Bundle) : MvvmController(args),
        TriviaBaseAdapter.OnItemClickListener {

    private val viewModel: HistoryViewModel by viewModel()
    private val disposer: Disposer = Disposer.create()
    private val scheduler: IObservableSchedulerRx2 by inject()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TriviaBaseAdapter<Result>(activity!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.history_controller, container, false)
                    .apply {
                        historyRecycler.layoutManager = LinearLayoutManager(context)
                        historyRecycler.adapter = adapter
                        historyRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                    }

    override fun onAttach(view: View) {
        super.onAttach(view)
         viewModel.getHistory()
                .compose(scheduler.schedule())
                .subscribe({ items ->
                    adapter.setItems(items)
                }, { throwable ->
                    Timber.e(throwable, "Error")
                })
                .disposeBy(disposer)
    }


    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val result = adapter.getItem(position)
        router.pushController(RouterTransaction.with(ResultController.newInstance(result.id!!, false)))
    }

    companion object {
        fun newInstance() = HistoryController(Bundle.EMPTY)
    }
}