package pl.szkoleniaandroid.billexpert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
import pl.szkoleniaandroid.billexpert.api.Bill
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.api.Category
import pl.szkoleniaandroid.billexpert.api.PostBillResponse
import pl.szkoleniaandroid.billexpert.databinding.ActivityBillDetailsBinding
import pl.szkoleniaandroid.billexpert.repository.SessionRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BillDetailsActivity : AppCompatActivity(), BillDetailsView {
    override fun saved() {
        finish()
    }

    lateinit var binding: ActivityBillDetailsBinding
    lateinit var viewModel: BillDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_details)
        viewModel = BillDetailsViewModel(billApi, sessionRepository)
        binding.viewmodel = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.view = this
    }

    override fun onStop() {
        super.onStop()
        viewModel.view = null
    }
}

interface BillDetailsView {
    fun saved()
}

class BillDetailsViewModel(private val billApi: BillApi, private val sessionRepository: SessionRepository) {
    val date = Date()
    val name = ObservableString("")
    val amount = ObservableString("0.0")
    val comment = ObservableString("")

    var view: BillDetailsView? = null

    fun saveClicked() {
        val bill = Bill(
                userId = sessionRepository.getUserId(),
                date = date,
                name = name.get()!!,
                amount = amount.get()!!.toDouble(),
                category = Category.OTHER,
                comment = comment.get()!!,
                objectId = ""
        )
        val call: Deferred<Response<PostBillResponse>> = billApi.postBill(bill)
        GlobalScope.launch(Dispatchers.Main) {
            val response = call.await()
            if (response.isSuccessful) {
                view?.saved()
            }
        }


    }
}
