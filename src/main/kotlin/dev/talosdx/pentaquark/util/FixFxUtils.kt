package dev.talosdx.pentaquark.util

import com.sun.glass.ui.Window
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import com.sun.jna.platform.win32.WinUser
import mu.KotlinLogging


object FixFxUtils {
    private val log = KotlinLogging.logger {}

    fun fixMinimizeAppByClickOnTaskbar() {
        try {
            val lhwnd = Window.getWindows()[0].nativeWindow
            val lpVoid = Pointer(lhwnd)
            val hwnd = HWND(lpVoid)
            val user32 = User32.INSTANCE
            val oldStyle = user32.GetWindowLong(hwnd, WinUser.GWL_STYLE)
            log.trace { Integer.toBinaryString(oldStyle) }
            val newStyle = oldStyle or 0x00020000 //WS_MINIMIZEBOX

            log.trace { Integer.toBinaryString(newStyle) }
            user32.SetWindowLong(hwnd, WinUser.GWL_STYLE, newStyle)
        } catch (e: Exception) {
            log.error(null, e)
        }
    }
}