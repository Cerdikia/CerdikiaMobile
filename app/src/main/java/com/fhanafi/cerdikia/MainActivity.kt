package com.fhanafi.cerdikia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
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
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels() // Gunakan viewModels() untuk mendapatkan ViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var composeBottomNavigationView: ComposeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val pref = UserPreference.getInstance(context = this)
        val factory = UserViewModelFactory(pref)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

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
        destinationListener()
    }

    private fun setupBottomNavigationBar() {
        binding.composeBottomNav.setContent {
            CerdikiaTheme {
                BottomNavigationBarCompose(viewModel = mainViewModel, navController = navController)
            }
        }
    }

    @Composable
    fun BottomNavigationBarCompose(viewModel: MainViewModel, navController: NavController) {
        val selectedRoute by viewModel.selectedRoute.collectAsState()

        val items = remember(selectedRoute) {
            listOf(
                BottomNavItem("Home", R.drawable.ic_homebotnav, R.id.navigation_home, selectedRoute == R.id.navigation_home),
                BottomNavItem("Rangking", R.drawable.ic_rangkingbotnav, R.id.navigation_rangking, selectedRoute == R.id.navigation_rangking),
                BottomNavItem("Shop", R.drawable.ic_shopbotnav, R.id.navigation_shop, selectedRoute == R.id.navigation_shop),
            )
        }

        CustomBottomNavigationBar(
            items = items,
            onItemClick = { item ->
                viewModel.updateSelectedRoute(item.route)
                navController.navigate(item.route)
            }
        )
    }

    private fun destinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> mainViewModel.updateSelectedRoute(R.id.navigation_home)
                R.id.navigation_rangking -> mainViewModel.updateSelectedRoute(R.id.navigation_rangking)
                R.id.navigation_shop -> mainViewModel.updateSelectedRoute(R.id.navigation_shop)
            }
        }
    }

    private fun setupTopBar() {
        val composeTopBar = findViewById<ComposeView>(R.id.compose_top_bar)
        composeTopBar.setContent {
            val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
            val currentDestination = navBackStackEntry?.destination

            val userData by userViewModel.userData.collectAsState(initial = UserModel())

            // setting theme for choosing which fragment did have to use topbar in components.theme
            CerdikiaTheme {
                val statusBarInsets = WindowInsets.statusBars.asPaddingValues()
                Column(modifier = Modifier.fillMaxWidth().padding(statusBarInsets)) {
                    when (currentDestination?.id) {
                        //Adding the id in here for showing the topbar in the specific Fragment
                        R.id.navigation_home, R.id.navigation_shop, R.id.stageFragment -> {
                            PlayerStatusBar(
                                flagResourceId = mainViewModel.playerFlag, // Use mainViewModel
                                gemImageResourceId = R.drawable.ic_gems,
                                gemCount = userData.gems, // Use mainViewModel
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
    // Function to set the visibility of the bottom navigation bar in specific fragment
    fun setBottomNavigationVisibility(isVisible: Boolean){
        composeBottomNavigationView = binding.composeBottomNav
        composeBottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}