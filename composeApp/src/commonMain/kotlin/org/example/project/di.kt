package org.example.project

import com.russhwolf.settings.Settings
import org.example.project.application.DeleteMenu
import org.example.project.application.DeleteProduct
import org.example.project.application.GetImageRequest
import org.example.project.application.GetMenus
import org.example.project.application.InsertImageInProduct
import org.example.project.application.InsertMenu
import org.example.project.application.Logout
import org.example.project.application.UpdateMenu
import org.example.project.domain.repository.MenuRepository
import org.example.project.domain.service.AuthService
import org.example.project.domain.service.UserService
import org.example.project.domain.service.MenuService
import org.example.project.domain.repository.UserRepository
import org.example.project.domain.model.AuthModel
import org.example.project.domain.repository.ProductRepository
import org.example.project.domain.service.ImageService
import org.example.project.domain.service.ProductService
import org.example.project.infraestructure.viewModel.MenuViewModel
import org.example.project.infraestructure.viewModel.UserViewModel
import org.example.project.shared.calculateWindowSize
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun provideToken(settings: Settings): AuthModel {
    return AuthModel(settings.getString(config.TOKEN_KEY, ""))
}

val applicationModule = module {
    factoryOf(::GetMenus)
    factoryOf(::UpdateMenu)
    factoryOf(::InsertMenu)
    factoryOf(::Logout)
    factoryOf(::DeleteProduct)
    factoryOf(::DeleteMenu)
    factoryOf(::InsertImageInProduct)
    factoryOf(::GetImageRequest)
}

val dataModule = module {
    singleOf(::Settings)
    singleOf(::provideToken)

    singleOf(::AuthService)
    singleOf(::UserService)
    singleOf(::MenuService)
    singleOf(::ImageService)
    singleOf(::ProductService)

    singleOf(::UserRepository)
    singleOf(::MenuRepository)
    singleOf(::ProductRepository)
}

val viewModelModule = module {
    viewModelOf(::UserViewModel)
    viewModelOf(::MenuViewModel)
}

fun initKoin() {
    startKoin{
        modules(dataModule, applicationModule, viewModelModule)
    }
}