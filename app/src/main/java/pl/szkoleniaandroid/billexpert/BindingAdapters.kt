package pl.szkoleniaandroid.billexpert

import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.ObservableField
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("dateToDisplay")
fun bindDate(textView: TextView, date: Date) {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    textView.text = format.format(date)
}
