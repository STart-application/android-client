package com.start.STart.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.start.STart.R
import com.start.STart.databinding.ActivityHomeBinding
import com.start.STart.ui.home.event.EventActivity
import com.start.STart.ui.home.festival.FestivalActivity
import com.start.STart.ui.home.info.InfoActivity
import com.start.STart.ui.home.rent.RentActivity
import com.start.STart.ui.home.setting.SettingActivity
import com.start.STart.ui.theme.DreamTheme
import com.start.STart.ui.theme.shadow

class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val sliderAdapter by lazy { SliderAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = sliderAdapter.apply {
            // TODO: 배너 받아오는 API 추가하고 불러오기
            list = listOf(
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
            )
        }

        TabLayoutMediator(binding.indicator, binding.slider) { _, _ ->
        }.attach()

        binding.composeMenu.setContent {
            MenuLayout()
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = resources.getString(R.string.seoultech_council)
        binding.toolbar.icBack.visibility = View.INVISIBLE
        binding.toolbar.icSetting.visibility = View.VISIBLE
        binding.toolbar.icSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    @Composable
    fun MenuLayout() {
        DreamTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
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
                        // TODO: 액티비티 이동 추가
                    }
                    MenuItem(title = "자취회비\n납부 확인", drawable = R.drawable.ic_home_menu_3, topEndRadius = 20.dp,) {
                        // TODO: 액티비티 이동 추가
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MenuItem(title = "상시사업 예약", drawable = R.drawable.ic_home_menu_4, bottomStartRadius = 20.dp) {
                        startActivity(Intent(applicationContext, RentActivity::class.java))
                    }
                    MenuItem(title = "어의 대동제", drawable = R.drawable.ic_home_menu_5) {
                        startActivity(Intent(applicationContext, FestivalActivity::class.java))
                    }
                    MenuItem(title = "이벤트 참여", drawable = R.drawable.ic_home_menu_6, bottomEndRadius = 20.dp) {
                        startActivity(Intent(applicationContext, EventActivity::class.java))
                    }
                }
            }
        }
    }

    @Composable
    fun MenuItem(
        title: String,
        drawable: Int,
        topStartRadius: Dp = 0.dp,
        topEndRadius: Dp = 0.dp,
        bottomEndRadius: Dp = 0.dp,
        bottomStartRadius: Dp = 0.dp,
        onClick: () -> Unit
    ) {
        ConstraintLayout(
            modifier = Modifier
                .shadow(
                    color = Color(0f, 0f, 0f, 0.25f),
                    topStartRadius,
                    topEndRadius,
                    bottomEndRadius,
                    bottomStartRadius,
                    spread = 0f.dp,
                    blurRadius = 4f.dp
                )
                .clip(RoundedCornerShape(topStartRadius, topEndRadius, bottomEndRadius, bottomStartRadius))
                .background(Color.White)
                .clickable { onClick() }
                .size(100.dp),
        ) {
            val (menuRef, titleRef) = createRefs()
            Image(
                painterResource(drawable),
                "",
                modifier = Modifier
                    .size(25.dp)
                    .constrainAs(menuRef) {
                        bottom.linkTo(titleRef.top, margin = 9.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top, margin = 60.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }

    @Preview
    @Composable
    fun MenuItemPreview() {
        MenuLayout()
    }
}