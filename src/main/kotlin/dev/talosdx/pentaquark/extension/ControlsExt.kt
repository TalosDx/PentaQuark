package dev.talosdx.pentaquark.extension

import de.jensd.fx.glyphs.GlyphIcon
import de.jensd.fx.glyphs.GlyphIcons
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView
import javafx.event.EventTarget
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import tornadofx.attachTo

fun EventTarget.materialIcon(
    icon: MaterialDesignIcon,
    op: MaterialDesignIconView.() -> Unit = {}
) = MaterialDesignIconView(icon).attachTo(this, op) {}

var MaterialDesignIconView.sizeEm: Int
    get() = size.replace(Regex("[^0-9]"), "").toInt()
    set(value) { size = "${value}em" }

fun Button.icon(icon: GlyphIcons, minButtonWidth: Double = 200.0) {
    graphic = when (icon) {
        is FontAwesomeIcon -> FontAwesomeIconView(icon)
        is MaterialDesignIcon -> MaterialDesignIconView(icon)
        else -> throw IllegalArgumentException("Unknown font family ${icon.fontFamily}")
    }
    with(graphic as GlyphIcon<*>) {
        contentDisplay = ContentDisplay.TOP
        setSize("3em")
    }
    minWidth = minButtonWidth
}