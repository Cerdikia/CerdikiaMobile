package com.fhanafi.cerdikia.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fhanafi.cerdikia.R

@Composable
fun CustomBottomNavigationBar(
    items: List<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
) {
    val selectedItemIndex = remember { mutableStateOf(items.indexOfFirst { it.isSelected }) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
    ) {
        items.forEachIndexed { index, item ->
            Box( // Use Box to overlap icon and indicator
                contentAlignment = Alignment.Center, // Center content within the Box
                modifier = Modifier
                    .weight(1f)
            ) {
                IconButton(
                    onClick = {
                        onItemClick(item.copy(isSelected = true))
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        tint = Color.Unspecified
                    )
                }
                if (item.isSelected) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_selected), // Replace with your drawable ID
                        contentDescription = "Selected Indicator",
                        modifier = Modifier
                            .size(52.dp) // Adjust size to be smaller than the icon
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BottomNavigationBarPreview() {
//    CustomBottomNavigationBar(
//        items = listOf(
//            BottomNavItem("Home", R.drawable.ic_homebotnav, "home"),
//            BottomNavItem("Shield", R.drawable.ic_rangkingbotnav, "shield"),
//            BottomNavItem("Treasure", R.drawable.ic_shopbotnav, "treasure")
//        ),
//        onItemClick = {} // Add an empty lambda for the preview
//    )
//}
