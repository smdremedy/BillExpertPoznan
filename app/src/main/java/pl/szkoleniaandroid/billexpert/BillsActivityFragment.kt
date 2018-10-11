package pl.szkoleniaandroid.billexpert

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind
import pl.szkoleniaandroid.billexpert.api.Bill
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.api.Category
import pl.szkoleniaandroid.billexpert.databinding.FragmentBillsBinding
import pl.szkoleniaandroid.billexpert.db.BillRepository
import pl.szkoleniaandroid.billexpert.repository.SessionRepository
import timber.log.Timber

class BillsActivityFragment : Fragment(), BillsView {
    override fun editBill(bill: Bill) {
        val intent = Intent(activity, BillDetailsActivity::class.java)
        intent.putExtra("bill", bill)
        activity!!.startActivity(intent)
    }

    lateinit var binding: FragmentBillsBinding
    lateinit var viewModel: BillsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBillsBinding.inflate(inflater, container, false)
        viewModel = BillsViewModel(activity!!.billApi, activity!!.sessionRepository, activity!!.billRepository)
        binding.viewmodel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadBills()
    }

    override fun showBills(bills: List<Bill>) {
        // adapter.addAll(bills)
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

interface BillsView {
    fun showBills(bills: List<Bill>)
    fun editBill(bill: Bill)
}

interface OnBillClickedListener {
    fun onBillClicked(bill: BillItem)
}

class BillsViewModel(private val billApi: BillApi,
                     private val sessionRepository: SessionRepository,
                     private val billRepository: BillRepository) {

    //    val bills = ObservableArrayList<BillItem>()
//    val itemBinding: ItemBinding<BillItem> = ItemBinding.of<BillItem>(BR.item, R.layout.bill_item).apply {
//        this.bindExtra(BR.listener, object : OnBillClickedListener {
//            override fun onBillClicked(bill: BillItem) {
//                view?.editBill(bill.bill)
//            }
//
//        })
//    }
    val bills = ObservableArrayList<Item>()
    lateinit var billsLiveData: LiveData<List<Item>>
    val itemBinding: OnItemBind<Item> = OnItemBind { itemBinding, position, item ->
        when (item) {
            is CategoryItem -> itemBinding.set(BR.item, R.layout.category_item)
            is BillItem -> {
                itemBinding.set(BR.item, R.layout.bill_item)
                itemBinding.bindExtra(BR.listener, object : OnBillClickedListener {
                    override fun onBillClicked(bill: BillItem) {
                        view?.editBill(bill.bill)
                    }
                })
            }
        }
    }

    var view: BillsView? = null

    fun loadBills() {
        billsLiveData = Transformations.map(billRepository.getBills()){
            it.map { BillItem(
                    name = it.name,
                    comment = it.comment,
                    amount = it.amount,
                    categoryUrl = "file:///android_asset/${it.category.name.toLowerCase()}.png",
                    bill = it
            ) }
        }
        GlobalScope.launch(Dispatchers.Main) {
            val resposne = billApi.getBills().await()
            if (resposne.isSuccessful) {
                bills.clear()
                val billItems: Map<Category, List<Bill>> = resposne.body()!!.results.groupBy { it.category }
                billItems.mapKeys {
                    bills.add(CategoryItem(it.key.name))
                    bills.addAll(it.value.map {
                        BillItem(
                                name = it.name,
                                comment = it.comment,
                                amount = it.amount,
                                categoryUrl = "file:///android_asset/${it.category.name.toLowerCase()}.png",
                                bill = it
                        )
                    })
                }

                bills.forEach { Timber.d(it.toString()) }
            }
        }

    }
}

sealed class Item

data class BillItem(val name: String, val comment: String, val amount: Double, val categoryUrl: String, val bill: Bill) : Item()

class CategoryItem(val categoryName: String) : Item()