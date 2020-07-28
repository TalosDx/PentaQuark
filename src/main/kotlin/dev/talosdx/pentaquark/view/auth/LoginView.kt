package dev.talosdx.pentaquark.view.auth

import dev.talosdx.pentaquark.view.subcomponents.WindowView
import tornadofx.View
import tornadofx.vbox

class LoginView(windowView: WindowView) : View("Login") {
    override val root = vbox {
        windowView.title = title
        add(windowView)
    }
}