package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.api.LoginResponse
import pl.szkoleniaandroid.billexpert.databinding.ActivityLoginBinding
import pl.szkoleniaandroid.billexpert.repository.SessionRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = LoginViewModel(billApi, object : StringProvider {
            override fun getString(res: Int, vararg formatArgs: Any): String {
                return this@LoginActivity.getString(res, *formatArgs)
            }
        }, sessionRepository)

        binding.viewmodel = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.view = this
    }

    override fun onStop() {
        viewModel.view = null
        super.onStop()
    }

    override fun goToBills() {
        val intent = Intent(this, BillsActivity::class.java)
        startActivity(intent)
        finish()
    }

}

interface StringProvider {
    fun getString(res: Int, vararg formatArgs: Any): String
}

interface LoginView {
    fun goToBills()
}


val Activity.billApi: BillApi
    get() = (application as App).billApi

typealias ObservableString = ObservableField<String>

class LoginViewModel(
        val billApi: BillApi,
        val stringProvider: StringProvider,
        val sessionRepository: SessionRepository
) : BaseObservable() {
    val username = ObservableString("test")
    val password = ObservableString("pass")
    val usernameError = ObservableString("")

    var passwordError = ""
        set(value) {
            field = value
            //notifyPropertyChanged(BR.viewmodel)
        }

    var view: LoginView? = null

    fun loginClicked() {
        var valid = true
        Timber.d("Username: $username pass:$password")
        if (username.get()!!.isEmpty()) {
            usernameError.set(stringProvider.getString(R.string.cant_be_empty, "Username"))
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
                    if (response.isSuccessful) {
                        val loginResponse = response.body()!!
                        sessionRepository.saveToken(loginResponse.sessionToken)
                        sessionRepository.saveUserId(loginResponse.objectId)


                        view?.goToBills()
                    } else {
                        val error = response.errorBody()
                    }
                }

            })
        }

    }
}