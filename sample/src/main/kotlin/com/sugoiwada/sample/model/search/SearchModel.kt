package com.sugoiwada.sample.model.search

import com.sugoiwada.sample.model.github.GitHubLinkHeader

class SearchModel<T>(var searchkey: String = "", var elements: List<T> = emptyList(), var totalCount: Int = 0, var linkHeader: GitHubLinkHeader? = null) {
    fun concat(searchModel: SearchModel<T>) {
        searchkey = searchModel.searchkey
        elements += searchModel.elements
        totalCount = searchModel.totalCount
        linkHeader = searchModel.linkHeader
    }

    val nextPage: Int?
        get() = linkHeader?.next?.page

    val totalCountText: String
        get() = "${elements.count()}/$totalCount"
}
