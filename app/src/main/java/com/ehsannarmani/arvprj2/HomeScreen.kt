package com.ehsannarmani.arvprj2

import android.graphics.Paint
import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff2CB0DD))
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.sun_smile),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "30º", fontSize = 64.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Precipitations", color = Color.White, fontSize = 18.sp)
        Text(text = "Max.: 30º      Min: 30º", color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xff104084).copy(alpha = .3f))
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.rain),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "18%", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sun),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "6%", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arr_1x),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "25 km/h", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xff104084).copy(alpha = .3f))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Today", fontSize = 20.sp, fontWeight = FontWeight.Bold, style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(.3f),
                        offset = Offset(3f, 4f)
                    )
                ))
                Text(text = "Mar, 10", fontSize = 20.sp, style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(.3f),
                        offset = Offset(3f, 4f)
                    )
                ))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(4) {
                    val current = it == 0
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(
                                .5.dp,
                                if (current) Color.White else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "31°C", color = Color.White, fontSize = 16.sp)
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .scale(2f),
                            contentScale = ContentScale.Fit,
                            painter = painterResource(id = R.drawable.ic_sum_under_cloud),
                            contentDescription = null
                        )
                        Text(text = "15.00", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xff104084).copy(alpha = .3f))
                .padding(16.dp)
        ) {
            Text(
                text = "Don’t miss sun today!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(.3f),
                        offset = Offset(3f, 4f)
                    )
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp), verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sunrise),
                    contentDescription = null
                )
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {

                    val path = Path().apply {
                        moveTo(0f, size.height - 80)
                        lineTo(size.width / 2, (size.height / 2f) - 80)
                        lineTo(size.width, size.height - 80)
                    }

                    val progressedPath = Path()
                    val measure = PathMeasure()
                    measure.setPath(path, false)
                    measure.getSegment(0f, measure.length / 1.5f, progressedPath)
//
//                    drawPath(
//                        path = path,
//                        color = Color.White,
//                        style = Stroke(7f, pathEffect = PathEffect.cornerPathEffect(200f))
//                    )

                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xffFFDC3A),
                                Color(0xffFF6B00),
                            )
                        ),
                        style = Stroke(7f, pathEffect = PathEffect.cornerPathEffect(200f))
                    )

                }
                Image(
                    painter = painterResource(id = R.drawable.ic_sunset),
                    contentDescription = null
                )
            }
        }
    }
}