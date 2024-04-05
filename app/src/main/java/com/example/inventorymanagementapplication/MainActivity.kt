package com.example.inventorymanagementapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventorymanagementapplication.ui.theme.InventoryManagementApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryManagementApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Controller for navigation:
                    val navController = rememberNavController()
                    /* A variable that defines the status (open/closed) of the
                    drawer:
                     */
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    // Coroutine scope is essential for drawer animation:
                    val scope = rememberCoroutineScope()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    // The current route of navigation destination:
                    val route = navBackStackEntry?.destination?.route
                    /* This drawer view is drawn on top of the currently open
                    screen view:
                    */
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                /* One navigationDrawerItem is a single item
                                in the navigation drawer menu:
                                */
                                NavigationDrawerItem(
                                    label = { Text(text = "Categories") },
                                    /* If the current route of navigation
                                    destination is "categoriesScreen",
                                    "selected" is true. Otherwise, it is false.
                                    */
                                    selected = route == "categoriesScreen",
                                    onClick = {
                                        /* After a drawer item is clicked,
                                        it is navigated to the screen defined
                                        by the route and the drawer is closed
                                        inside the coroutine scope.
                                        */
                                        navController.navigate(route = "categoriesScreen")
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = "Home"
                                        )
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = "Login") },
                                    selected = route == "loginScreen",
                                    onClick = {
                                        navController.navigate(route = "loginScreen")
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = "Login"
                                        )
                                    }
                                )
                            }
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "loginScreen"
                        ) {
                            /* Screens and their corresponding routes are
                            defined using composables. Each screen is
                            associated with its specific composable method:
                            */
                            composable(route = "categoriesScreen") {
                                CategoriesScreen(
                                    onMenuClicked = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    /* Callback for navigating to the
                                    CategoryEditScreen with the given
                                    CategoryItem:
                                    */
                                    goToCategoryEdit = { categoryItem ->
                                        navController.navigate(route = "categoryEditScreen/${categoryItem.categoryId}")
                                    },
                                    /* Callback for navigating to the
                                    CategoryAddScreen:
                                    */
                                    goToCategoryAdd = { navController.navigate(route = "categoryAddScreen") },
                                    goToItemList = { categoryItem ->
                                        navController.navigate(route = "rentalItemsScreen/${categoryItem.categoryId}") }
                                )
                            }
                            composable(route = "loginScreen") {
                                LoginScreen(
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    onLoginClick = {
                                        navController.navigate(route = "categoriesScreen")
                                    }
                                )
                            }
                            composable(route = "categoryEditScreen/{categoryId}") {
                                CategoryEditScreen(
                                    goToCategories = {
                                        navController.navigate(route = "categoriesScreen")
                                    },
                                    goBack = { navController.navigateUp() }
                                )
                            }
                            composable(route = "categoryAddScreen") {
                                CategoryAddScreen(
                                    goToCategories = {
                                        navController.navigate(route = "categoriesScreen")
                                    },
                                    goBack = { navController.navigateUp() }
                                )
                            }
                            composable(route = "rentalItemsScreen/{categoryId}") {
                                RentalItemsScreen(
                                    goBack = { navController.navigateUp() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
