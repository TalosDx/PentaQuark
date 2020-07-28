package dev.talosdx.pentaquark.model.factory

import dev.talosdx.pentaquark.config.AppConfig
import dev.talosdx.pentaquark.controller.AuthController
import dev.talosdx.pentaquark.controller.WindowController
import dev.talosdx.pentaquark.view.subcomponents.WindowView
import dev.talosdx.pentaquark.view.auth.LoginView
import dev.talosdx.pentaquark.view.css.AppStyles
import org.koin.dsl.module

object DiFactory {
    val modules = module {
        single { AppConfig() }
        single { WindowController() }
        single { AuthController() }

        single { AppStyles() }
        single { WindowView(get()) }
        single { LoginView(get()) }
    }
}