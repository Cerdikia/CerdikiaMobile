package com.fhanafi.cerdikia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.fhanafi.cerdikia.databinding.ActivityMainBinding
import com.fhanafi.cerdikia.ui.components.BottomNavItem
import com.fhanafi.cerdikia.ui.components.CustomBottomNavigationBar
import com.fhanafi.cerdikia.ui.components.PlayerStatusBar
import com.fhanafi.cerdikia.ui.components.theme.CerdikiaTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_rangking, R.id.navigation_shop
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)

        setupBottomNavigationBar()
        setupTopBar()
    }

    private fun setupBottomNavigationBar() {
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

    private fun setupTopBar() {
        val composeTopBar = findViewById<ComposeView>(R.id.compose_top_bar)
        composeTopBar.setContent {
            val mainViewModel : MainViewModel = viewModel() // Corrected variable name
            val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
            val currentDestination = navBackStackEntry?.destination

            val currentFlag by mainViewModel.playerFlag.collectAsState() // Use mainViewModel
            val currentGems by mainViewModel.playerGems.collectAsState() // Use mainViewModel
            val currentEnergy by mainViewModel.playerEnergy.collectAsState() // Use mainViewModel

            CerdikiaTheme {
                val statusBarInsets = WindowInsets.statusBars.asPaddingValues()
                Column(modifier = Modifier.fillMaxWidth().padding(statusBarInsets)) {
                    when (currentDestination?.id) {
                        R.id.navigation_home, R.id.navigation_shop -> {
                            PlayerStatusBar(
                                flagResourceId = mainViewModel.playerFlag, // Use mainViewModel
                                gemImageResourceId = R.drawable.ic_gems,
                                gemCount = mainViewModel.playerGems, // Use mainViewModel
                                energyImageResourceId = R.drawable.ic_lighting,
                                energyCount = mainViewModel.playerEnergy // Use mainViewModel
                            )
                        }
                        else -> {
                            Spacer(modifier = Modifier.height(0.dp))
                        }
                    }
                }
            }
        }
    }
}