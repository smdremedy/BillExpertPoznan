package pl.szkoleniaandroid.billexpert.api

import kotlinx.coroutines.experimental.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.Serializable
import java.util.*


interface BillApi {

    @Headers("X-Parse-Revocable-Session: 1")
    @GET("login")
    fun getLogin(@Query("username") username: String,
                 @Query("password") password: String): Call<LoginResponse>


    @POST("classes/Bill")
    fun postBill(@Body bill: Bill): Deferred<Response<PostBillResponse>>

    @GET("classes/Bill")
    fun getBills(): Deferred<Response<BillsResponse>>
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

data class Bill(
        val userId: String,
        val date: Date = Date(),
        val name: String = "",
        val amount: Double = 0.0,
        val category: Category = Category.OTHER,
        val comment: String = "",
        val objectId: String = ""
): Serializable

class BillsResponse(
        val results: List<Bill>
)