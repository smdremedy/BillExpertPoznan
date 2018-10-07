package pl.szkoleniaandroid.billexpert.db

import pl.szkoleniaandroid.billexpert.api.Category
import java.util.*

class BillDto {
    var userId: String = ""
    var date: Date = Date()
    var name: String = ""
    var amount: Double = 0.0
    var category: Category = Category.OTHER
    var comment: String = ""
    var objectId: String = ""
}