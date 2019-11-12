package com.priomkhan.githubsearch.data

import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubUserSearchService {

        //calling "/search/user?q=user"
        @GET("/search/users")
        suspend fun getUserSearchData(@Query("q") q: String?): Response<UserSearchList>


}