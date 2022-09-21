package com.theappland.veroandroidtaskapp.api

import com.google.gson.JsonArray
import com.theappland.veroandroidtaskapp.util.Constants
import retrofit2.http.*

interface TaskApi {

    @GET(Constants.END_POINT)
    suspend fun getTasks() : JsonArray
}

