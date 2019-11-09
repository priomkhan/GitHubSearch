package com.priomkhan.githubsearch.data

import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface GitHubUserSearchService {


        @GET("/search/users?q=priom")
        suspend fun getUserSearchData(): Response<UserSearchList>
        //suspend fun getUserSearchData(): Callback<ArrayList<UserSearchResult>>
}