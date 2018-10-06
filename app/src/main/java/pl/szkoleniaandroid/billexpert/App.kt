package pl.szkoleniaandroid.billexpert

import android.app.Application
import pl.szkoleniaandroid.billexpert.api.BillApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class App : Application() {

    lateinit var billApi: BillApi

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val retrofit = Retrofit.Builder()
                .baseUrl("https://parseapi.back4app.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        billApi = retrofit.create(BillApi::class.java)
    }
}