package me.ameriod.trivia.ui.result

import io.reactivex.Observable
import me.ameriod.lib.mvp.Mvp
import me.ameriod.trivia.api.db.Result
import me.ameriod.trivia.ui.adapter.TriviaAdapterItem

class ResultContract {

    interface View : Mvp.View {
        fun setResult(items: List<TriviaAdapterItem>)
    }

    interface Presenter : Mvp.Presenter<View> {
        fun getResult()
    }

    interface Interactor {
        fun getResult(): Observable<Result>
    }

}