package dev.talosdx.pentaquark.view.css

import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.cssclass

class AppStyles : Stylesheet() {
    companion object {
        val windowIcon by cssclass()
    }

    init {
        windowIcon {
            fill = Color.RED
        }
    }
}