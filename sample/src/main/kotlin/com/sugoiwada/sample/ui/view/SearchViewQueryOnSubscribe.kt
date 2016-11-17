package com.sugoiwada.sample.ui.view

import android.support.v7.widget.SearchView
import rx.Emitter
import rx.Observable
import rx.android.MainThreadSubscription
import rx.functions.Action1

class SearchViewQueryOnSubscribe(val view: SearchView) : Action1<Emitter<Query>> {
    override fun call(emitter: Emitter<Query>) {
        MainThreadSubscription.verifyMainThread()

        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                emitter.onNext(Query(query, true))
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                emitter.onNext(Query(newText, false))
                return true
            }
        }

        view.setOnQueryTextListener(listener)

        emitter.setCancellation { view.setOnQueryTextListener(null) }
    }
}

class Query(val text: String, val submitted: Boolean)

inline fun SearchView.queryText(): Observable<Query> = Observable.fromEmitter(SearchViewQueryOnSubscribe(this), Emitter.BackpressureMode.LATEST)
