package pl.szkoleniaandroid.billexpert

import android.R
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.ObservableArrayList
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
import pl.szkoleniaandroid.billexpert.api.Bill
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.databinding.FragmentBillsBinding
import pl.szkoleniaandroid.billexpert.repository.SessionRepository
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class BillsActivityFragment : Fragment(), BillsView {

    lateinit var binding: FragmentBillsBinding
    lateinit var viewModel: BillsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBillsBinding.inflate(inflater, container, false)
        viewModel = BillsViewModel(activity!!.billApi, activity!!.sessionRepository)
        binding.viewmodel = viewModel
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
}

class BillsViewModel(private val billApi: BillApi, private val sessionRepository: SessionRepository) {

    val bills = ObservableArrayList<Bill>()
    var view: BillsView? = null

    init {
        bills.add(Bill(name = "test", userId = ""))
    }

    fun loadBills() {
        GlobalScope.launch(Dispatchers.Main) {
            val resposne = billApi.getBills().await()
            if (resposne.isSuccessful) {
                bills.addAll(resposne.body()!!.results)
                bills.forEach { Timber.d(it.toString()) }

                view?.showBills(bills)
            }
        }

    }
}
