package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.preference.PreferenceManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.repository.SessionRepository
import pl.szkoleniaandroid.billexpert.repository.SharedPreferencesSessionRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.*

const val REST_API_KEY = "mt4btJUcnmVaEJGzncHqkogm0lDM3n2185UNSjiX"

class App : Application() {

    lateinit var billApi: BillApi
    lateinit var sessionRepository: SessionRepository

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val moshi = Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()

        val client = OkHttpClient.Builder()
                .addInterceptor {
                    val builder = it.request().newBuilder()
                            .addHeader("X-Parse-Application-Id", "RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX")
                            .addHeader("X-Parse-REST-API-Key", REST_API_KEY)
                    if (sessionRepository.getToken().isNotEmpty()) {
                        builder.addHeader("X-Parse-Session-Token", sessionRepository.getToken())
                    }
                    it.proceed(builder.build())
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build()

        billApi = retrofit.create(BillApi::class.java)
        sessionRepository = SharedPreferencesSessionRepository(
                PreferenceManager.getDefaultSharedPreferences(this)
        )
    }
}

val Activity.sessionRepository: SessionRepository
    get() = (this.application as App).sessionRepository