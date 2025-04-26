package com.fhanafi.cerdikia.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
    flagResourceId: StateFlow<Int>, // Use StateFlow for dynamic updates
    gemImageResourceId: Int,
    gemCount: Int, // Use StateFlow for dynamic updates
    energyImageResourceId: Int,
    energyCount: StateFlow<Int>, // Use StateFlow for dynamic updates
    bottomBorderColor: Color = Color(38, 137,181),
    bottomBorderSize: Dp = 1.5.dp
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Flag
                val currentFlag by flagResourceId.collectAsState(initial = R.drawable.ic_flag) // Provide a default subject as english course
                Image(
                    painter = painterResource(id = currentFlag),
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
//                    val currentGemCount by gemCount.collectAsState(initial = 0) // this little piece of shit maybe the problem because it was looking into MainViewModel which the user data was handle with UserViewModel that what my suspicion
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
                    val currentEnergyCount by energyCount.collectAsState(initial = 0) // for the future me if i want to add energy element in top bar this line should be removed because it create more ambigu
                    Text(
                        text = currentEnergyCount.toString(),
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
