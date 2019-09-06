package me.ameriod.trivia.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.history_controller.view.*
import me.ameriod.lib.mvp.view.MvpController
import me.ameriod.trivia.R
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.di.get
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.adapter.TriviaBaseViewHolder
import me.ameriod.trivia.ui.result.ResultController

class HistoryController(args: Bundle) : MvpController<HistoryContract.View, HistoryContract.Presenter>(args), HistoryContract.View, TriviaBaseAdapter.OnItemClickListener {

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TriviaBaseAdapter<Result>(activity!!, this)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.history_controller, container, false)
        v.historyRecycler.layoutManager = LinearLayoutManager(v.context)
        v.historyRecycler.adapter = adapter
        v.historyRecycler.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        return v
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        getPresenter().getHistory()
    }

    override fun setHistory(items: List<Result>) {
        adapter.setItems(items)
    }

    override fun onItemClicked(vh: TriviaBaseViewHolder<*>, position: Int) {
        val result = adapter.getItem(position)
        router.pushController(RouterTransaction.with(ResultController.newInstance(result.id!!, false)))
    }

    override fun createPresenter(): HistoryContract.Presenter = get()

    override fun displayError(error: String) {
        // no op
    }

    override fun showProgress(show: Boolean) {
        // no op
    }

    companion object {
        fun newInstance() = HistoryController(Bundle.EMPTY)
    }
}