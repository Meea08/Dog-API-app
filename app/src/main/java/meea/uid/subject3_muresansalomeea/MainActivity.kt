@file:Suppress("DEPRECATION")

package meea.uid.subject3_muresansalomeea

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading data. Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        progressDialog.show()
        Handler().postDelayed({
            progressDialog.dismiss()
            val intent = Intent(this, ListBreedsActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }
}