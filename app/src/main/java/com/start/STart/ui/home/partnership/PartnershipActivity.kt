package com.start.STart.ui.home.partnership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.start.STart.R
import com.start.STart.databinding.ActivityPartnershipBinding

class PartnershipActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivityPartnershipBinding.inflate(layoutInflater) }
    private lateinit var googleMap: GoogleMap

    private var isListVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_list, PartnerListFragment.newInstance())
            .commit()

        binding.btnMenu.setOnClickListener {

            binding.containerList.visibility = if(isListVisible) View.GONE else View.VISIBLE
            isListVisible = !isListVisible
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }
}