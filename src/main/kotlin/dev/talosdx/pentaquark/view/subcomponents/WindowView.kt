package dev.talosdx.pentaquark.view.subcomponents

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon
import dev.talosdx.pentaquark.controller.WindowController
import dev.talosdx.pentaquark.extension.materialIcon
import dev.talosdx.pentaquark.extension.sizeEm
import dev.talosdx.pentaquark.view.css.AppStyles
import javafx.geometry.Pos
import tornadofx.*

class WindowView(controller: WindowController) : View("") {
    override val root = hbox {
        useMaxWidth = true
        alignment = Pos.TOP_RIGHT
        prefHeight = 29.0
        prefWidth = 82.0
        hbox {
            prefHeight = 29.0
            prefWidth = 518.0
            onMouseClicked = controller.changeWindowSizeDoubleClick()
            onMousePressed = controller.pressWindow()
            onMouseDragged = controller.dragWindow()

            materialIcon(MaterialDesignIcon.RADIOBOX_BLANK) {
                sizeEm = 4
                addClass(AppStyles.windowIcon)
            }

            label {
                prefHeight = 29.0
                prefWidth = 520.0
                bind(titleProperty)
                hboxConstraints {
                    insets(left = 10.0)
                }
            }

            button {
                isMnemonicParsing = false
                onMouseClicked = controller.hideWindow()
                prefHeight = 10.0
                prefWidth = 10.0
                text = "_"
                hboxConstraints {
                    insets(right = 1.0)
                }
            }

            button {
                isMnemonicParsing = false
                onMouseClicked = controller.changeWindowSize()
                prefHeight = 10.0
                prefWidth = 10.0
                text = "X"
                hboxConstraints {
                    insets(right = 1.0)
                }
            }


            button {
                isMnemonicParsing = false
                onMouseClicked = controller.closeWindow()
                prefHeight = 10.0
                prefWidth = 10.0
                text = "X"
                hboxConstraints {
                    insets(right = 1.0)
                }
            }

        }
    }
}
