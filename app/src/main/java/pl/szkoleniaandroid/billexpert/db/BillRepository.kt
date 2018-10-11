package pl.szkoleniaandroid.billexpert.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.szkoleniaandroid.billexpert.api.Bill

interface BillRepository {
    fun getBills(): LiveData<List<Bill>>

    fun saveBill(bill: Bill)


}

class BillRoomRepository(private val billDao: BillDao) : BillRepository {
    override fun getBills(): LiveData<List<Bill>> {
        return Transformations.map(billDao.getAll()) {
            it.map {
                Bill(
                        userId = it.userId,
                        date = it.date,
                        name = it.name,
                        amount = it.amount,
                        category = it.category,
                        comment = it.comment,
                        objectId = it.objectId
                )
            }
        }
    }

    override fun saveBill(bill: Bill) {
        billDao.insert(BillDto().apply {
            userId = bill.userId
            date = bill.date
            name = bill.name
            amount = bill.amount
            category = bill.category
            comment = bill.comment
            objectId = bill.objectId
        })
    }

}