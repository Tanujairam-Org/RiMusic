package it.fast4x.rimusic.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import it.fast4x.rimusic.R
import it.fast4x.rimusic.enums.NavRoutes
import it.fast4x.rimusic.enums.NavigationBarPosition
import it.fast4x.rimusic.enums.UiType
import it.fast4x.rimusic.ui.components.NavigationRail
import it.fast4x.rimusic.ui.components.ScaffoldTB
import it.fast4x.rimusic.ui.styling.LocalAppearance
import it.fast4x.rimusic.ui.styling.favoritesIcon
import it.fast4x.rimusic.utils.UiTypeKey
import it.fast4x.rimusic.utils.bold
import it.fast4x.rimusic.utils.getCurrentRoute
import it.fast4x.rimusic.utils.menuItemColors
import it.fast4x.rimusic.utils.navigationBarPositionKey
import it.fast4x.rimusic.utils.rememberPreference
import it.fast4x.rimusic.utils.semiBold

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun Scaffold(
    navController: NavController,
    topIconButtonId: Int,
    onTopIconButtonClick: () -> Unit,
    showButton1: Boolean = false,
    topIconButton2Id: Int,
    onTopIconButton2Click: () -> Unit,
    showButton2: Boolean,
    bottomIconButtonId: Int? = R.drawable.search,
    onBottomIconButtonClick: (() -> Unit)? = {},
    showBottomButton: Boolean? = false,
    hideTabs: Boolean? = false,
    tabIndex: Int,
    onTabChanged: (Int) -> Unit,
    showTopActions: Boolean? = false,
    tabColumnContent: @Composable ColumnScope.(@Composable (Int, String, Int) -> Unit) -> Unit,
    onHomeClick: () -> Unit,
    onSettingsClick: (() -> Unit)? = {},
    onStatisticsClick: (() -> Unit)? = {},
    onHistoryClick: (() -> Unit)? = {},
    onSearchClick: (() -> Unit)? = {},
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.(Int) -> Unit
) {
    val (colorPalette, typography) = LocalAppearance.current
    val navigationBarPosition by rememberPreference(navigationBarPositionKey, NavigationBarPosition.Left)
    val uiType  by rememberPreference(UiTypeKey, UiType.RiMusic)

    if (navigationBarPosition == NavigationBarPosition.Top || navigationBarPosition == NavigationBarPosition.Bottom) {
            ScaffoldTB(
                navController = navController,
                topIconButtonId = topIconButtonId,
                onTopIconButtonClick = onTopIconButtonClick,
                showButton1 = showButton1,
                topIconButton2Id = topIconButton2Id,
                onTopIconButton2Click = onTopIconButton2Click,
                showButton2 = showButton2,
                tabIndex = tabIndex,
                onTabChanged = onTabChanged,
                tabColumnContent = tabColumnContent,
                showBottomButton = showBottomButton,
                bottomIconButtonId = bottomIconButtonId,
                onBottomIconButtonClick = onBottomIconButtonClick ?: {},
                showTopActions = showTopActions,
                content = content,
                hideTabs = hideTabs,
                onHomeClick = onHomeClick,
                onStatisticsClick = onStatisticsClick,
                onSettingsClick = onSettingsClick,
                onHistoryClick = onHistoryClick,
                onSearchClick = onSearchClick
            )
    } else {
        //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val customModifier = if(uiType == UiType.RiMusic)
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        else Modifier

        var expanded by remember { mutableStateOf(false) }

        androidx.compose.material3.Scaffold(
            modifier = customModifier,
            containerColor = colorPalette.background0,
            topBar = {
                if(uiType == UiType.RiMusic) {
                    TopAppBar(
                        navigationIcon = {
                            //val currentRoute = navController.currentBackStackEntry?.destination?.route
                            //println("navController current destination and route ${navController.currentDestination} $currentRoute")
                            if (getCurrentRoute(navController) != "home")
                                IconButton(
                                    onClick = {
                                        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED)
                                            navController.popBackStack()
                                    }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.chevron_back),
                                        tint = colorPalette.favoritesIcon,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                        },
                        title = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.app_icon),
                                    colorFilter = ColorFilter.tint(colorPalette.favoritesIcon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable {
                                            //onHomeClick()
                                            navController.navigate(NavRoutes.home.name)
                                        }
                                )
                                BasicText(
                                    text = "Music",
                                    style = TextStyle(
                                        fontSize = typography.xxl.semiBold.fontSize,
                                        fontWeight = typography.xxxl.semiBold.fontWeight,
                                        color = colorPalette.text
                                    ),
                                    modifier = Modifier
                                        .clickable {
                                            //onHomeClick()
                                            navController.navigate(NavRoutes.home.name)
                                        }
                                )
                            }
                        },
                        actions = {
                            //if (showTopActions == true) {
                                IconButton(
                                    onClick = {
                                        //if (onSearchClick != null) {
                                        //onSearchClick()
                                        navController.navigate(NavRoutes.search.name)
                                        //}
                                    }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.search),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.burger),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(colorPalette.background0)
                            ) {
                                DropdownMenuItem(
                                    colors = menuItemColors(),
                                    text = { Text(stringResource(R.string.history)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.history),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        //if (onHistoryClick != null) {
                                        //onHistoryClick()
                                        navController.navigate(NavRoutes.history.name)
                                        //}
                                    }
                                )
                                DropdownMenuItem(
                                    colors = menuItemColors(),
                                    text = { Text(stringResource(R.string.statistics)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.stats_chart),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        //if (onStatisticsClick != null) {
                                        //onStatisticsClick()
                                        navController.navigate(NavRoutes.statistics.name)
                                        //}
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    colors = menuItemColors(),
                                    text = { Text("Settings") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.settings),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        //if (onSettingsClick != null) {
                                        //onSettingsClick()
                                        navController.navigate(NavRoutes.settings.name)
                                        //}
                                    }
                                )
                            }

                                    /*
                                IconButton(onClick = { }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.stats_chart),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                if (onSettingsClick != null) {
                                    IconButton(onClick = onSettingsClick) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.settings),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                 */
                            //}
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarColors(
                            containerColor = colorPalette.background0,
                            titleContentColor = colorPalette.text,
                            scrolledContainerColor = colorPalette.background0,
                            navigationIconContentColor = colorPalette.background0,
                            actionIconContentColor = colorPalette.text
                        )
                    )
                }
            },

            bottomBar = {

            }

        ) {
            //it.calculateTopPadding()
            //**

            Row(
                //horizontalArrangement = Arrangement.spacedBy(0.dp),
                modifier = modifier
                    //.border(BorderStroke(1.dp, Color.Red))
                    //.padding(top = 50.dp)
                    .padding(it)
                    .background(colorPalette.background0)
                    .fillMaxSize()
            ) {
                val navigationRail: @Composable () -> Unit = {
                    NavigationRail(
                        topIconButtonId = topIconButtonId,
                        onTopIconButtonClick = onTopIconButtonClick,
                        showButton1 = showButton1,
                        topIconButton2Id = topIconButton2Id,
                        onTopIconButton2Click = onTopIconButton2Click,
                        showButton2 = showButton2,
                        bottomIconButtonId = bottomIconButtonId,
                        onBottomIconButtonClick = onBottomIconButtonClick ?: {},
                        showBottomButton = showBottomButton,
                        tabIndex = tabIndex,
                        onTabIndexChanged = onTabChanged,
                        content = tabColumnContent,
                        hideTabs = hideTabs
                    )
                }

                if (navigationBarPosition == NavigationBarPosition.Left)
                    navigationRail()

                val topPadding = if (uiType == UiType.ViMusic) 30.dp else 0.dp

                AnimatedContent(
                    targetState = tabIndex,
                    transitionSpec = {
                        val slideDirection = when (targetState > initialState) {
                            true -> AnimatedContentTransitionScope.SlideDirection.Up
                            false -> AnimatedContentTransitionScope.SlideDirection.Down
                        }

                        val animationSpec = spring(
                            dampingRatio = 0.9f,
                            stiffness = Spring.StiffnessLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        )

                        slideIntoContainer(slideDirection, animationSpec) togetherWith
                                slideOutOfContainer(slideDirection, animationSpec)
                    },
                    content = content, label = "",
                    modifier = Modifier
                        //.fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = topPadding)
                )

                if (navigationBarPosition == NavigationBarPosition.Right)
                    navigationRail()

            }
            //**
        }
    }

}
