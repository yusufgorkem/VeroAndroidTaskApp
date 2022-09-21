package com.theappland.veroandroidtaskapp.api

import com.google.gson.Gson
import com.theappland.veroandroidtaskapp.model.OAuth
import com.theappland.veroandroidtaskapp.util.Constants.AUTHORIZATION
import com.theappland.veroandroidtaskapp.util.CustomSharedPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val refreshTokenApi: RefreshTokenApi
) : Authenticator {

    private val mediaType = "application/json".toMediaTypeOrNull()
    private val body = "{\n        \"username\":\"365\",\n        \"password\":\"1\"\n}".toRequestBody(
        mediaType
    )

    private lateinit var jsonToOAuth: OAuth
    private var customSharedPreferences = CustomSharedPreferences()

    override fun authenticate(route: Route?, response: Response): Request? {

            runBlocking {
                val result = refreshTokenApi.refreshToken(body)

                val gson = Gson()
                jsonToOAuth = gson.fromJson(result["oauth"], OAuth::class.java)

                customSharedPreferences.saveOAuth(jsonToOAuth)
            }

            return response.request.newBuilder()
                .header(AUTHORIZATION, jsonToOAuth.token_type + " " + jsonToOAuth.access_token)
                .build()
    }
}