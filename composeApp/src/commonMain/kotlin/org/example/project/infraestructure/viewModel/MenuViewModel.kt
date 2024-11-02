package org.example.project.infraestructure.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.PlatformContext
import coil3.request.ImageRequest
import kotlinx.coroutines.launch
import org.example.project.application.DeleteMenu
import org.example.project.application.DeleteProduct
import org.example.project.application.GetImageRequest
import org.example.project.application.GetMenus
import org.example.project.application.InsertImageInProduct
import org.example.project.application.InsertMenu
import org.example.project.application.UpdateMenu
import org.example.project.domain.model.MenuCollectionModel
import org.example.project.domain.model.MenuModel
import org.example.project.infraestructure.UIState
import org.example.project.shared.SharedImage

class MenuViewModel(
    private val getMenusUseCase: GetMenus,
    private val updateMenuUseCase: UpdateMenu,
    private val insertMenuUseCase: InsertMenu,
    private val deleteProductUseCase: DeleteProduct,
    private val deleteMenuUseCase: DeleteMenu,
    private val insertImageUseCase: InsertImageInProduct,
    private val getImageRequestUseCase: GetImageRequest
) : ViewModel() {
    val menuCollectionState: MutableState<UIState<MenuCollectionModel>> = mutableStateOf(UIState.Disabled)
    val menuState: MutableState<UIState<MenuModel>> = mutableStateOf(UIState.Disabled)

    // Si el estado es success, guardan el id del modelo de la operación realizada
    val deletedProductState: MutableState<UIState<Int>> = mutableStateOf(UIState.Disabled)
    val deletedMenuState: MutableState<UIState<Int>> = mutableStateOf(UIState.Disabled)
    val insertedImageState: MutableState<UIState<Int>> = mutableStateOf(UIState.Disabled)

    fun getMenusList() {
        // Lanzamos hilo para realizar petición
        viewModelScope.launch {
            menuCollectionState.value = UIState.Loading

            // El restaurante contiene una lista de menus
            val menuCollectionModel: MenuCollectionModel = getMenusUseCase()

            if (menuCollectionModel.data.isEmpty()) {
                menuCollectionState.value = UIState.Error("Error al descargar la carta")
            } else {
                menuCollectionState.value = UIState.Success(menuCollectionModel)
            }
        }
    }

    fun updateMenu(menuModel: MenuModel) {
        viewModelScope.launch {
            menuState.value = UIState.Loading

            val insertMenu: MenuModel = updateMenuUseCase(menuModel)

            if (menuModel.name.isEmpty()) {
                menuState.value = UIState.Error("No se ha podido actualizar el Menu")
            } else {
                menuState.value = UIState.Success(insertMenu)
            }
        }
    }

    fun insertMenu(menuModel: MenuModel) {
        viewModelScope.launch {
            menuState.value = UIState.Loading

            val insertedMenu: MenuModel = insertMenuUseCase(menuModel)

            if (insertedMenu.name.isEmpty()) {
                menuState.value = UIState.Error("No se ha podido insertar el Menu")
            } else {
                menuState.value = UIState.Success(insertedMenu)
            }
        }
    }

    fun deleteMenu(id: Int) {
        viewModelScope.launch {
            deletedMenuState.value = UIState.Loading

            val result = deleteMenuUseCase(id)

            if (result) {
                deletedMenuState.value = UIState.Success(id)
            } else {
                deletedMenuState.value = UIState.Error("No se ha podido borrar el Menu")
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            deletedProductState.value = UIState.Loading

            val result = deleteProductUseCase(id)

            if (result) {
                deletedProductState.value = UIState.Success(id)
            } else {
                deletedProductState.value = UIState.Error("No se ha podido borrar el Producto")
            }
        }
    }

    fun insertImageInProduct(image: SharedImage, idProduct: Int) {
        viewModelScope.launch {
            insertedImageState.value = UIState.Loading

            val insertedImage = insertImageUseCase(image, idProduct)

            if (insertedImage != -1) {
                insertedImageState.value = UIState.Success(insertedImage)
            } else {
                insertedImageState.value = UIState.Error("No se ha podido insertar la imagen")
            }
        }
    }

    fun getImageRequest(idProduct: Int, context: PlatformContext): ImageRequest {
        return getImageRequestUseCase.invoke(idProduct, context)
    }

}