package com.fhanafi.cerdikia.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fhanafi.cerdikia.R

@Composable
fun CustomBottomNavigationBar(
    items: List<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
) {
    val selectedItem = remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            IconButton(
                onClick = {
                    selectedItem.value = index
                    onItemClick(item)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.label,
                    tint = Color.Unspecified
                )
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
