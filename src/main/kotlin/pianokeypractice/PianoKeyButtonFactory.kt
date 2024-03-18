package pianokeypractice

import javafx.scene.control.Button
import javafx.scene.layout.Pane

object PianoKeyButtonFactory {

    fun createWhitePianoButton(name: String, number: Int, keyPane: Pane, onButtonClicked: (Button) -> Unit): Button {
        val whiteButton = Button(name)
        whiteButton.style = "-fx-base: white; -fx-padding: 55 0 0 0; -fx-text-fill: transparent;"
        whiteButton.prefWidth = 80.0
        whiteButton.prefHeight = 180.0
        whiteButton.setOnMouseClicked { onButtonClicked.invoke(whiteButton) }
        whiteButton.layoutXProperty().bind(keyPane.layoutXProperty().add(number * 80))
        return whiteButton
    }

    fun createBlackButton(name: String, matchingWhiteButton: Button, onButtonClicked: (Button) -> Unit): Button {
        val blackButton = Button(name)
        blackButton.style = "-fx-base: black; -fx-font-size: 10px; -fx-text-fill: transparent;"
        blackButton.prefWidth = 60.0
        blackButton.prefHeight = 120.0
        blackButton.setOnMouseClicked { onButtonClicked.invoke(blackButton) }
        blackButton.translateXProperty()
            .bind(
                matchingWhiteButton.layoutXProperty()
                    .add(matchingWhiteButton.widthProperty())
                    .subtract(blackButton.prefWidth / 2)
            )
        return blackButton
    }
}