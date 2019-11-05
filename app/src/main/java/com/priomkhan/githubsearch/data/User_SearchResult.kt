package com.priomkhan.githubsearch.data
/*
Step:1
Here we define our user data class that we get from the search result of github api
 */
data class User_SearchResult (
    val login: String,
    val url: String,
    val avatar_url: String,
    val repos_url: String
)
