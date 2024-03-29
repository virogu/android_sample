package com.virogu.paging3.bean

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("stargazers_count") val starCount: Int
)

class RepoResponse(
    @SerializedName("items") val items: List<Repo> = emptyList()
)