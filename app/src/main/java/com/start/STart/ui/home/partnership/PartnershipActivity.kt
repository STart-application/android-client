package com.start.STart.ui.home.partnership

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.start.STart.R
import com.start.STart.api.partner.response.Partner
import com.start.STart.databinding.ActivityPartnershipBinding
import com.start.STart.databinding.BottomsheetPartnerCategoryBinding
import com.start.STart.databinding.BottomsheetPartnerInfoBinding
import com.start.STart.databinding.ItemPartnerCategoryBinding
import com.start.STart.util.dp2px
import com.start.STart.util.getBitmapFromVectorDrawable

class PartnershipActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy { ActivityPartnershipBinding.inflate(layoutInflater) }
    private val viewModel: PartnerViewModel by viewModels()

    private lateinit var partnerInfoBinding: BottomsheetPartnerInfoBinding
    private val partnerInfoBottomSheet by lazy { BottomSheetDialog(this) }

    private lateinit var partnerCategoryBinding: BottomsheetPartnerCategoryBinding
    private val categoryMap = hashMapOf<PartnerCategory, ItemPartnerCategoryBinding>()
    private val partnerCategoryBottomSheet by lazy { BottomSheetDialog(this) }

    private lateinit var googleMap: GoogleMap
    private val markers = mutableListOf<Marker?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        (supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment)
            .getMapAsync(this)

        binding.toolbar.textTitle.text = "제휴사업"
        binding.toolbar.btnBack.setOnClickListener { finish() }

        val partnerListFragment = PartnerListFragment.newInstance().apply {
            setOnItemClickListener { position ->
                bindBottomSheet(adapter.list[position])
                partnerInfoBottomSheet.show()
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_list, partnerListFragment)
            .commit()

        binding.btnMenu.setOnClickListener {
            viewModel.toggleMenu()
        }

        binding.inputKeyword.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                viewModel.showMenu()
            }
        }

        binding.inputKeyword.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                binding.inputKeyword.clearFocus()
            }
            false
        }

        binding.inputKeyword.addTextChangedListener {
            val keyword = it.toString()
            viewModel.setKeyword(keyword)
        }

        onBackPressedDispatcher.addCallback {
            val isVisible = viewModel.menuVisibility.value ?: false
            if(isVisible) {
                viewModel.closeMenu()
            } else {
                finish()
            }
        }

        viewModel.menuVisibility.observe(this) { isVisible ->

            val containerVisibility = if (isVisible) View.VISIBLE else View.GONE
            binding.containerList.visibility = containerVisibility

            val iconRes = if (isVisible) R.drawable.ic_map else R.drawable.ic_menu
            binding.imageMenu.setImageResource(iconRes)

            if(!isVisible) {
                binding.inputKeyword.setText("")
                binding.inputKeyword.clearFocus()
            }

        }

        viewModel.partnerList.observe(this) {
            addMarkers(it)
        }


        partnerInfoBinding = BottomsheetPartnerInfoBinding.inflate(layoutInflater, null, false)
        partnerInfoBottomSheet.setContentView(partnerInfoBinding.root)

        partnerCategoryBinding =
            BottomsheetPartnerCategoryBinding.inflate(layoutInflater, null, false)
        partnerCategoryBottomSheet.setContentView(partnerCategoryBinding.root)
        partnerCategoryBinding.btnApply.setOnClickListener {
            partnerCategoryBottomSheet.dismiss()
        }
        partnerCategoryBinding.btnInit.setOnClickListener {
            viewModel.setCategory(PartnerCategory.all)
            partnerCategoryBottomSheet.dismiss()
        }

        initCategory()


        binding.layoutCategory.setOnClickListener {
            partnerCategoryBottomSheet.show()
        }


        val purpleColor = ContextCompat.getColor(this, R.color.dream_purple)
        val grayColor = ContextCompat.getColor(this, R.color.text_caption)
        viewModel.category.observe(this) { category ->
            categoryMap.entries.forEach { entry ->
                entry.value.let {
                    it.root.setBackgroundResource(R.drawable.background_partner_category_disabled)
                    it.imageCategory.imageTintList = ColorStateList.valueOf(grayColor)
                    it.textCategory.setTextColor(grayColor)
                }
            }

            categoryMap[category]?.let {
                it.root.setBackgroundResource(R.drawable.background_partner_category)
                it.imageCategory.imageTintList = ColorStateList.valueOf(purpleColor)
                it.textCategory.setTextColor(purpleColor)
            }

            if(category == PartnerCategory.all) {
                binding.textCategory.text = "카테고리"
                binding.textCategory.setTextColor(grayColor)
                binding.categoryDropdown.imageTintList = ColorStateList.valueOf(grayColor)
            } else {
                binding.textCategory.text = category.title
                binding.textCategory.setTextColor(purpleColor)
                binding.categoryDropdown.imageTintList = ColorStateList.valueOf(purpleColor)
            }

            markers.forEach {
                val partner = it?.tag as Partner
                it.isVisible = partner.partnerTypeId == category.id || category == PartnerCategory.all
            }
        }
    }

    private fun initCategory() {
        val grayColor = ContextCompat.getColor(this, R.color.text_caption)

        PartnerCategory.entries.forEach { category ->
            val categoryItemBinding =
                ItemPartnerCategoryBinding.inflate(layoutInflater, null, false)

            categoryItemBinding.root.setBackgroundResource(R.drawable.background_partner_category)

            if (category.tag != null) {
                categoryItemBinding.imageCategory.setImageResource(category.tag)
                categoryItemBinding.imageCategory.imageTintList = ColorStateList.valueOf(grayColor)
            } else {
                categoryItemBinding.imageCategory.visibility = View.GONE
            }
            categoryItemBinding.textCategory.text = category.title
            categoryItemBinding.textCategory.setTextColor(grayColor)

            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                setMargins(0, 0, dp2px(8f).toInt(), dp2px(10f).toInt())
            }
            partnerCategoryBinding.composeCategory.addView(categoryItemBinding.root, param)

            categoryMap[category] = categoryItemBinding

            categoryItemBinding.root.setOnClickListener {
                viewModel.setCategory(category)
            }


        }
        viewModel.setCategory(PartnerCategory.all)
    }


    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        initPosition()
        googleMap.setOnMarkerClickListener { marker ->
            val partner = marker.tag as Partner
            bindBottomSheet(partner)
            partnerInfoBottomSheet.show()
            true
        }

        viewModel.loadPartner()
    }

    private fun bindBottomSheet(partner: Partner) {

        partnerInfoBinding.also {
            it.textName.text = partner.name
            it.textAddress.text = partner.address
            it.textBenefit.text = partner.benefit
            val category = PartnerCategory.findById(partner.partnerTypeId)?.title
            it.textCategory.text = category

            it.textDescription.text = partner.description
            it.textPhone.text = partner.phoneNumber

            it.btnMap.setOnClickListener {
                startActivity(Intent(this, PartnerMapActivity::class.java).apply {
                    putExtra(PartnerMapActivity.KEY_PARTNER, partner)
                })
            }
        }
    }

    private fun initPosition() {
        val latLng = LatLng(37.6333, 127.0778)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    private fun addMarkers(list: List<Partner>) {

        list.forEach { partner ->
            val category = PartnerCategory.findById(partner.partnerTypeId)
            if (partner.coordinateX != null && partner.coordinateY != null) {
                val pos = LatLng(partner.coordinateX, partner.coordinateY)
                category?.let {
                    val bitmap = getBitmapFromVectorDrawable(this, it.marker!!)

                    val marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    )?.also { marker ->
                        marker.tag = partner
                    }

                    markers.add(marker)
                }
            }
        }
    }
}