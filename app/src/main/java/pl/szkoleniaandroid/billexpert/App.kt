package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.repository.SessionRepository
import pl.szkoleniaandroid.billexpert.repository.SharedPreferencesSessionRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.*

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

        val retrofit = Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        billApi = retrofit.create(BillApi::class.java)
        sessionRepository = SharedPreferencesSessionRepository(
                PreferenceManager.getDefaultSharedPreferences(this)
        )
    }
}

val Activity.sessionRepository: SessionRepository
    get() = (this.application as App).sessionRepository