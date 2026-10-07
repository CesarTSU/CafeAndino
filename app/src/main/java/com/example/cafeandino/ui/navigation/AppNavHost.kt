package com.example.cafeandino.ui.navigation

// AppNavHost.kt


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cafeandino.ui.CartScreen
import com.example.cafeandino.ui.CheckoutScreen
import com.example.cafeandino.ui.ConfirmationScreen
import com.example.cafeandino.ui.HomeScreen
import com.example.cafeandino.ui.ProductDetailScreen
import com.example.cafeandino.viewmodel.CartViewModel
import com.example.cafeandino.viewmodel.CheckoutViewModel
import com.example.cafeandino.viewmodel.HomeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    checkoutViewModel: CheckoutViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        // NODO 1: menú
        composable(Routes.HOME) {
            HomeScreen(
                homeViewModel = homeViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { productId ->
                    navController.navigate(Routes.productDetail(productId))
                },
                //Primer error que encuentro habia que cambiar carrito por routes.cart para que no se crashee
                onCartClick = { navController.navigate(Routes.CART) }
                ,
                onCheckoutClick = { navController.navigate(Routes.CHECKOUT) }
            )
        }

        // NODO 2: detalle de producto
        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val menuItems by homeViewModel.menuItems.collectAsState()
            val orderCount by cartViewModel.orderCount.collectAsState()
            val product = menuItems.firstOrNull { it.id == productId }

            ProductDetailScreen(
                product = product,
                orderCount = orderCount,
                onAdd = {
                    if (product != null) {
                        cartViewModel.addItem(product)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // NODO 3: carrito
        composable(Routes.CART) {
            CartScreen(
                cartViewModel = cartViewModel,
                onCheckoutClick = { navController.navigate(Routes.CHECKOUT) },
                onBack = { navController.popBackStack() }
            )
        }

        // NODO 4: formulario de pedido
        composable(Routes.CHECKOUT) {
            val orderCount by cartViewModel.orderCount.collectAsState()

            CheckoutScreen(
                viewModel = checkoutViewModel,
                itemCount = orderCount,
                onOrderConfirmed = { customerName ->
                    cartViewModel.confirmOrder(customerName)
                    checkoutViewModel.reset()
                    navController.navigate(
                        Routes.confirmation(Uri.encode(customerName))
                    ) {
                        popUpTo(Routes.HOME)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // NODO 5: confirmación, que recibe el nombre por la ruta
        composable(
            route = Routes.CONFIRMATION,
            arguments = listOf(navArgument("customerName") { type = NavType.StringType })
        ) { backStackEntry ->
            val customerName = backStackEntry.arguments?.getString("customerName") ?: ""

            ConfirmationScreen(
                customerName = customerName,
                onBackToMenu = { navController.popBackStack() }
            )
        }
    }
}
