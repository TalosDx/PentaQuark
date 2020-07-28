package dev.talosdx.pentaquark

import dev.talosdx.pentaquark.model.factory.DiFactory
import dev.talosdx.pentaquark.util.FixFxUtils.fixMinimizeAppByClickOnTaskbar
import dev.talosdx.pentaquark.util.ResizeHelper
import dev.talosdx.pentaquark.view.auth.LoginView
import dev.talosdx.pentaquark.view.css.AppStyles
import javafx.stage.Stage
import javafx.stage.StageStyle
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import tornadofx.launch
import kotlin.reflect.KClass


class App : App(LoginView::class, AppStyles::class) {

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)
        super.start(stage)
        stage.isResizable = true

        fixMinimizeAppByClickOnTaskbar()
        ResizeHelper.addResizeListener(stage)
    }


    companion object {
        private val log = KotlinLogging.logger {}

        @JvmStatic
        fun main(args: Array<String>) {
            log.info { "Starting Penta Quark..." }

            log.info { "Init DI..." }
            startKoin {
                slf4jLogger()
                properties(mapOf())
                fileProperties()
                environmentProperties()
                modules(DiFactory.modules)
            }
            FX.dicontainer = object : DIContainer, KoinComponent {
                override fun <T : Any> getInstance(type: KClass<T>): T =
                    getKoin().get(clazz = type, qualifier = null, parameters = null)
            }
            log.info { "Init DI done." }

            log.info { "Init TornadoFx..." }
            launch<dev.talosdx.pentaquark.App>(*args)
            log.info { "Init TornadoFx done." }
        }
    }
}
