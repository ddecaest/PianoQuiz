package introduction

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.VBox

object IntroductorySelectionPanel {

    fun createSelectionPanel(
        onKeyPracticeButtonClicked: () -> Unit,
        onMusicNotationPracticeButtonClicked: () -> Unit,
        onExitButtonClicked: () -> Unit
    ): VBox {
        val selectionPanel = VBox()
        selectionPanel.alignment = Pos.CENTER
        selectionPanel.spacing = 25.0

        val keyPracticeButton = Button("Key practice")
        keyPracticeButton.setOnMouseClicked { onKeyPracticeButtonClicked.invoke() }

        val musicNotationButton = Button("Music notation practice")
        musicNotationButton.setOnMouseClicked { onMusicNotationPracticeButtonClicked.invoke() }

        val exitButton = Button("Exit")
        exitButton.setOnMouseClicked { onExitButtonClicked.invoke() }

        selectionPanel.children.addAll(keyPracticeButton, musicNotationButton, exitButton)
        return selectionPanel
    }
}