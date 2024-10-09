import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import meea.uid.subject3_muresansalomeea.R

class AdapterBreedList(
    private val context: Context,
    private val breedNames: List<String>,
    private val onBreedClickListener: OnBreedClickListener) :
    RecyclerView.Adapter<AdapterBreedList.BreedViewHolder>() {

    interface OnBreedClickListener {
        fun onBreedClick(breedName: String)
    }

    inner class BreedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val breedNameTextView: TextView = itemView.findViewById(R.id.breedTextView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(breedName: String) {
            breedNameTextView.text = breedName
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val breedName = breedNames[position]
                onBreedClickListener.onBreedClick(breedName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_element_breed, parent, false)
        return BreedViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val breedName = breedNames[position]
        holder.bind(breedName)
    }

    override fun getItemCount(): Int {
        return breedNames.size
    }
}
