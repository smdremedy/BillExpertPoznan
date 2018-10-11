package pl.szkoleniaandroid.billexpert.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BillDao {

    @Insert
    fun insert(billDto: BillDto)

    @Query("SELECT * from bill ORDER BY category")
    fun getAll(): LiveData<List<BillDto>>
}