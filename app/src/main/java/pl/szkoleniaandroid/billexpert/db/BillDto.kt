package pl.szkoleniaandroid.billexpert.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import pl.szkoleniaandroid.billexpert.api.Category
import java.util.*

@Entity(tableName = "bill")
@TypeConverters(Converters::class)
class BillDto {
    @PrimaryKey
    var objectId: String = ""
    var userId: String = ""
    var date: Date = Date()
    var name: String = ""
    var amount: Double = 0.0
    var category: Category = Category.OTHER
    var comment: String = ""
}


class Converters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun fromCategory(category: Category): Int = category.ordinal

    @TypeConverter
    fun toCategory(index: Int): Category = Category.values()[index]
}