package com.sugoiwada.sample.model.search

import com.sugoiwada.sample.model.github.GitHubLinkHeader

class SearchModel<T>(var searchkey: String = "", var elements: List<T> = emptyList(), var totalCount: Int = 0, var linkHeader: GitHubLinkHeader? = null) {
    var newElements: List<T> = elements

    fun concat(searchModel: SearchModel<T>) {
        searchkey = searchModel.searchkey
        elements += searchModel.elements
        newElements = searchModel.elements
        totalCount = searchModel.totalCount
        linkHeader = searchModel.linkHeader
    }

    val nextPage: Int?
        get() = linkHeader?.next?.page

    val totalCountText: String
        get() = "${elements.count()}/$totalCount"
}
