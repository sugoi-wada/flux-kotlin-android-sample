package com.sugoiwada.sample.model.github

import android.net.Uri

class GitHubLinkHeader(val first: Element?, val last: Element?, val next: Element?, val prev: Element?) {

    companion object {
        fun parse(string: String): GitHubLinkHeader? {
            val elements = string.split(",").map { Element.parse(it) }

            val first = elements.find { it?.rel == "first" }
            val last = elements.find { it?.rel == "last" }
            val next = elements.find { it?.rel == "next" }
            val prev = elements.find { it?.rel == "prev" }

            if (first == null && last == null && next == null && prev == null) {
                return null
            }

            return GitHubLinkHeader(first, last, next, prev)
        }
    }
}

class Element(val rel: String, val uri: String, val page: Int) {

    companion object {
        fun parse(string: String): Element? {
            val attr = string.split("; ")
            if (attr.count() != 2) {
                return null
            }

            fun trimString(string: String): String? {
                if (string.count() < 3) {
                    return null
                }
                return string.drop(1).dropLast(1)
            }

            fun value(field: String): String? {
                val pair = field.split("=")
                if (pair.count() != 2) {
                    return null
                }
                return pair.last().drop(1).dropLast(1)
            }

            fun parseInt(string: String): Int? {
                try {
                    return string.toInt()
                } catch (e: NumberFormatException) {
                    return null
                }
            }

            val uri = trimString(attr.first()) ?: return null
            val rel = value(attr.last()) ?: return null
            val page = parseInt(Uri.parse(uri).getQueryParameter("page")) ?: return null

            return Element(rel, uri, page)
        }
    }
}