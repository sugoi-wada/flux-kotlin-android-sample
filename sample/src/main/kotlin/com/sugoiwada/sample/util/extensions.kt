package com.sugoiwada.sample.util

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager

fun RecyclerView.isNearBottomEdge(edgeOffset: Int = 0): Boolean {
    val lm = (layoutManager as? LinearLayoutManager) ?: throw IllegalStateException("layoutManagerはLinearLayoutManagerを継承したものにしてね。")
    return lm.itemCount - 2 - edgeOffset < lm.findLastVisibleItemPosition()
}

fun FragmentActivity.closeKeyboard() {
    currentFocus?.let { view ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}