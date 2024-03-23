package musicnotationpractice

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line


object MusicNotationPracticePanel {

    private val keyToPressIndicatorButton = Button("< PRESS START >")
    private val correctCounterButton = Button("CORRECT: 0")
    private val wrongCounterButton = Button("WRONG: 0")

    private lateinit var musicStaff: Pane
    private val staffLines = mutableListOf<Line>()

    fun createAndShow(root: VBox) {
        val (musicStaffPane, staffLinesCreated) = createMusicStaff()
        val inputPanel = createButtonPanel()

        musicStaff = musicStaffPane
        staffLines.addAll(staffLinesCreated)

        val pane = BorderPane()
        pane.top = musicStaff
        pane.bottom = inputPanel
        BorderPane.setMargin(musicStaff, Insets(10.0))
        BorderPane.setMargin(inputPanel, Insets(10.0))

        root.children.clear()
        root.children.addAll(pane)
    }

    private fun createMusicStaff(): Pair<Pane, List<Line>> {
        val musicStaffPane = Pane()
        musicStaffPane.prefWidth = 550.0
        musicStaffPane.prefHeight = 250.0

        // Creating horizontal lines for the staff
        for (i in 0 until 5) {
            val yPosition = (i + 1) * 15.0 // Adjust spacing as needed
            val line = Line(0.0, yPosition, 550.0, yPosition) // Adjust width as needed
            line.stroke = Color.BLACK
            line.strokeWidth = 2.0
            staffLines.add(line)
            musicStaffPane.children.add(line)
        }
        return musicStaffPane to staffLines
    }

    private fun createButtonPanel(): HBox {
        // TODO
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

        val musicNoteBox = VBox()
        val noteLabel = Label("Enter Music Note:")
        val noteInput = TextField()
        musicNoteBox.children.addAll(noteLabel, noteInput)

        val startButton = Button("Start")
        startButton.setOnMouseClicked {
            correctCounterButton.text = "CORRECT: 0"
            wrongCounterButton.text = "WRONG: 0"
            rollNextNote()
        }

        buttonPanel.children.addAll(startButton,
            keyToPressIndicatorButton,
            correctCounterButton,
            wrongCounterButton
        )
        return buttonPanel
    }

    private fun rollNextNote() {
        // Example: Drawing a music note on the first staff line
        val noteCircle = Circle(50.0, staffLines[0].startYProperty().get(), 8.0) // Adjust X-position, Y-position, and radius as needed
        println(staffLines[0].startYProperty().get())
        noteCircle.fill = Color.RED // Adjust color as needed
        musicStaff.children.addAll(noteCircle)
    }
}