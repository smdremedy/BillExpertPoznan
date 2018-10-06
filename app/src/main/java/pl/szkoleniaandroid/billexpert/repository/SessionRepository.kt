package pl.szkoleniaandroid.billexpert.repository

import android.content.SharedPreferences

interface SessionRepository {
    fun saveToken(token: String)
    fun saveUserId(userId: String)
    fun getToken(): String
    fun getUserId(): String
}


class SharedPreferencesSessionRepository(private val sharedPreferences: SharedPreferences) : SessionRepository {
    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    override fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID_KEY, userId).apply()
    }

    override fun getToken() = sharedPreferences.getString(TOKEN_KEY, "")!!

    override fun getUserId() = sharedPreferences.getString(USER_ID_KEY, "")!!

    companion object {
        const val TOKEN_KEY = "token"
        const val USER_ID_KEY = "user_id"
    }

}