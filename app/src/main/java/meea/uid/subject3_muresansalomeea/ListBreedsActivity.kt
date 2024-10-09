@file:Suppress("DEPRECATION")

package meea.uid.subject3_muresansalomeea

import AdapterBreedList
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import meea.uid.subject3_muresansalomeea.api.RetrofitClient
import meea.uid.subject3_muresansalomeea.dto.BreedResponse
import meea.uid.subject3_muresansalomeea.manager.FavoritesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListBreedsActivity : AppCompatActivity(), AdapterBreedList.OnBreedClickListener {

    private lateinit var breedRecyclerViewRef: RecyclerView
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_breeds)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading images. Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)

        breedRecyclerViewRef = findViewById(R.id.breedListRecyclerView)

        val dogApiService = RetrofitClient.instance
        val callBreeds = dogApiService.getDogBreeds()

        callBreeds.enqueue(object : Callback<BreedResponse>{
            override fun onResponse(call: Call<BreedResponse>, response: Response<BreedResponse>) {
                if (response.isSuccessful){
                    val breedsResponse = response.body()
                    val breedsMap = breedsResponse?.message

                    // extract the breed names from the map
                    val breedNames = breedsMap?.keys?.toList() ?: emptyList()

                    // set up recycler view and adapter
                    val adapter = AdapterBreedList(this@ListBreedsActivity, breedNames, this@ListBreedsActivity)
                    breedRecyclerViewRef.layoutManager = LinearLayoutManager(this@ListBreedsActivity)
                    breedRecyclerViewRef.adapter = adapter
                }else{
                    Log.e("ListBreedsActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BreedResponse>, t: Throwable) {
                Log.e("ListBreedsActivity", "Failed to make API call: ${t.message}")
            }
        })
    }

    override fun onBreedClick(breedName: String) {
        // Show ProgressDialog
        progressDialog.show()

        Handler().postDelayed({
            // Dismiss ProgressDialog
            progressDialog.dismiss()

            val intent = Intent(this@ListBreedsActivity, ListImagesActivity::class.java)
            intent.putExtra("BREED_NAME", breedName)
            startActivity(intent)

        }, 1500)
    }
}