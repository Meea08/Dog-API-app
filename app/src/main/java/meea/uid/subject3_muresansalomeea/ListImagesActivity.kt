package meea.uid.subject3_muresansalomeea

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import meea.uid.subject3_muresansalomeea.adapters.AdapterImageList
import meea.uid.subject3_muresansalomeea.api.RetrofitClient
import meea.uid.subject3_muresansalomeea.dto.ImageResponse
import meea.uid.subject3_muresansalomeea.manager.FavoritesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.take

class ListImagesActivity : AppCompatActivity(), AdapterImageList.OnImageLongPressListener {

    private lateinit var imageListView: ListView
    private lateinit var loadMoreButton: Button
    private lateinit var favoritesManager: FavoritesManager

    private val dogApiService = RetrofitClient.instance // Assuming you have a RetrofitClient setup

    private val breedImages = mutableListOf<String>()
    private lateinit var breedName: String

    private var displayedImages = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_images)

        imageListView = findViewById(R.id.imageListView)
        loadMoreButton = findViewById(R.id.loadMoreButton)

        // Retrieve the breed name from the intent
        breedName = intent.getStringExtra("BREED_NAME") ?: ""
        favoritesManager = FavoritesManager(this)
        // Make API call to get breed images
        getBreedImages(breedName)

        // Set up Load More button click listener
        loadMoreButton.setOnClickListener {
            // Load more images if needed
            loadMoreImages()
        }

        registerForContextMenu(imageListView)
    }

    private fun getBreedImages(breed: String) {
        val callImages = dogApiService.getDogImages(breed)
        callImages.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    val imagesResponse = response.body()
                    val images = imagesResponse?.message ?: emptyList()


                    displayedImages += 6
                    // Display the first 6 images
                    if (displayedImages >= images.size) {
                        loadMoreButton.visibility = View.GONE
                    }
                    breedImages.addAll(images.take(6))

                    updateListView()
                } else {
                    // Handle the error
                    Log.e(
                        "meea.uid.subject3_muresansalomeea.ListImagesActivity",
                        "Error: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                // Handle the failure
                Log.e(
                    "meea.uid.subject3_muresansalomeea.ListImagesActivity",
                    "Failed to make API call: ${t.message}"
                )
            }
        })
    }

    private fun loadMoreImages() {
        val callImages = dogApiService.getDogImages(breedName)
        callImages.enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.isSuccessful) {
                    val imagesResponse = response.body()
                    val images = imagesResponse?.message ?: emptyList()

                    // Display the first 6 images
                    displayedImages += 6
                    if (displayedImages >= images.size) {
                        loadMoreButton.visibility = View.GONE
                    } else {
                        breedImages.addAll(images.take(displayedImages))
                        updateListView()
                    }

                } else {
                    // Handle the error
                    Log.e(
                        "meea.uid.subject3_muresansalomeea.ListImagesActivity",
                        "Error: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                // Handle the failure
                Log.e(
                    "meea.uid.subject3_muresansalomeea.ListImagesActivity",
                    "Failed to make API call: ${t.message}"
                )
            }
        })
    }

    private fun updateListView() {
        // Update the ListView with the current breedImages list
        val adapter = AdapterImageList(breedName,this, breedImages,favoritesManager,this)
        imageListView.adapter = adapter
    }

    override fun onImageLongPress(imageUrl: String) {
        Log.d("yehehehe", "long pressed on $imageUrl")
//        showContextMenu(imageUrl)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.context_menu,menu)
    }
    private fun showContextMenu(imageUrl: String) {
        val popupMenu = PopupMenu(this@ListImagesActivity, imageListView)
        popupMenu.menuInflater.inflate(R.menu.context_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.addToFavorites -> {
                    favoritesManager.addFavorite(breedName, imageUrl)
                    Log.e("list", favoritesManager.getFavorites(breedName).toString())
                    true
                }
                R.id.removeFromFavorites -> {
                    favoritesManager.removeFavorite(breedName, imageUrl)
                    Log.e("list", favoritesManager.getFavorites(breedName).toString())
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}