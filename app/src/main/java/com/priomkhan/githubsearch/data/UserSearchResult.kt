package com.priomkhan.githubsearch.data

import androidx.annotation.WorkerThread
import com.priomkhan.githubsearch.GITHUB_SERVICE_URL
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.Serializable

/*
Step:1
Here we define our user data class that we get from the search result of github api
 */
data class UserSearchResult(
    @Json(name = "login") val userName: String,
    @Json(name = "url") val userUrl: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "repos_url") val reposUrl: String
): Serializable{

}
