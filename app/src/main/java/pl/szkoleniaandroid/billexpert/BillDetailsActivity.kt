package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
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
    override fun saved(bill: Bill) {
        val intent = Intent()
        intent.putExtra("bill", bill)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    lateinit var binding: ActivityBillDetailsBinding
    lateinit var viewModel: BillDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_details)
        val bill = intent.getSerializableExtra("bill") as Bill? ?: Bill(
                userId = sessionRepository.getUserId()
        )
        viewModel = BillDetailsViewModel(billApi, sessionRepository, bill)
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
    fun saved(bill: Bill)
}

class BillDetailsViewModel(private val billApi: BillApi,
                           private val sessionRepository: SessionRepository,
                           private val originalBill: Bill) {
    val date = originalBill.date
    val name = ObservableString(originalBill.name)
    val amount = ObservableString(originalBill.amount.toString())
    val comment = ObservableString(originalBill.comment)

    val categories = Category.values().toList()
    val selectedCategoryIndex = ObservableInt(originalBill.category.ordinal)

    var view: BillDetailsView? = null

    fun saveClicked() {
        val bill = Bill(
                userId = sessionRepository.getUserId(),
                date = date,
                name = name.get()!!,
                amount = amount.get()!!.toDouble(),
                category = Category.values()[selectedCategoryIndex.get()],
                comment = comment.get()!!,
                objectId = ""
        )
        val call: Deferred<Response<PostBillResponse>> = billApi.postBill(bill)
        GlobalScope.launch(Dispatchers.Main) {
            val response = call.await()
            if (response.isSuccessful) {
                view?.saved(bill)
            }
        }


    }
}
