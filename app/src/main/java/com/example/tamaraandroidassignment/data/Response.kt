package com.example.tamaraandroidassignment.data

import com.google.gson.annotations.SerializedName

data class ResponseItem(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null,
)
