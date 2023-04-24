package com.start.STart.ui.home.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.start.STart.R

class Info1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(R.drawable.organization)
            .thumbnail(0.3f)
            .into(view.findViewById(R.id.organization))

        view.findViewById<ImageView>(R.id.organization).setOnClickListener {
            (requireActivity() as InfoActivity).showPhotoView()
        }
    }
}