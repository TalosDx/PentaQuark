package dev.talosdx.pentaquark.controller

import dev.talosdx.pentaquark.util.ResizeHelper
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.input.MouseEvent
import javafx.stage.Screen
import tornadofx.FX.Companion.primaryStage
import java.lang.System.currentTimeMillis

class WindowController {
    private var minimizeWidth = 0.0
    private var minimizeHeight = 0.0
    private var minY = 0.0
    private var minX = 0.0
    private var isMaximized = false;
    private var xOffset: Double = 0.0
    private var yOffset: Double = 0.0
    private var headerClickCounter = 0
    private var headerLastTimeClick = 0L

    fun hideWindow(): EventHandler<MouseEvent> = EventHandler {
        primaryStage.isIconified = !primaryStage.isIconified
    }

    fun changeWindowSize(): EventHandler<MouseEvent> = EventHandler {
        isMaximized = if (!isMaximized) {
            maximizeWindow()
            true
        } else {
            minimizeWindow()
            false
        }
    }

    private fun minimizeWindow() {
        primaryStage.x = minX
        primaryStage.y = minY
        primaryStage.width = minimizeWidth
        primaryStage.height = minimizeHeight
    }

    private fun saveSizes() {
        minX = primaryStage.x
        minY = primaryStage.y
        minimizeWidth = primaryStage.width
        minimizeHeight = primaryStage.height
    }

    private fun maximizeWindow() {
        saveSizes()

        val primaryScreenBounds = Screen.getPrimary().visualBounds
        primaryStage.x = primaryScreenBounds.minX
        primaryStage.y = primaryScreenBounds.minY
        primaryStage.width = primaryScreenBounds.width
        primaryStage.height = primaryScreenBounds.height
    }

    fun closeWindow(): EventHandler<MouseEvent> = EventHandler {
        Platform.exit()
    }

    fun pressWindow(): EventHandler<MouseEvent> = EventHandler {
        xOffset = it.sceneX
        yOffset = it.sceneY
    }

    fun dragWindow(): EventHandler<MouseEvent> = EventHandler {
        if(!ResizeHelper.isResizing && primaryStage.scene.cursor == Cursor.DEFAULT) {
            primaryStage.x = it.screenX - xOffset
            primaryStage.y = it.screenY - yOffset
        }
    }

    fun changeWindowSizeDoubleClick(): EventHandler<MouseEvent> = EventHandler {
        val currentTimeMillis = currentTimeMillis()

        headerClickCounter++
        if (headerClickCounter == 2) {
            headerClickCounter = 0
            if ((currentTimeMillis - headerLastTimeClick) / 100 <= 5) {
                changeWindowSize().handle(it)
            }
        }
        headerLastTimeClick = currentTimeMillis()
    }
}
