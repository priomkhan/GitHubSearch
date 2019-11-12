package com.priomkhan.githubsearch.data

import java.io.Serializable
import java.util.*

data class UserDetails(
                        val name : String?,
                        val email: String?,
                        val location: String?,
                        val joinDate : String?,
                        val bio : String?,
                        val followers :Int,
                        val following : Int,
                        val public_repos: Int
                       ): Serializable {

}
