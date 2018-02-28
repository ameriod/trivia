package me.ameriod.trivia.ui.result

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.controller_result.view.*
import me.ameriod.trivia.R
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem
import me.ameriod.trivia.ui.adapter.TriviaBaseAdapter
import me.ameriod.trivia.ui.result.recycler.ResultGraphItem

class ResultController(args: Bundle) : Controller(args) {

    val result: Result = args.getParcelable(RESULT)

    val adapter by lazy(LazyThreadSafetyMode.NONE) {
        TriviaBaseAdapter<TriviaAdapterItem>(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.controller_result, container, false)
        v.resultBtnDone.setOnClickListener { _ -> activity?.finish() }
        v.resultRecycler.layoutManager = LinearLayoutManager(v.context)
        v.resultRecycler.adapter = adapter
        v.resultRecycler.addItemDecoration(DividerItemDecoration(v.context, DividerItemDecoration.VERTICAL))
        return v
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        val items = mutableListOf<TriviaAdapterItem>()
        items.add(ResultGraphItem(result.totalQuestions, result.correctQuestions, result.incorrectQuestions))
        items.addAll(result.items)
        adapter.setItems(items)
    }

    companion object {
        private const val RESULT = "result"

        @JvmStatic
        fun newInstance(result: Result): ResultController {
            val args = Bundle()
            args.putParcelable(RESULT, result)
            return ResultController(args)
        }
    }
}