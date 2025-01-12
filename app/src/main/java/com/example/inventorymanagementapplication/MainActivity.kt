package com.example.inventorymanagementapplication

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventorymanagementapplication.ui.theme.InventoryManagementApplicationTheme
import com.example.inventorymanagementapplication.view.CategoriesScreen
import com.example.inventorymanagementapplication.view.CategoryAddScreen
import com.example.inventorymanagementapplication.view.CategoryEditScreen
import com.example.inventorymanagementapplication.view.LoginScreen
import com.example.inventorymanagementapplication.view.RegistrationScreen
import com.example.inventorymanagementapplication.view.RentalItemAddScreen
import com.example.inventorymanagementapplication.view.RentalItemEditScreen
import com.example.inventorymanagementapplication.view.RentalItemsScreen
import com.example.inventorymanagementapplication.viewmodel.LogoutViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryManagementApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val logoutViewModel: LogoutViewModel = viewModel()
                    // Controller for navigation:
                    val navController = rememberNavController()
                    val context = LocalContext.current

                    LaunchedEffect(key1 = logoutViewModel.logoutState.value.error) {
                        /* The let keyword creates an independent scope, which
                        is why the error property is referenced as "it". The
                        lambda is executed whenever the error is not null.
                        */
                        logoutViewModel.logoutState.value.error?.let {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    }

                    LaunchedEffect(key1 = logoutViewModel.logoutState.value.logoutOk) {
                        if (logoutViewModel.logoutState.value.logoutOk) {
                            logoutViewModel.setLogout(ok = false)
                            navController.navigate(route = "loginScreen") {
                                /* All other screens are popped out from
                                backstack except login screen as a current
                                screen.
                                 */
                                popUpTo(route = "loginScreen") {
                                    inclusive = true
                                }
                            }
                        }
                    }

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
                        // Deactivate side swipe in login and registration screens:
                        gesturesEnabled = (route != "loginScreen" && route != "registrationScreen"),
                        drawerState = drawerState,
//                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        drawerContent = {
                            ModalDrawerSheet {
                                Spacer(modifier = Modifier.height(16.dp))
                                /* One navigationDrawerItem is a single item
                                in the navigation drawer menu:
                                */
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(id = R.string.categories)) },
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
                                            contentDescription = stringResource(id = R.string.home)
                                        )
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(id = R.string.logout)) },
                                    selected = route == "loginScreen",
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        logoutViewModel.logout()
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = stringResource(id = R.string.logout_icon)
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
                            composable(route = "registrationScreen") {
                                RegistrationScreen(
                                    onRegistrationClick = {
                                        navController.navigate(route = "loginScreen")
                                    },
                                    goBack = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                            composable(route = "loginScreen") {
                                LoginScreen(
                                    goToCategories = {
                                        navController.navigate(route = "categoriesScreen")
                                    },
                                    onLoginClick = {
                                        navController.navigate(route = "categoriesScreen")
                                    },
                                    goToRegistrationScreen = {
                                        navController.navigate(route = "registrationScreen")
                                    }
                                )
                            }
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
                                        navController.navigate(
                                            route = "categoryEditScreen/${categoryItem.categoryId}"
                                        )
                                    },
                                    /* Callback for navigating to the
                                    CategoryAddScreen:
                                    */
                                    goToCategoryAdd = { navController.navigate(route = "categoryAddScreen") },
                                    goToRentalItemList = { categoryItem ->
                                        navController.navigate(
                                            route = "rentalItemsScreen/${categoryItem.categoryId}"
                                        )
                                    }
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
                            composable(route = "categoryEditScreen/{categoryId}") {
                                CategoryEditScreen(
                                    goToCategories = {
                                        navController.navigate(route = "categoriesScreen")
                                    },
                                    goBack = { navController.navigateUp() }
                                )
                            }
                            composable(route = "rentalItemsScreen/{categoryId}") {
                                RentalItemsScreen(
                                    goBack = { navController.navigateUp() },
                                    goToRentalItemEdit = {
                                        navController.navigate(
                                            route = "rentalItemEditScreen/${it.rentalItemId}/${it.categoryId}"
                                        )
                                    },
                                    goToRentalItemAdd = {
                                        navController.navigate(route = "rentalItemAddScreen/${it.categoryId}")
                                    }
                                )
                            }
                            composable(route = "rentalItemAddScreen/{categoryId}") {
                                RentalItemAddScreen(
                                    goToRentalItems = {
                                        navController.navigate(route = "rentalItemsScreen/${it.categoryId}") {
                                            popUpTo(
                                                "rentalItemsScreen/${it.categoryId}"
                                            ) {
                                                inclusive = true
                                                saveState = true
                                            }
                                        }
                                    },
                                    goBack = { navController.navigateUp() }
                                )
                            }
                            composable(route = "rentalItemEditScreen/{rentalItemId}/{categoryId}") {
                                RentalItemEditScreen(
                                    goToRentalItems = {
                                        navController.navigate(route = "rentalItemsScreen/${it.categoryId}") {
                                            popUpTo(
                                                "rentalItemsScreen/${it.categoryId}"
                                            ) {
                                                inclusive = true
                                                saveState = true
                                            }
                                        }
                                    },
                                    goBack = { navController.navigateUp() })
                            }
                        }
                    }
                }
            }
        }
    }
}
