package com.sugoiwada.sample.data.api.dto

import com.bluelinelabs.logansquare.annotation.JsonObject
import com.sugoiwada.sample.model.github.GitHubLinkHeader

@JsonObject(fieldNamingPolicy = JsonObject.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
        fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
class GitHubResponse(var totalCount: Int = 0, var items: List<Item> = emptyList()){
    var gitHubLinkHeader: GitHubLinkHeader? = null
}

@JsonObject(fieldNamingPolicy = JsonObject.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
        fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
class Item(var login: String = "", var avatarUrl: String = "", var url: String = "")
