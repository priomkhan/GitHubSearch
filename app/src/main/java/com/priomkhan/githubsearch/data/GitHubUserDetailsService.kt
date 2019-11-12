package com.priomkhan.githubsearch.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubUserDetailsService {


    @GET("/users/{userName}")
    suspend fun getUserDetailsData(@Path("userName") userName: String? ): Response<UserDetails>
}