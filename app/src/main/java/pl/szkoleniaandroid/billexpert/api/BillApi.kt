package pl.szkoleniaandroid.billexpert.api

import retrofit2.Call
import retrofit2.http.*
import java.util.*

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

    @Headers(
            "X-Parse-Application-Id: RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX",
            "X-Parse-REST-API-Key: $REST_API_KEY"
    )
    @POST("classes/Bill")
    fun postBill(@Body bill: Bill, @Header("X-Parse-Session-Token") token: String): Call<PostBillResponse>
}

class PostBillResponse(val objectId: String)

class LoginResponse(
        val username: String,
        val createdAt: String,
        val updatedAt: String,
        val objectId: String,
        val sessionToken: String
)

enum class Category {
    OTHER,
    BILLS,
    CAR,
    CHEMISTRY,
    CLOTHES,
    COSMETICS,
    ELECTRONICS,
    ENTERTAINMENT,
    FOOD,
    FURNITURE,
    GROCERIES,
    HEALTH,
    SHOES,
    SPORT,
    TOYS,
    TRAVEL
}

class Bill(
        val userId: String,
        val date: Date = Date(),
        val name: String = "",
        val amount: Double = 0.0,
        val category: Category = Category.OTHER,
        val comment: String = "",
        val objectId: String = ""
)