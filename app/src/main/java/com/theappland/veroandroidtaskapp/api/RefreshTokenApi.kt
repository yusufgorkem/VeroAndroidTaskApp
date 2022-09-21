package com.theappland.veroandroidtaskapp.api

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RefreshTokenApi {

    @Headers(
        "Authorization: Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz",
        "Content-Type: application/json"
    )
    @POST("login")
    suspend fun refreshToken(@Body user: RequestBody) : JsonObject
}