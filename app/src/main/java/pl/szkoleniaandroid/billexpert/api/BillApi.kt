package pl.szkoleniaandroid.billexpert.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val REST_API_KEY = "mt4btJUcnmVaEJGzncHqkogm0lDM3n2185UNSjiX"

interface BillApi {

    @Headers(
            "X-Parse-Application-Id: RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX",
            "X-Parse-REST-API-Key: $REST_API_KEY",
            "X-Parse-Revocable-Session: 1"
    )
    @GET("login")
    fun getLogin(@Query("username") username: String,
                 @Query("password") password: String): Call<LoginResponse>
}

class LoginResponse(
        val username: String,
        val createdAt: String,
        val updatedAt: String,
        val objectId: String,
        val sessionToken: String
)