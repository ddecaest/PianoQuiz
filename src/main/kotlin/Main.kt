import PianoKeyButtonFactory.createBlackButton
import PianoKeyButtonFactory.createWhitePianoButton
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration
import kotlin.random.Random


class PianoApp : Application() {

    private lateinit var primaryStage: Stage

    private val keyToPressIndicatorButton = Button("< PRESS START >")
    private val correctCounterButton = Button("CORRECT: 0")
    private val wrongCounterButton = Button("WRONG: 0")

    private var listOfAllKeys = mutableListOf(
        "C", "D", "E", "F", "G", "A", "B", "C#", "D#", "F#", "G#", "A#"
    )

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        primaryStage.title = "Piano App"

        val root = VBox()
        root.spacing = 25.0
        root.padding = Insets(10.0)

        val introductorySelectionPanel = IntroductorySelectionPanelFactory.createSelectionPanel(
            onKeyPracticeButtonClicked = { showPianoPane() },
            onMusicNotationPracticeButtonClicked = { this.primaryStage.close() },
            onExitButtonClicked = { this.primaryStage.close() }
        )
        root.children.add(introductorySelectionPanel)

        val scene = Scene(root, 600.0, 400.0)
        primaryStage.scene = scene

        primaryStage.show()
    }

    private fun showPianoPane() {
        val root = primaryStage.scene.root as VBox

        // Top Panel with Piano Layout
        val pianoPanel = createPianoLayout()
        VBox.setVgrow(pianoPanel, Priority.ALWAYS)

        // Bottom Panel with Buttons
        val buttonPanel = creatButtonPanel()

        root.children.clear()
        root.children.addAll(pianoPanel, buttonPanel)
    }

    private fun createPianoLayout(): StackPane {
        val whiteKeysPane = Pane()
        val whiteButtons = mutableListOf<Button>()
        for (i in listOfAllKeys.indices) {
            if (listOfAllKeys[i].contains("#")) {
                continue
            }

            val whiteButton = createWhitePianoButton(i, whiteKeysPane, ::handlePianoButtonClicked)
            whiteKeysPane.children.add(whiteButton)
            whiteButtons.add(whiteButton)
        }

        val blackKeysPane = Pane()
        for (i in listOfAllKeys.indices) {
            if (!listOfAllKeys[i].contains("#")) {
                continue
            }

            val matchingWhiteButton = whiteButtons.find { listOfAllKeys[i].contains(it.text) }!!
            val blackButton = createBlackButton(i, matchingWhiteButton, ::handlePianoButtonClicked)
            blackKeysPane.children.add(blackButton)
        }

        // Make sure white keys are clickable
        whiteKeysPane.isPickOnBounds = false
        blackKeysPane.isPickOnBounds = false

        val stack = StackPane()
        stack.alignment = Pos.CENTER
        stack.children.addAll(whiteKeysPane, blackKeysPane);
        return stack
    }

    private fun creatButtonPanel(): HBox {
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
        val nextKey = listOfAllKeys[Random.nextInt(listOfAllKeys.size)]
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

fun main() {
    launch(PianoApp::class.java)
}