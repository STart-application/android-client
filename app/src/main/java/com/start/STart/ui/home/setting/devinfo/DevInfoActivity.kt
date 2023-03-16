package com.start.STart.ui.home.setting.devinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.start.STart.BuildConfig
import com.start.STart.R
import com.start.STart.databinding.ActivityDevInfoBinding
import com.start.STart.ui.theme.DreamTheme
import com.start.STart.ui.theme.pretendard
import com.start.STart.ui.theme.shadow

class DevInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDevInfoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutContent.setContent {
            DreamTheme {
                Content()
            }
        }

        initView()
        initViewListeners()
    }

    private fun initView() {
        binding.toolbar.textTitle.text = "개발 관련 정보 및 문의하기"
    }

    private fun initViewListeners() {
        binding.toolbar.icBack.setOnClickListener {
            finish()
        }
    }

    @Composable
    private fun Content() {
        ConstraintLayout {
            val guideline = createGuidelineFromTop(0.07f)
            val img = createRef()
            val developers = createRef()
            Image(painterResource(R.drawable.logo_dream), "",
                modifier = Modifier
                    .constrainAs(img) {
                        top.linkTo(guideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .width(140.dp)
                    .height(67.dp)
            )
            val box = createRef()
            Column(modifier = Modifier.constrainAs(box) {
                top.linkTo(img.bottom)
                bottom.linkTo(developers.top)
            }) {
                Box(
                    modifier = Modifier
                        .padding(20.dp, 10.dp)
                        .shadow(
                            color = Color(0f, 0f, 0f, 0.25f),
                            borderRadius = 10f.dp,
                            spread = 0f.dp,
                            blurRadius = 6f.dp
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "어플리케이션 버전", style = MaterialTheme.typography.h1)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = BuildConfig.VERSION_NAME,
                            style = TextStyle(
                                fontFamily = pretendard,
                                fontWeight = FontWeight.Bold,
                                fontSize = 48.sp
                            ),
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(20.dp, 10.dp)
                        .shadow(
                            color = Color(0f, 0f, 0f, 0.25f),
                            borderRadius = 10f.dp,
                            spread = 0f.dp,
                            blurRadius = 6f.dp
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "개발 관련 문의", style = MaterialTheme.typography.subtitle1)
                        Spacer(modifier = Modifier.height(11.dp))
                        Text(
                            text = "dev.seoultech@gmail.com",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
            Text(modifier = Modifier
                .constrainAs(developers) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("서버 ")
                    }
                    append("유서린 임새연 박준찬   ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("안드로이드 ")
                    }
                    append("채홍무 백송희\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("IOS ")
                    }
                    append("오승언 변상우   ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("디자인 ")
                    }
                    append("김태림")

                })
        }

    }

    @Preview
    @Composable
    fun Preview() {
        DreamTheme {
            Content()
        }
    }
}