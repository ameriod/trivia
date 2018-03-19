package me.ameriod.trivia.ui.result

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.controller_result.view.*
import me.ameriod.lib.mvp.view.MvpController
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter

class ResultController(args: Bundle) : MvpController<ResultContract.View, ResultContract.Presenter>(args), ResultContract.View {

    val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TriviaBaseAdapter<TriviaAdapterItem>(activity!!)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_result, container, false)
        v.resultBtnDone.setOnClickListener { _ -> activity?.finish() }
        v.resultRecycler.layoutManager = LinearLayoutManager(v.context)
        v.resultRecycler.adapter = adapter
        v.resultRecycler.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        return v
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        getPresenter().getResult()
    }

    override fun setResult(items: List<TriviaAdapterItem>) {
        adapter.setItems(items)
    }

    override fun createPresenter() = ResultPresenter.newInstance(applicationContext!!, args.getLong(RESULT_ID))

    override fun displayError(error: String) {
        // no op
    }

    override fun showProgress(show: Boolean) {
        // no op
    }

    companion object {
        private const val RESULT_ID = "result_id"

        @JvmStatic
        fun newInstance(resultId: Long): ResultController {
            val args = Bundle()
            args.putLong(RESULT_ID, resultId)
            return ResultController(args)
        }
    }
}