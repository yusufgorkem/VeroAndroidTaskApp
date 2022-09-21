package com.theappland.veroandroidtaskapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.theappland.veroandroidtaskapp.model.OAuth
import com.theappland.veroandroidtaskapp.util.Constants.PREFERENCES_TIME

class CustomSharedPreferences {

    companion object {

        private var sharedPreferences: SharedPreferences? = null

        @Volatile private var instance: CustomSharedPreferences? = null

        private var lock = Any()

        operator fun invoke(context: Context) : CustomSharedPreferences = instance ?: synchronized(lock) {
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context) : CustomSharedPreferences {
            sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }
    }

    fun saveTime(time: Long) {
        sharedPreferences?.edit(commit = true) {
            putLong(PREFERENCES_TIME,time)
        }
    }

    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME, 0)

    fun saveOAuth(oAuth: OAuth) {
        sharedPreferences?.edit(commit = true) {
            putString("ACCESS_TOKEN" ,oAuth.access_token)
            putString("TOKEN_TYPE" ,oAuth.token_type)
        }
    }

    fun getTokenType() : String? {
        return sharedPreferences?.getString("TOKEN_TYPE","")
    }

    fun getAccessToken() : String? {
        return sharedPreferences?.getString("ACCESS_TOKEN","")
    }
}