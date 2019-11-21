package com.priomkhan.githubsearch.data

import com.squareup.moshi.Json

class UserRepo(@Json(name="name") val repoName: String?,
               val forks_count: Int,
               val stargazers_count: Int,
               val html_url: String) {

}
