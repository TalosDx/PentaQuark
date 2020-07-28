package dev.talosdx.pentaquark.extension

import javafx.scene.paint.Paint
import tornadofx.PropertyHolder

fun PropertyHolder.exampleColor(color: Paint) {
    unsafe("-some-property-color", raw(toHex(color)))
}

private fun toHex(color: Paint) = "#${color.toString().substring(2)}"