package com.start.STart.ui.home.festival.info.photozone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.festival.response.PhotoZone
import com.start.STart.databinding.ItemPhotozoneBinding
import com.start.STart.ui.home.festival.info.photozone.PhotoZoneAdapter.PhotoZoneViewHolder

class PhotoZoneAdapter: RecyclerView.Adapter<PhotoZoneViewHolder>() {

    private lateinit var listener: OnItemClickListener

    var list: List<PhotoZone> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

        inner class PhotoZoneViewHolder(val binding: ItemPhotozoneBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoZone: PhotoZone) {

            binding.location.text = photoZone.photoName
            binding.description.text = photoZone.photoDescription

            Glide.with(binding.root)
                .load(photoZone.photoImageUrl)
                .error(R.drawable.logo_empty)
                .centerCrop()
                .into(binding.image)

            binding.root.setOnClickListener { listener.onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoZoneViewHolder {
        val binding= ItemPhotozoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoZoneViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PhotoZoneViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickListener {
        fun onClick()
    }
}



