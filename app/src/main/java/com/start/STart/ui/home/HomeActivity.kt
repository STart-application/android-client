package com.start.STart.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.start.STart.R
import com.start.STart.api.banner.BannerModel
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityHomeBinding
import com.start.STart.ui.home.event.EventActivity
import com.start.STart.ui.home.festival.FestivalActivity
import com.start.STart.ui.home.info.InfoActivity
import com.start.STart.ui.home.pay.PaymentActivity
import com.start.STart.ui.home.rent.home.RentHomeActivity
import com.start.STart.ui.home.setting.SettingActivity
import com.start.STart.ui.theme.DreamTheme
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity(), SliderAdapter.OnItemClickListener {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels()
    private val sliderAdapter by lazy { SliderAdapter(this) }

    private val photoView by lazy { PhotoViewDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initLiveDataObservers()

        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = sliderAdapter

        // Slider와 Indicator를 연결
        TabLayoutMediator(binding.indicator, binding.slider) { _, _ ->
        }.attach()

        // 메뉴 컴포즈로 구성
        binding.composeMenu.setContent {
            MenuLayout()
        }
    }

    override fun onClick() {
        if(!photoView.isAdded) {
            photoView.show(supportFragmentManager, ".PhotoView")
        }
    }

    fun setImage() {
        photoView.setImage(sliderAdapter.list[binding.slider.currentItem])
    }

    private fun initLiveDataObservers() {
        val replaceList = listOf(BannerModel("대체 이미지", null, 1, false, R.drawable.logo_empty))

        viewModel.loadBannerResult.observe(this) {
            if(it.isSuccessful) {
                val list = it.data as List<BannerModel>
                sliderAdapter.list = list.run {
                    ifEmpty { replaceList }
                }

            } else {
                sliderAdapter.list =  replaceList

            }
            binding.progressbarBanner.visibility = View.INVISIBLE
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = resources.getString(R.string.seoultech_council)
        binding.toolbar.btnBack.visibility = View.INVISIBLE
        binding.toolbar.btnEnd.visibility = View.VISIBLE
        binding.toolbar.btnEnd.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    @Composable
    fun MenuLayout() {
        DreamTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 33.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    MenuItem(title = "총학생회 설명", drawable = R.drawable.ic_home_menu_1, topStartRadius = 20.dp,) {
                        startActivity(Intent(applicationContext, InfoActivity::class.java))
                    }
                    MenuItem(title = "제휴사업", drawable = R.drawable.ic_home_menu_2) {
                        Toasty.info(applicationContext, "준비중입니다!").show()
                    }
                    MenuItem(title = "자치회비\n납부 확인", drawable = R.drawable.ic_home_menu_3, topEndRadius = 20.dp,) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            if(PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA) != null) {
                                withContext(Dispatchers.Main) {
                                    startActivity(Intent(applicationContext, PaymentActivity::class.java))
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toasty.info(applicationContext, "로그인이 필요합니다!").show()
                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MenuItem(title = "상시사업 예약", drawable = R.drawable.ic_home_menu_4, bottomStartRadius = 20.dp) {
                        startActivity(Intent(applicationContext, RentHomeActivity::class.java))
                    }
                    MenuItem(title = "어의 대동제", drawable = R.drawable.ic_home_menu_5) {
                        if(viewModel.festivalEnabledResult.value?.isSuccessful?:false) {
                            startActivity(Intent(applicationContext, FestivalActivity::class.java))
                        } else {
                            Toasty.info(applicationContext, "축제 기간이 아닙니다!").show()
                        }
                    }
                    MenuItem(title = "이벤트 참여", drawable = R.drawable.ic_home_menu_6, bottomEndRadius = 20.dp) {
                        startActivity(Intent(applicationContext, EventActivity::class.java))
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun MenuItemPreview() {
        MenuLayout()
    }
}