package com.fhanafi.cerdikia

import android.content.Intent
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
import com.fhanafi.cerdikia.databinding.ActivityMainBinding
import com.fhanafi.cerdikia.ui.components.BottomNavItem
import com.fhanafi.cerdikia.ui.components.CustomBottomNavigationBar
import com.fhanafi.cerdikia.ui.components.PlayerStatusBar
import com.fhanafi.cerdikia.ui.components.theme.CerdikiaTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.pref.dataStore
import com.fhanafi.cerdikia.ui.components.ShimmerTopBar
import com.fhanafi.cerdikia.ui.splashscreen.SplashActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels() // Gunakan viewModels() untuk mendapatkan ViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var composeBottomNavigationView: ComposeView
    private lateinit var userPreference: UserPreference // Bisa di handle di UserViewModel kemungkinan untuk lebih tercentralisasi sejak userPreference ada di userViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = UserPreference.getInstance(dataStore)  // Bisa di handle di UserViewModel kemungkinan untuk lebih tercentralisasi sejak userPreference ada di userViewModel

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val factory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        observeUserToken()
        setupBottomNavigationBar()
        setupTopBar()
        destinationListener()
    }

    private fun observeUserToken() {
        lifecycleScope.launch {
            userPreference.getUserData().collect { user ->
                if (user.accessToken == null || user.refreshToken == null) {
                    // Navigate to SplashActivity
                    val intent = Intent(this@MainActivity, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
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
        val isNavigationAllowed by viewModel.isNavigationAllowed.collectAsState()

        val items = remember(selectedRoute) {
            listOf(
                BottomNavItem("Home", R.drawable.ic_homebotnav, R.id.navigation_home, selectedRoute == R.id.navigation_home),
                BottomNavItem("Rangking", R.drawable.ic_rangkingbotnav, R.id.navigation_rangking, selectedRoute == R.id.navigation_rangking),
                BottomNavItem("Profile", R.drawable.ic_profilebotnav, R.id.profileFragment, selectedRoute == R.id.profileFragment),
                BottomNavItem("Shop", R.drawable.ic_shopbotnav, R.id.navigation_shop, selectedRoute == R.id.navigation_shop),
            )
        }

        CustomBottomNavigationBar(
            items = items,
            onItemClick = { item ->
                if(isNavigationAllowed && item.route != selectedRoute) {
                    viewModel.setNavigationAllowed(false)
                    viewModel.updateSelectedRoute(item.route)
                    navController.navigate(item.route)

                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000L) // sesuaikan dengan delay yang sesuai
                        viewModel.setNavigationAllowed(true)
                    }
                }
            }
        )
    }

    private fun destinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> mainViewModel.updateSelectedRoute(R.id.navigation_home)
                R.id.navigation_rangking -> mainViewModel.updateSelectedRoute(R.id.navigation_rangking)
                R.id.profileFragment -> mainViewModel.updateSelectedRoute(R.id.profileFragment)
                R.id.navigation_shop -> mainViewModel.updateSelectedRoute(R.id.navigation_shop)
            }
        }
    }
    // TODO: i stil need to add energy for more element gamification but somehow it was creating more bug and i dont know why
    private fun setupTopBar() {
        val composeTopBar = findViewById<ComposeView>(R.id.compose_top_bar)
        composeTopBar.setContent {
            val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(null)
            val currentDestination = navBackStackEntry?.destination

            val userData by userViewModel.userData.collectAsState(initial = UserModel())
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                isLoading = true
                userViewModel.refreshPointData()
                delay(800)
                isLoading = false
            }

            // setting theme for choosing which fragment did have to use topbar in components.theme
            CerdikiaTheme {
                val statusBarInsets = WindowInsets.statusBars.asPaddingValues()
                Column(modifier = Modifier.fillMaxWidth().padding(statusBarInsets)) {
                    when (currentDestination?.id) {
                        //Adding the id in here for showing the topbar in the specific Fragment
                        R.id.navigation_home, R.id.navigation_shop, R.id.stageFragment -> {
                            if (isLoading) {
                                ShimmerTopBar()
                            } else {
                                PlayerStatusBar(
                                    flagResourceId = R.drawable.ic_indoflag, // Use mainViewModel
                                    gemImageResourceId = R.drawable.ic_gems,
                                    gemCount = userData.gems, // Use mainViewModel
                                    energyImageResourceId = R.drawable.ic_lighting,
                                    energyCount = userData.energy // Use mainViewModel
                                )
                            }
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