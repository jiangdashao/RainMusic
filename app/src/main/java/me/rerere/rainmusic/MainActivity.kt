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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.rainmusic.ui.local.LocalNavController
import me.rerere.rainmusic.ui.screen.index.IndexScreen
import me.rerere.rainmusic.ui.screen.login.LoginScreen
import me.rerere.rainmusic.ui.theme.RainMusicTheme
import okhttp3.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge to Edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            RainMusicTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
                    val darkIcon = MaterialTheme.colors.isLight
                    SideEffect {
                        systemUiController.setNavigationBarColor(Color.Transparent, darkIcon)
                        systemUiController.setStatusBarColor(Color.Transparent, darkIcon)
                    }

                    val navController = rememberAnimatedNavController()

                    CompositionLocalProvider(
                        LocalNavController provides navController
                    ) {
                        AnimatedNavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = "index",
                            enterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = {
                                        it
                                    },
                                    animationSpec = tween()
                                )
                            },
                            exitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = {
                                        -it
                                    },
                                    animationSpec = tween()
                                ) + fadeOut(
                                    animationSpec = tween()
                                )
                            },
                            popEnterTransition = {
                                slideInHorizontally(
                                    initialOffsetX = {
                                        -it
                                    },
                                    animationSpec = tween()
                                )
                            },
                            popExitTransition = {
                                slideOutHorizontally(
                                    targetOffsetX = {
                                        it
                                    },
                                    animationSpec = tween()
                                )
                            }
                        ) {

                            composable("login") {
                                LoginScreen()
                            }

                            composable("index") {
                                IndexScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}