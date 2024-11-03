package org.example.project.infraestructure.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.project.domain.model.MenuCollectionModel
import org.example.project.domain.model.MenuModel
import org.example.project.domain.model.ProductModel
import org.example.project.infraestructure.UIState
import org.example.project.infraestructure.viewModel.MenuViewModel
import org.example.project.shared.calculateWindowSize
import org.koin.compose.viewmodel.koinViewModel

class MenuView() : Screen {
    @Composable
    override fun Content() {
        val menuViewModel: MenuViewModel = koinViewModel()

        val menuCollectionState by menuViewModel.menuCollectionState

        LaunchedEffect(Unit) {
            menuViewModel.getMenusList()
        }

        when (menuCollectionState) {
            UIState.Disabled -> {}
            is UIState.Error -> Text((menuCollectionState as UIState.Error).msg)
            UIState.Loading -> CircularProgressIndicator()
            is UIState.Success<MenuCollectionModel> ->
                menuList((menuCollectionState as UIState.Success<MenuCollectionModel>).model.data)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun menuList(menus: List<MenuModel>) {
        val listState = rememberLazyListState() // Crear el LazyListState

        var selectedMenuIndex by remember { mutableStateOf(-1) }

        // Mostrar la lista de menús y productos
        LazyColumn(state = listState) {
            stickyHeader {
                // Cabecera con los nombres de los distintos menus o categorias
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    itemsIndexed(menus) { index, menu ->
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    selectedMenuIndex = index // Actualizar el índice del menú seleccionado
                                    CoroutineScope(Dispatchers.Main).launch {
                                        listState.scrollToItem(index + 1) // Desplazarse al índice del menú
                                    }
                                }
                                .padding(4.dp)
                                .zIndex(0f)
                        ) {
                            Column {
                                Text(
                                    text = menu.name,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )

                                // Mostrar la línea azul solo si este menú está seleccionado
                                if (selectedMenuIndex == index) {
                                    // Esto deberia hacerse con otro elemento, pero Divider o Box no se aplican correctamente
                                    Text(
                                        text = menu.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color.Blue)
                                            .zIndex(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            menus.forEach { menu ->
                item {
                    val windowSizeClass = calculateWindowSize()
                    println("windowSize: $windowSizeClass")
                    when (windowSizeClass.widthSizeClass) {
                        WindowWidthSizeClass.Compact -> menuItemCompact(menu)
                        WindowWidthSizeClass.Medium -> menuItemCompact(menu)
                        WindowWidthSizeClass.Expanded -> menuItemExpanded(menu)
                        else -> menuItemCompact(menu)
                    }

                }
            }
        }
    }

    @Composable
    private fun menuItemExpanded(menu: MenuModel) {
        Column {
            // Mostrar el nombre del menú
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFFF2F2F2))  // Fondo gris claro
                    .padding(16.dp)
            ) {
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.h5
                )
            }

            // Número de productos por fila
            val columnCount = 3

            // Mostrar los productos en filas
            menu.products.chunked(columnCount).forEach{ rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    println("row")
                    rowItems.forEach { product ->
                        println("rowItem")
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .shadow(
                                    elevation = 6.dp,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .offset(x = (-2).dp, y = (-4).dp) // Desplaza la sombra para simularla en la parte inferior y derecha
                                .background(Color.White, shape = MaterialTheme.shapes.medium) // Fondo y forma de la tarjeta
                        ) {
                            productItem(product)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    // Agregar espacios en blanco si el último row tiene menos de columnCount elementos
                    val remainingSlots = columnCount - rowItems.size
                    repeat(remainingSlots) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }


    @Composable
    private fun menuItemCompact(menu: MenuModel) {
        Column {
            // Mostrar el nombre del menú
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFFF2F2F2))  // Fondo gris claro
                    .padding(16.dp)
            ) {
                Text(
                    text = menu.name,
                    style = MaterialTheme.typography.h5
                )
            }

            // Mostrar los productos en filas
            menu.products.forEachIndexed { index, product ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        productItem(product)
                    }
                }

                // Línea divisoria entre productos
                if (index < menu.products.size - 1) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
            }
        }
    }

    @Composable
    private fun productItem(product: ProductModel) {
        val menuViewModel: MenuViewModel = koinViewModel()

        Row(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .weight(1f)
                    .background(Color.LightGray),
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
                // Mostrar nombre, descripción y precio del producto
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(1.dp)
                )
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(1.dp)
                )
                Text(
                    text = product.price + " €",
                    modifier = Modifier.padding(1.dp)
                )
            }
        }
    }
}

