package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.api.LoginResponse
import pl.szkoleniaandroid.billexpert.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = LoginViewModel(billApi)
    }
}

val Activity.billApi: BillApi
    get() = (application as App).billApi

typealias ObservableString = ObservableField<String>

class LoginViewModel(val billApi: BillApi) : BaseObservable() {
    val username = ObservableString("test")
    val password = ObservableString("pass")
    val usernameError = ObservableString("")

    var passwordError = ""
        set(value) {
            field = value
            //notifyPropertyChanged(BR.viewmodel)
        }

    fun loginClicked() {
        var valid = true
        Timber.d("Username: $username pass:$password")
        if (username.get()!!.isEmpty()) {
            usernameError.set("Can't be empty!")
            valid = false
        }
        if (password.get()!!.isEmpty()) {
            passwordError = "cant be empty"
            valid = false
        }
        if (valid) {
            val call: Call<LoginResponse> = billApi.getLogin(username.get()!!, password.get()!!)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Timber.e(t)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    Timber.d(response.toString())
                }

            })
        }

    }
}