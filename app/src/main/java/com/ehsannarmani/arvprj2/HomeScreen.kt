package com.ehsannarmani.arvprj2

import android.util.Range
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ehsannarmani.arvprj2.extensions.to1Decimal
import com.ehsannarmani.arvprj2.extensions.toCelsius
import com.ehsannarmani.arvprj2.models.getImageForSymbol
import com.ehsannarmani.arvprj2.viewModels.HomeViewModel
import com.ehsannarmani.arvprj2.viewModels.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {

    val scope = rememberCoroutineScope()

    val response by viewModel.response.collectAsState()
    val today = Calendar.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(), contentAlignment = Alignment.Center
    ) {
        when (val res = response) {
            is Response.Loading -> {
                CircularProgressIndicator()
            }

            is Response.Error -> {
                Text(text = res.message, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            is Response.Success -> {
                with(res.data) {
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
                            painter = painterResource(id = weatherImage),
                            contentDescription = null
                        )
                        Text(
                            text = "${temperatures.first().second.toCelsius().to1Decimal()}º",
                            fontSize = 64.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Precipitations", color = Color.White, fontSize = 18.sp)
                        Text(
                            text = "Max.: ${
                                maxTemperatures.max().to1Decimal()
                            }º      Min: ${minTemperatures.min().to1Decimal()}º",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
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
                                val prep = remember {
                                    try {
                                        ((precipitations.first() * 100) / precipitations.max()).roundToInt()
                                    }catch (e:Exception){
                                        0
                                    }
                                }
                                Text(
                                    text = "$prep%",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.sun),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${((100/uvs.max())*uvs.first())}%",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.arr_1x),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${((windSpeeds.first() * 3600) / 1000).roundToInt()} km/h",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
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
                                Text(
                                    text = "Today",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(.3f),
                                            offset = Offset(3f, 4f)
                                        )
                                    )
                                )
                                Text(
                                    text = today.getDisplayName(
                                        Calendar.MONTH, Calendar.SHORT,
                                        Locale.getDefault()
                                    ).orEmpty() + ", ${today[Calendar.DAY_OF_MONTH]}",
                                    fontSize = 20.sp,
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black.copy(.3f),
                                            offset = Offset(3f, 4f)
                                        )
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(temperatures.dropLast(1)) { index, (date, temperature) ->
                                    val isCurrent =
                                        (date?.hours ?: 0) == today[Calendar.HOUR_OF_DAY]
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .border(
                                                .5.dp,
                                                if (isCurrent) Color.White else Color.Transparent,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "${temperature.toCelsius().to1Decimal()}°C",
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                        Image(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .scale(1.7f)
                                                .size(40.dp),
                                            contentScale = ContentScale.Fit,
                                            painter = painterResource(id = getImageForSymbol(symbols[index].toString())),
                                            contentDescription = null
                                        )
                                        Text(
                                            text = date?.hours.toString() + ".00",
                                            fontSize = 16.sp,
                                            color = Color.White
                                        )
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
                                val sumImageBitmap =
                                    ImageBitmap.imageResource(id = R.drawable.yellow_sum)
                                Image(
                                    painter = painterResource(id = R.drawable.ic_sunrise),
                                    contentDescription = null
                                )
                                val sunProgress = remember {
                                    mutableDoubleStateOf(0.0)
                                }
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f)
                                ) {
                                    val sunrise = (sunrises.first()?.time ?: 0)
                                    val sunset = (sunsets.first()?.time ?: 0)
                                    val system = System.currentTimeMillis()

                                    if (sunProgress.value == 0.0) {
                                        scope.launch(Dispatchers.IO) {
                                            sunProgress.value = percentOfRange(
                                                min = sunrise,
                                                max = sunset,
                                                value = system
                                            )
                                        }
                                    }

                                    val lineSpaceFromBottom = 80
                                    val sunSpaceFromLine = 65
                                    val path = Path().apply {
                                        moveTo(0f, size.height - lineSpaceFromBottom)
                                        lineTo(
                                            size.width / 2,
                                            (size.height / 2f) - lineSpaceFromBottom
                                        )
                                        lineTo(size.width, size.height - lineSpaceFromBottom)
                                    }
                                    val sunIconPath = Path().apply {
                                        moveTo(
                                            0f,
                                            size.height - (lineSpaceFromBottom + sunSpaceFromLine)
                                        )
                                        lineTo(
                                            size.width / 2,
                                            (size.height / 1.7f) - (lineSpaceFromBottom + sunSpaceFromLine)
                                        )
                                        lineTo(
                                            size.width,
                                            size.height - (lineSpaceFromBottom + sunSpaceFromLine)
                                        )
                                    }

                                    val measure = PathMeasure()
                                    measure.setPath(sunIconPath, false)

                                    val style =
                                        Stroke(7f, pathEffect = PathEffect.cornerPathEffect(200f))

                                    val sunPosition =
                                        measure.getPosition((measure.length * sunProgress.value).toFloat())
                                    drawPath(
                                        path = path,
                                        color = Color.White,
                                        style = style
                                    )

                                    drawImage(
                                        image = sumImageBitmap,
                                        topLeft = sunPosition,
                                    )

                                    clipRect(right = (size.width * sunProgress.value).toFloat()) {
                                        drawPath(
                                            path = path,
                                            brush = Brush.horizontalGradient(
                                                listOf(
                                                    Color(0xffFFDC3A),
                                                    Color(0xffFF6B00),
                                                )
                                            ),
                                            style = style
                                        )
                                    }

                                }
                                Image(
                                    painter = painterResource(id = R.drawable.ic_sunset),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}


operator fun Offset.minus(value: Float): Offset {
    return copy(x = x - value, y = y - value)
}

fun Offset.minus(xValue: Float, yValue: Float): Offset {
    return copy(x = x - xValue, y = y - yValue)
}

fun percentOfRange(max: Long, min: Long, value: Long): Double {
    val diff = max - min
    val distance = value - min
    return distance / diff.toDouble()
}


