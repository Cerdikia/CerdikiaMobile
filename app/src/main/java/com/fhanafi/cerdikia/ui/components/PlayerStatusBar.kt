package com.fhanafi.cerdikia.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fhanafi.cerdikia.R
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerStatusBar(
    flagResourceId: Int, // Use StateFlow for dynamic updates
    gemImageResourceId: Int,
    gemCount: Int, // Use StateFlow for dynamic updates
    energyImageResourceId: Int,
    energyCount: Int, // Use StateFlow for dynamic updates
    bottomBorderColor: Color = Color(38, 137,181),
    bottomBorderSize: Dp = 1.5.dp
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Flag
//                val currentFlag by flagResourceId.collectAsState(initial = R.drawable.ic_indoflag) // Provide a default subject as english course
                Image(
                    painter = painterResource(id = flagResourceId),
                    contentDescription = "Player Flag",
                    modifier = Modifier.size(48.dp)
                )

                // Gems
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = gemImageResourceId),
                        contentDescription = "Gems",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = gemCount.toString(),
                        color = Color.White,
                        style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    )
                }

                // Energy
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = energyImageResourceId),
                        contentDescription = "Energy",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
//                    val currentEnergyCount by energyCount.collectAsState(initial = 0) // for the future me if i want to add energy element in top bar this line should be removed because it create more ambigu
                    Text(
                        text = energyCount.toString(),
                        color = Color.White,
                        style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF00A2EA)
        )
    )
    HorizontalDivider(thickness = bottomBorderSize, color = bottomBorderColor)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimmerTopBar() {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(width = 60.dp, height = 32.dp)
                            .shimmerEffect()
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF00A2EA)
        )
    )
    HorizontalDivider(thickness = 1.5.dp, color = Color(38, 137, 181))
}


@Composable
fun Modifier.shimmerEffect(): Modifier {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 1200, easing = LinearEasing))
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, translateAnim),
        end = Offset(translateAnim + 200f, translateAnim + 200f)
    )

    return this
        .background(brush, shape = RoundedCornerShape(8.dp))
}
