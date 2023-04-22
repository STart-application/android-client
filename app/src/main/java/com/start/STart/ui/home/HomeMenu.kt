package com.start.STart.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.start.STart.ui.theme.shadow

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
            .clip(
                RoundedCornerShape(
                    topStartRadius,
                    topEndRadius,
                    bottomEndRadius,
                    bottomStartRadius
                )
            )
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