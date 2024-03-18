import javafx.scene.control.Button
import javafx.scene.layout.Pane

object PianoKeyButtonFactory {

    private var listOfAllKeys = mutableListOf(
        "C", "D", "E", "F", "G", "A", "B", "C#", "D#", "F#", "G#", "A#"
    )

    fun createWhitePianoButton(i: Int, whiteKeysPane: Pane, onButtonClicked: (Button) -> Unit): Button {
        val whiteButton = Button(listOfAllKeys[i])
        whiteButton.style = "-fx-base: white; -fx-padding: 55 0 0 0; -fx-text-fill: transparent;"
        whiteButton.prefWidth = 80.0
        whiteButton.prefHeight = 180.0
        whiteButton.setOnMouseClicked { onButtonClicked.invoke(whiteButton) }
        whiteButton.layoutXProperty().bind(whiteKeysPane.layoutXProperty().add(i * 80))
        return whiteButton
    }

    fun createBlackButton(i: Int, matchingWhiteButton: Button, onButtonClicked: (Button) -> Unit): Button {
        val blackButton = Button(listOfAllKeys[i])
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