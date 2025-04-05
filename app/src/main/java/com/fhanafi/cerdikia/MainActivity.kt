package com.fhanafi.cerdikia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.fhanafi.cerdikia.databinding.ActivityMainBinding
import com.fhanafi.cerdikia.ui.components.BottomNavItem
import com.fhanafi.cerdikia.ui.components.CustomBottomNavigationBar
import com.fhanafi.cerdikia.ui.components.theme.CerdikiaTheme


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_rangking, R.id.navigation_shop
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
        val composeBottomNav = findViewById<ComposeView>(R.id.compose_bottom_nav)
        composeBottomNav.setContent {
            CerdikiaTheme { // Wrap your Compose content with your theme
                var selectedRoute by remember { mutableStateOf(R.id.navigation_home) }

                val items = remember(selectedRoute) {
                    listOf(
                        BottomNavItem(
                            "Home",
                            R.drawable.ic_homebotnav,
                            R.id.navigation_home,
                            selectedRoute == R.id.navigation_home
                        ),
                        BottomNavItem(
                            "Rangking",
                            R.drawable.ic_rangkingbotnav,
                            R.id.navigation_rangking,
                            selectedRoute == R.id.navigation_rangking
                        ),
                        BottomNavItem(
                            "Shop",
                            R.drawable.ic_shopbotnav,
                            R.id.navigation_shop,
                            selectedRoute == R.id.navigation_shop
                        )
                    )
                }

                CustomBottomNavigationBar(
                    items = items,
                    onItemClick = { item ->
                        selectedRoute = item.route
                        navController.navigate(item.route)
                    }
                )
            }
        }
    }
}