package com.priomkhan.githubsearch.data

import com.squareup.moshi.Json
import java.io.Serializable

/*
Step:1
Here we define our user data class that we get from the search result of github api
 */
data class UserSearchResult (
    @Json(name="login") val  userName: String,
    @Json(name="url") val userUrl: String,
    @Json(name="avatar_url") val avatarUrl: String,
    @Json(name="repos_url") val reposUrl: String
): Serializable
