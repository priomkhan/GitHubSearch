package com.priomkhan.githubsearch.data

import okhttp3.OkHttpClient
import java.util.*
import android.os.AsyncTask.execute
import okhttp3.Request
import android.os.AsyncTask.execute
import com.priomkhan.githubsearch.LOG_TAG
import kotlin.math.log


class GitHubCredential() {



    fun getCredential(loginId: String, password: String): OkHttpClient{
        val authToken = "Basic " + Base64.getEncoder().encode("$loginId:$password".toByteArray()).toString(Charsets.UTF_8)
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", authToken)
                val request = builder.build()

                chain.proceed(request)
            }
            .build()



        return httpClient
    }


}