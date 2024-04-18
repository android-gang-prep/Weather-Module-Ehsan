package com.ehsannarmani.arvprj2

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun BoxScope.Circle(borderWidth:Dp,scale: Float, offsetX: Dp, offsetY: Dp) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .offset(x = offsetX, y = offsetY)
            .scale(scale)
            .clip(CircleShape)
            .border(borderWidth, Color.White, CircleShape)
    )
}

@Composable
fun SplashScreen(navController: NavController) {
    val density = LocalDensity.current





    Scaffold(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val circleSize = 800f
            val path = Path().apply {
                arcTo(
                    rect = Rect(
                        center = Offset(x = (maxWidth.value*density.density)-(circleSize/3), y= ((maxHeight.value)/4f)*density.density),
                        radius = circleSize
                    ),
                    startAngleDegrees = 360f,
                    sweepAngleDegrees = -359f,
                    forceMoveTo = true
                )
            }

            val measure = remember {
                PathMeasure()
            }

            measure.setPath(path,false)
            val circlePosition = remember {
                Animatable(0f)
            }
            LaunchedEffect(circlePosition.isRunning) {
                if (!circlePosition.isRunning){
                    circlePosition.snapTo(0f)
                    circlePosition.animateTo(measure.length, animationSpec = tween(3000, easing = LinearEasing))
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xff86B9FD))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.White)
                        )
                    )
            )

            Circle(
                scale = 3f,
                offsetX = maxWidth-50.dp,
                offsetY = maxHeight/4,
                borderWidth = 1.dp
            )
            Circle(
                scale = 7f,
                offsetX = maxWidth-50.dp,
                offsetY = maxHeight/4,
                borderWidth = .3.dp
            )
            Circle(
                scale = 11f,
                offsetX = maxWidth-50.dp,
                offsetY = maxHeight/4,
                borderWidth = .05.dp
            )
            Circle(
                scale = 15f,
                offsetX = maxWidth-50.dp,
                offsetY = maxHeight/4,
                borderWidth = .02.dp
            )

            val pos = measure.getPosition(circlePosition.value)

            Image(
                painter = painterResource(id = R.drawable.half_sun),
                contentDescription = null,
                modifier=Modifier.offset(y = maxHeight/9),
            )


            Image(
                modifier=Modifier.offset { IntOffset(x = pos.x.toInt(),y = pos.y.toInt()) },
                painter = painterResource(id = R.drawable.cloud),
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 32.dp), verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Never get caught\n" +
                        "in the rain again",
                fontSize = 42.sp, style = TextStyle(lineHeight = 50.sp),
                fontWeight = FontWeight.Bold,
                color = Color(0xff494A4B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Stay ahead of the weather with our accurate forecasts",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff494A4B)
            )
            Spacer(modifier = Modifier.height(84.dp))
            Button(
                onClick = {
                    navController.navigate("home")
                }, shape = RoundedCornerShape(30), colors = ButtonDefaults.buttonColors(
                    containerColor =
                    Color(0xff0C5285)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    text = "Get started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(46.dp))
        }
    }
}