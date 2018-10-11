package pl.szkoleniaandroid.billexpert.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BillDto::class), version = 1)
abstract class BillDatabase : RoomDatabase() {

    abstract fun getBillDao(): BillDao

}