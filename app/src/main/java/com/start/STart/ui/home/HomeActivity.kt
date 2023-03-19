package com.start.STart.ui.home

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.start.STart.R
import com.start.STart.databinding.ActivityHomeBinding
import com.start.STart.ui.theme.DreamTheme
import com.start.STart.ui.theme.shadow

class HomeActivity : AppCompatActivity() {

    companion object {
        val menuNames = listOf(
            "총학생회 설명",
            "제휴사업",
            "자치회비\n납부 확인",
            "상시사업 예약",
            "축제 이벤트",
            "이벤트 참여",
        )

        val menuDrawables = listOf(
            R.drawable.ic_home_menu_1,
            R.drawable.ic_home_menu_2,
            R.drawable.ic_home_menu_3,
            R.drawable.ic_home_menu_4,
            R.drawable.ic_home_menu_5,
            R.drawable.ic_home_menu_6,
        )
    }

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val sliderAdapter by lazy { SliderAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = sliderAdapter.apply {
            list = listOf(
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
            )
        }

        TabLayoutMediator(binding.indicator, binding.slider) { _, _ ->
        }.attach()

        binding.composeMenu.setContent {
            Menu()
        }
    }

    @Composable
    fun Menu() {
        DreamTheme {
            MenuItem(title = "총학생회 설명", drawable = R.drawable.ic_home_menu_1)
        }
    }

    @Composable
    fun MenuItem(title: String, drawable: Int) {
        ConstraintLayout(
            modifier = Modifier
                .shadow(
                    color = Color(0f, 0f, 0f, 0.25f),
                    borderRadius = 10f.dp,
                    spread = 0f.dp,
                    blurRadius = 6f.dp
                )
                .background(Color.White)
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
        DreamTheme {
            MenuItem(title = "총학생회 설명", drawable = R.drawable.ic_home_menu_1)
        }
    }
}