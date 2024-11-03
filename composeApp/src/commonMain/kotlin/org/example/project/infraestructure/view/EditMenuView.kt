package org.example.project.infraestructure.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext

import org.example.project.domain.model.MenuCollectionModel
import org.example.project.domain.model.MenuModel
import org.example.project.domain.model.ProductModel
import org.example.project.infraestructure.UIState
import org.example.project.infraestructure.viewModel.MenuViewModel
import org.example.project.infraestructure.viewModel.UserViewModel
import org.example.project.shared.calculateWindowSize
import org.koin.compose.viewmodel.koinViewModel

class EditMenuView() : Screen {
    private var showDialog by mutableStateOf(false)

    @Composable
    override fun Content() {
        val menuViewModel: MenuViewModel = koinViewModel()
        val userViewModel: UserViewModel = koinViewModel()

        val menuCollectionState by menuViewModel.menuCollectionState
        val insertedMenuState by menuViewModel.menuState
        val logoutState by userViewModel.logoutState

        val navigator = LocalNavigator.currentOrThrow

        var currentMenus by remember { mutableStateOf<List<MenuModel>>(emptyList()) }

        LaunchedEffect(Unit) {
            menuViewModel.getMenusList()
        }

        Column {
            Row {
                // Botón para cerrar sesión
                Button(
                    onClick = { userViewModel.logout() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {
                    Text("Cerrar sesión")
                }

                // Botón para guardar carta
                Button(
                    onClick = { currentMenus.forEach { menu -> menuViewModel.updateMenu(menu) } },
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {
                    Text("Guardar carta")
                }

                // Botón para ver carta
                Button(
                    onClick = { navigator.push(MenuView()) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {
                    Text("Ver carta")
                }
            }

            when (logoutState) {
                UIState.Disabled -> {}
                is UIState.Error -> Text((logoutState as UIState.Error).msg)
                UIState.Loading -> CircularProgressIndicator()
                is UIState.Success -> {
                    // Reseteamos logout a estado original para su posterior uso
                    userViewModel.logoutState.value = UIState.Disabled
                    navigator.push(LoginUserView())
                }
            }

            //TODO sincronizar productos con menus y admin y poner idAdmin obligatorio
            //TODO detectar tamaño de vista

            when (menuCollectionState) {
                UIState.Disabled -> {}
                is UIState.Error -> Text((menuCollectionState as UIState.Error).msg)
                UIState.Loading -> CircularProgressIndicator()
                is UIState.Success<MenuCollectionModel> -> {
                    //TODO nueva lista para ordenador
                    //editableMenuListExpanded()
                    currentMenus =
                        (menuCollectionState as UIState.Success<MenuCollectionModel>).model.data
                    editableMenuList(currentMenus)
                }
            }

            // Espaciador para empujar el botón hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            when (insertedMenuState) {
                UIState.Disabled -> {}
                is UIState.Error -> Text((insertedMenuState as UIState.Error).msg)
                UIState.Loading -> CircularProgressIndicator()
                is UIState.Success -> {
                    LaunchedEffect(Unit) {
                        menuViewModel.getMenusList()
                    }
                }
            }

            // Diálogo lanzado por animación
            AnimatedVisibility(
                visible = showDialog,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                addMenuDialog(
                    onDismiss = { showDialog = false },
                    onAddMenu = { newMenuModel ->
                        menuViewModel.insertMenu(newMenuModel)

                        showDialog = false
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun editableMenuList(menusValue: List<MenuModel>) {
        val menuViewModel: MenuViewModel = koinViewModel()

        var menus by remember { mutableStateOf(menusValue) }

        val deletedMenuState by menuViewModel.deletedMenuState

        when (deletedMenuState) {
            UIState.Disabled -> {}
            is UIState.Error -> Text((deletedMenuState as UIState.Error).msg)
            UIState.Loading -> CircularProgressIndicator()
            is UIState.Success -> {
                val deletedId = (deletedMenuState as UIState.Success<Int>).model
                // Filtrar los menus que no tengan el ID eliminado
                menus = menus.filterNot { it.id == deletedId }
            }
        }

        // Mostrar la lista de menús y productos
        LazyColumn {
            menus.forEach { menu ->
                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF2F2F2))  // Fondo gris claro
                            .padding(16.dp)
                    ) {
                        Text(
                            text = menu.name,
                            style = MaterialTheme.typography.h5
                        )
                    }
                }

                item {
                    editableMenuItem(menu)
                }
            }

            item { // Botón para agregar un nuevo menú
                Button(onClick = { showDialog = true }) {
                    Text("Agregar Nuevo Menú")
                }
            }
        }
    }

    @Composable
    fun editableMenuItem(menu: MenuModel) {
        var menuName by remember { mutableStateOf(menu.name) }
        var products by remember { mutableStateOf(menu.products) }

        val menuViewModel: MenuViewModel = koinViewModel()

        val deletedProductState by menuViewModel.deletedProductState

        when (deletedProductState) {
            UIState.Disabled -> {}
            is UIState.Error -> Text((deletedProductState as UIState.Error).msg)
            UIState.Loading -> CircularProgressIndicator()
            is UIState.Success -> {
                val deletedId = (deletedProductState as UIState.Success<Int>).model
                // Filtrar los productos que no tengan el ID eliminado
                products = products.filterNot { it.id == deletedId }
            }
        }

        val windowSizeClass = calculateWindowSize()
        val columnCount: Int = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> 1
            WindowWidthSizeClass.Medium -> 2
            WindowWidthSizeClass.Expanded -> 3
            else -> 1
        }

        Column {
            // Editar el nombre del menú
            Row {
                TextField(
                    value = menuName,
                    onValueChange = {
                        menuName = it
                        menu.name = menuName
                    },
                    label = { Text("Nombre del Menú") },
                )

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { menuViewModel.deleteMenu(menu.id) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Eliminar Menú")
                }
            }

            // Mostrar los productos en filas
            products.chunked(columnCount).forEachIndexed { index, rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { product ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            editableProductItem(product)
                        }
                    }
                }

                // Línea divisoria entre productos
                if (index < menu.products.size - 1) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
            }

            // Botón para agregar un nuevo producto
            Button(
                onClick = {
                    // id = -1 indica que el nuevo producto no se ha añadido a DB
                    val newProduct = ProductModel(id = -1, name = "", description = "")
                    // Agregamos un nuevo producto vacío a la lista
                    products = products + newProduct
                    // Actualizamos también el original
                    menu.products = products
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Agregar Producto")
            }
        }
    }

    @Composable
    fun editableProductItem(product: ProductModel) {
        var productName by remember { mutableStateOf(product.name) }
        var productDescription by remember { mutableStateOf(product.description) }
        var productPrice by remember { mutableStateOf(product.price) }

        val menuViewModel: MenuViewModel = koinViewModel()

        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .weight(1f)
                        .background(Color.LightGray)
                        .clickable {
                            // Lanzar vista para editar la imagen del producto
                            navigator.push(ImagePickerView(product.id, product.name, menuViewModel))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val context = LocalPlatformContext.current

                    // El atributo model requiere de un ImageRequest o un ImageRequest.data
                    AsyncImage(
                        model = menuViewModel.getImageRequest(product.id, context),
                        contentDescription = null,
                        imageLoader = SingletonImageLoader.get(context)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp)) // Espacio entre la imagen y el texto

                Column(modifier = Modifier.weight(3f)) {
                    // Editar el nombre y la descripción del producto
                    TextField(
                        value = productName,
                        onValueChange = {
                            productName = it
                            product.name = productName
                        },
                        label = { Text("Nombre del Producto") }
                    )
                    TextField(
                        value = productDescription,
                        onValueChange = {
                            productDescription = it
                            product.description = productDescription
                        },
                        label = { Text("Descripción del Producto") },
                    )
                    TextField(
                        value = productPrice,
                        onValueChange = {
                            productPrice = it
                            product.price = productPrice
                            println("product: $product")
                        },
                        label = { Text("Precio del Producto") }
                    )
                    Button(
                        onClick = { menuViewModel.deleteProduct(product.id) },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Eliminar Producto")
                    }

                }
            }
        }
    }

    @Composable
    fun addMenuDialog(onDismiss: () -> Unit, onAddMenu: (MenuModel) -> Unit) {
        // Creamos un menú con un producto vacío
        val newProducts: List<ProductModel> = listOf(
            ProductModel(id = -1, name = "", description = "")
        )
        val newMenu = MenuModel(id = -1, name = "", products = newProducts)

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Agregar Nuevo Menú") },
            text = {
                editableMenuItem(newMenu)
            },
            confirmButton = {
                Button(onClick = { onAddMenu(newMenu) }) {
                    Text("Agregar Menú")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}