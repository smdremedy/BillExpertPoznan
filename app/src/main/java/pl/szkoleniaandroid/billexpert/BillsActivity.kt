package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_bills.*

class BillsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sessionRepository.getToken().isEmpty()) {
            goToLogin()
            return
        }
        setContentView(R.layout.activity_bills)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bills, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                startActivityForResult(Intent(this, BillDetailsActivity::class.java), REQUEST_ADD)
                return true
            }
            R.id.action_refresh -> return true
            R.id.action_logout -> {
                sessionRepository.saveToken("")
                sessionRepository.saveUserId("")
                goToLogin()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD) {

            if(resultCode == Activity.RESULT_OK) {

                val fragment: BillsActivityFragment = supportFragmentManager.findFragmentById(R.id.fragment) as BillsActivityFragment
                fragment.viewModel.loadBills()
            } else {
                Toast.makeText(this, "Nothing added!", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val REQUEST_ADD = 1
    }

}
