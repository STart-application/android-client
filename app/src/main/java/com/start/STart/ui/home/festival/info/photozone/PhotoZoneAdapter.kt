package com.start.STart.ui.home.festival.info.photozone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.start.STart.api.festival.response.PhotoZone
import com.start.STart.databinding.ItemPhotozoneBinding
import com.start.STart.ui.home.festival.info.photozone.PhotoZoneAdapter.PhotoZoneViewHolder

class PhotoZoneAdapter: RecyclerView.Adapter<PhotoZoneViewHolder>() {
    var list: List<PhotoZone> = listOf()
        set(value) {
            field = value
        }

    inner class PhotoZoneViewHolder(val binding: ItemPhotozoneBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photoZone: PhotoZone) {

            binding.location.text = photoZone.photoName
            binding.description.text = photoZone.photoDescription

            Glide.with(binding.root)
                .load(photoZone.photoImageUrl)
                .into(binding.image)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoZoneViewHolder {
        val binding= ItemPhotozoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoZoneViewHolder(
            binding
        )
    }
    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PhotoZoneViewHolder, position: Int) {
        holder.bind(list[position])
    }
}



