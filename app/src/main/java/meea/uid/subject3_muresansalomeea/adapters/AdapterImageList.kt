package meea.uid.subject3_muresansalomeea.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import meea.uid.subject3_muresansalomeea.R
import meea.uid.subject3_muresansalomeea.manager.FavoritesManager

class AdapterImageList(
    private val breedName: String,
    private val context: Context,
    private val images: List<String>,
    private val favoritesManager: FavoritesManager,
    private val onImageLongPressListener: OnImageLongPressListener?=null) :
    BaseAdapter() {

    interface  OnImageLongPressListener{
        fun onImageLongPress(imageUrl: String)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_element_image, null)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val starLogo = view.findViewById<ImageView>(R.id.starLogo)

        Picasso.get().load(images[position]).into(imageView)

        val isFavorite = favoritesManager.isFavorite(breedName, images[position])
        starLogo.visibility = if (isFavorite) View.VISIBLE else View.GONE

        view.setOnLongClickListener{
            onImageLongPressListener?.onImageLongPress(images[position])
            true
        }
        return view
    }
}
