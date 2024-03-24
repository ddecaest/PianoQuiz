package pianokeypractice

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.util.Duration
import pianokeypractice.PianoKeyButtonFactory.createBlackButton
import pianokeypractice.PianoKeyButtonFactory.createWhitePianoButton
import kotlin.random.Random

object PianoKeyPracticePanel {

    private val keyToPressIndicatorButton = Button("< PRESS START >")
    private val correctCounterButton = Button("CORRECT: 0")
    private val wrongCounterButton = Button("WRONG: 0")

    private var listOfAllKeys = mutableListOf(
        "C", "D", "E", "F", "G", "A", "B", "C#", "D#", "F#", "G#", "A#"
    )

    fun createAndShow(root: VBox) {
        // Top Panel with Piano Layout
        val pianoKeyPanel = createPianoKeyPanel()
        // Bottom Panel with Buttons
        val buttonPanel = createButtonPanel()

        root.children.clear()
        root.children.addAll(pianoKeyPanel, buttonPanel)
    }

    private fun createPianoKeyPanel(): StackPane {
        val whiteKeysPane = Pane()
        val whiteButtons = mutableListOf<Button>()
        for (i in listOfAllKeys.indices) {
            if (listOfAllKeys[i].contains("#")) {
                continue
            }
            val whiteButton = createWhitePianoButton(listOfAllKeys[i], i, whiteKeysPane, ::handlePianoButtonClicked)
            whiteKeysPane.children.add(whiteButton)
            whiteButtons.add(whiteButton)
        }

        val blackKeysPane = Pane()
        for (i in listOfAllKeys.indices) {
            if (!listOfAllKeys[i].contains("#")) {
                continue
            }
            val matchingWhiteButton = whiteButtons.find { listOfAllKeys[i].contains(it.text) }!!
            val blackButton = createBlackButton(listOfAllKeys[i], matchingWhiteButton, ::handlePianoButtonClicked)
            blackKeysPane.children.add(blackButton)
        }

        // Make sure white keys are clickable
        whiteKeysPane.isPickOnBounds = false
        blackKeysPane.isPickOnBounds = false

        val stack = StackPane()
        stack.alignment = Pos.CENTER
        stack.children.addAll(whiteKeysPane, blackKeysPane);

        VBox.setVgrow(stack, Priority.ALWAYS)

        return stack
    }

    private fun createButtonPanel(): HBox {
        val buttonPanel = HBox()
        buttonPanel.alignment = Pos.BOTTOM_CENTER
        buttonPanel.spacing = 25.0
        buttonPanel.style = "-fx-border-color: black; -fx-border-width: 2px;"

        keyToPressIndicatorButton.prefWidth = 125.0
        keyToPressIndicatorButton.style = "-fx-background-color: transparent; -fx-border-width: 0;"
        correctCounterButton.prefWidth = 125.0
        correctCounterButton.style = "-fx-background-color: transparent; -fx-border-width: 0;"
        wrongCounterButton.prefWidth = 125.0
        wrongCounterButton.style = "-fx-background-color: transparent; -fx-border-width: 0;"

        val startButton = Button("Start")
        startButton.setOnMouseClicked {
            correctCounterButton.text = "CORRECT: 0"
            wrongCounterButton.text = "WRONG: 0"
            rollNextKey()
        }

        buttonPanel.children.addAll(startButton, keyToPressIndicatorButton, correctCounterButton, wrongCounterButton)
        return buttonPanel
    }

    private fun handlePianoButtonClicked(button: Button) {
        if (keyToPressIndicatorButton.text == "< PRESS START >") {
            return
        }
        if (keyToPressIndicatorButton.text == button.text) {
            val split = correctCounterButton.text.split(" ")
            correctCounterButton.text = split[0] + " " + (split[1].toInt() + 1).toString()
            blinkButton(button, Color.GREEN)
            blinkButton(correctCounterButton, Color.GREEN)
            rollNextKey()

        } else {
            val split = wrongCounterButton.text.split(" ")
            wrongCounterButton.text = split[0] + " " + (split[1].toInt() + 1).toString()
            blinkButton(button, Color.RED)
            blinkButton(wrongCounterButton, Color.RED)
        }
    }

    private fun rollNextKey() {
        var nextKey = keyToPressIndicatorButton.text
        while (nextKey != keyToPressIndicatorButton.text) {
            nextKey = listOfAllKeys[Random.nextInt(listOfAllKeys.size)]
        }
        keyToPressIndicatorButton.text = nextKey
    }

    private fun blinkButton(button: Button, blinkColour: Color) {
        val originalBackground = button.background

        val timeline = Timeline(
            KeyFrame(
                Duration.ZERO,
                KeyValue(
                    button.backgroundProperty(),
                    Background(BackgroundFill(blinkColour, CornerRadii.EMPTY, Insets.EMPTY))
                )
            ),
            KeyFrame(Duration.seconds(0.5), KeyValue(button.backgroundProperty(), originalBackground))
        )
        timeline.play()
    }
}