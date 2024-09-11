package org.unizd.rma.babic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.unizd.rma.babic.R
import org.unizd.rma.babic.models.State
import java.text.SimpleDateFormat
import java.util.Locale

class StateViewAdapter(
    private val deleteUpdateCallBack: (type: String, Position: Int, state: State) -> Unit
) : RecyclerView.Adapter<StateViewAdapter.ViewHolder>() {

    private val stateList = arrayListOf<State>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleText)
        val descText: TextView = itemView.findViewById(R.id.descText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val surface: TextView = itemView.findViewById(R.id.surface)
        val delImg: ImageView = itemView.findViewById(R.id.deleteImg)
        val editImg: ImageView = itemView.findViewById(R.id.editImg)
        val flagImage: ImageView = itemView.findViewById(R.id.flagImage)
        val countryText: TextView = itemView.findViewById(R.id.country)
    }

    fun addAllState(newStateList: List<State>) {
        stateList.clear()
        stateList.addAll(newStateList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_state_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state = stateList[position]

        holder.titleTxt.text = state.title
        holder.descText.text = state.description
        holder.surface.text = state.surface
        holder.countryText.text = state.country

//        // Load flag image using Glide library
//        Glide.with(holder.itemView.context)
//            .load(state.flagurl)
//            .into(holder.flagImage)



        // Load image from imageUri using Glide library
        Glide.with(holder.itemView.context)
            .load(state.imageUri)
            .into(holder.flagImage)

        holder.delImg.setOnClickListener {
            if (holder.adapterPosition != -1) {
                deleteUpdateCallBack("delete",holder.adapterPosition, state)
            }
        }

        holder.editImg.setOnClickListener {
            if (holder.adapterPosition != -1) {
                deleteUpdateCallBack("update",holder.adapterPosition, stateList[position])
            }
        }


        val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.dateText.text = dateFormat.format(state.date)
    }

    override fun getItemCount(): Int {
        return stateList.size
    }
}
