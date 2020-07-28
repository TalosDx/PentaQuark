package dev.talosdx.pentaquark.util

import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.stage.Stage


object ResizeHelper {
    private lateinit var resizeListener: ResizeListener
    val isResizing: Boolean get() = resizeListener.isResizing

    fun addResizeListener(stage: Stage): ResizeListener? {
        resizeListener = ResizeListener(stage)
        val scene: Scene = stage.scene
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener)
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener)
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener)
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener)
        scene.addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener)
        scene.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener)
        return resizeListener
    }
}

class ResizeListener(private val stage: Stage) : EventHandler<MouseEvent?> {
    private val scene: Scene = stage.scene
    private var cursorEvent: Cursor = Cursor.DEFAULT
    private val border = 4
    private var startX = 0.0
    private var startY = 0.0
    private var sceneOffsetX = 0.0
    private var sceneOffsetY = 0.0
    private var padTop = 0.0
    private var padRight = 0.0
    private var padBottom = 0.0
    private var padLeft = 0.0
    var isResizing = false;

    override fun handle(event: MouseEvent?) {
        val mouseEvent = event!!

        val mouseEventType: EventType<out MouseEvent> = mouseEvent.eventType
        val mouseEventX = mouseEvent.sceneX
        val mouseEventY = mouseEvent.sceneY
        val viewWidth = stage.width - padLeft - padRight
        val viewHeight = stage.height - padTop - padBottom
        if (MouseEvent.MOUSE_MOVED == mouseEventType) {
            cursorEvent = if (mouseEventX < border + padLeft && mouseEventY < border + padTop) {
                isResizing = true
                Cursor.NW_RESIZE
            } else if (mouseEventX < border + padLeft && mouseEventY > viewHeight - border + padTop) {
                isResizing = true
                Cursor.SW_RESIZE
            } else if (mouseEventX > viewWidth - border + padLeft && mouseEventY < border + padTop) {
                isResizing = true
                Cursor.NE_RESIZE
            } else if (mouseEventX > viewWidth - border + padLeft && mouseEventY > viewHeight - border + padTop) {
                isResizing = true
                Cursor.SE_RESIZE
            } else if (mouseEventX < border + padLeft) {
                isResizing = true
                Cursor.W_RESIZE
            } else if (mouseEventX > viewWidth - border + padLeft) {
                isResizing = true
                Cursor.E_RESIZE
            } else if (mouseEventY < border + padTop) {
                isResizing = true
                Cursor.N_RESIZE
            } else if (mouseEventY > viewHeight - border + padTop) {
                isResizing = true
                Cursor.S_RESIZE
            } else {
                isResizing = false
                Cursor.DEFAULT
            }
            scene.cursor = cursorEvent
        } else if (MouseEvent.MOUSE_EXITED == mouseEventType || MouseEvent.MOUSE_EXITED_TARGET == mouseEventType) {
            scene.cursor = Cursor.DEFAULT
        } else if (MouseEvent.MOUSE_PRESSED == mouseEventType) {
            startX = viewWidth - mouseEventX
            startY = viewHeight - mouseEventY
            sceneOffsetX = mouseEvent.sceneX
            sceneOffsetY = mouseEvent.sceneY
        } else if (MouseEvent.MOUSE_DRAGGED == mouseEventType && Cursor.DEFAULT != cursorEvent) {
            if (Cursor.W_RESIZE != cursorEvent && Cursor.E_RESIZE != cursorEvent) {
                val minHeight =
                    if (stage.minHeight > border * 2)
                        stage.minHeight
                    else (border * 2).toDouble()

                if (Cursor.NW_RESIZE == cursorEvent || Cursor.N_RESIZE == cursorEvent || Cursor.NE_RESIZE == cursorEvent) {
                    if (stage.height > minHeight || mouseEventY < 0) {
                        val height =
                            stage.y - mouseEvent.screenY + stage.height + sceneOffsetY
                        val y = mouseEvent.screenY - sceneOffsetY
                        stage.height = height
                        stage.y = y
                    }
                } else {
                    if (stage.height > minHeight || mouseEventY + startY - stage.height > 0) {
                        stage.height = mouseEventY + startY + padBottom + padTop
                    }
                }
            }
            if (Cursor.N_RESIZE != cursorEvent && Cursor.S_RESIZE != cursorEvent) {
                val minWidth =
                    if (stage.minWidth > border * 2)
                        stage.minWidth
                    else (border * 2).toDouble()
                if (Cursor.NW_RESIZE == cursorEvent || Cursor.W_RESIZE == cursorEvent || Cursor.SW_RESIZE == cursorEvent) {
                    if (stage.width > minWidth || mouseEventX < 0) {
                        val width = stage.x - mouseEvent.screenX + stage.width + sceneOffsetX
                        val x = mouseEvent.screenX - sceneOffsetX
                        stage.width = width
                        stage.x = x
                    }
                } else {
                    if (stage.width > minWidth || mouseEventX + startX - stage.width > 0) {
                        stage.width = mouseEventX + startX + padLeft + padRight
                    }
                }
            }
        }
    }
}