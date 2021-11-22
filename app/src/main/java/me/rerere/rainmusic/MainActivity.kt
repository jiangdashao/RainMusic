package me.rerere.rainmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.rerere.rainmusic.repo.UserRepo
import me.rerere.rainmusic.ui.local.LocalNavController
import me.rerere.rainmusic.ui.screen.Screen
import me.rerere.rainmusic.ui.screen.index.IndexScreen
import me.rerere.rainmusic.ui.screen.login.LoginScreen
import me.rerere.rainmusic.ui.screen.search.SearchScreen
import me.rerere.rainmusic.ui.theme.RainMusicTheme
import me.rerere.rainmusic.util.*
import okhttp3.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var preparingData = true

    @Inject
    lateinit var userRepo: UserRepo

    @ExperimentalPagerApi
    @ExperimentalMaterial3Api
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepVisibleCondition {
                preparingData
            }
        }

        init()

        // Edge to Edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            RainMusicTheme {
                ProvideWindowInsets {
                    val navController = rememberAnimatedNavController()

                    CompositionLocalProvider(
                        LocalNavController provides navController
                    ) {
                        AnimatedNavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = "index",
                            enterTransition = defaultEnterTransition,
                            exitTransition = defaultExitTransition,
                            popEnterTransition = defaultPopEnterTransition,
                            popExitTransition = defaultPopExitTransition
                        ) {
                            composable(Screen.Login.route) {
                                LoginScreen()
                            }

                            composable(Screen.Index.route) {
                                IndexScreen()
                            }

                            composable(Screen.Search.route) {
                                SearchScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun init() {
        userRepo
            .refreshLogin()
            .onCompletion {
                preparingData = false
            }
            .onEach {
                if(it is DataState.Error){
                    toast("未登录!")
                }
            }
            .launchIn(lifecycleScope)
    }
}