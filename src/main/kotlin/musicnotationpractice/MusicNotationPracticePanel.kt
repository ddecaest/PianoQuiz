package musicnotationpractice

import Util.blinkButton
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import java.util.*
import kotlin.random.Random


object MusicNotationPracticePanel {

    private const val SPACE_BETWEEN = 15.0
    private const val MUSIC_STAFF_WIDTH = 550.0

    private val validUserInputRegex = Regex("[a-gA-G]")

    private lateinit var musicStaff: Pane
    private val staffLines = mutableListOf<Line>()

    // From top of the music staff to bottom
    private val notes = listOf("A", "G", "F", "E", "D", "C", "B", "A", "G", "F", "E", "D", "C")

    private var lastNote: Note? = null

    data class Note(var graphicalElements: List<Node>, val noteName: String)

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
        musicStaffPane.prefWidth = MUSIC_STAFF_WIDTH
        musicStaffPane.prefHeight = 250.0

        val invisibleTopLine = Line(0.0, SPACE_BETWEEN, MUSIC_STAFF_WIDTH, SPACE_BETWEEN)
        invisibleTopLine.style = "-fx-stroke: transparent;"
        staffLines.add(invisibleTopLine)
        musicStaffPane.children.add(invisibleTopLine)

        for (i in 0..<5) {
            val yPosition = (i + 2) * SPACE_BETWEEN // Adjust spacing as needed
            val line = Line(0.0, yPosition, MUSIC_STAFF_WIDTH, yPosition) // Adjust width as needed
            line.stroke = Color.BLACK
            line.strokeWidth = 2.0
            staffLines.add(line)
            musicStaffPane.children.add(line)
        }

        val invisibleBottomLine = Line(0.0, 7 * SPACE_BETWEEN, MUSIC_STAFF_WIDTH, 7 * SPACE_BETWEEN)
        invisibleBottomLine.style = "-fx-stroke: transparent;"
        staffLines.add(invisibleBottomLine)
        musicStaffPane.children.add(invisibleBottomLine)

        return musicStaffPane to staffLines
    }

    private fun createButtonPanel(): HBox {
        val buttonPanel = HBox()
        buttonPanel.alignment = Pos.BOTTOM_CENTER
        buttonPanel.spacing = 25.0
        buttonPanel.style = "-fx-border-color: black; -fx-border-width: 2px;"

        val userInputBox = VBox()
        val noteLabel = Label("Enter Music Note:")
        val noteInput = TextField()
        userInputBox.children.addAll(noteLabel, noteInput)

        val correctCounterButton = Button("CORRECT: 0")
        correctCounterButton.prefWidth = 125.0
        correctCounterButton.style = "-fx-background-color: transparent; -fx-border-width: 0;"

        val wrongCounterButton = Button("WRONG: 0")
        wrongCounterButton.prefWidth = 125.0
        wrongCounterButton.style = "-fx-background-color: transparent; -fx-border-width: 0;"

        val startButton = Button("Start")
        startButton.prefWidth = 125.0

        startButton.setOnMouseClicked {
            startButton.text = "Let's go!"
            correctCounterButton.text = "CORRECT: 0"
            wrongCounterButton.text = "WRONG: 0"
            rollNextNote()
        }

        noteInput.setOnKeyPressed {
            try {
                if (startButton.text != "Let's go!" || !it.text.matches(validUserInputRegex)) {
                    return@setOnKeyPressed
                }

                if (it.text.uppercase(Locale.getDefault()) == lastNote?.noteName) {
                    val split = correctCounterButton.text.split(" ")
                    correctCounterButton.text = split[0] + " " + (split[1].toInt() + 1).toString()
                    blinkButton(correctCounterButton, Color.GREEN)
                    rollNextNote()
                } else {
                    val split = wrongCounterButton.text.split(" ")
                    wrongCounterButton.text = split[0] + " " + (split[1].toInt() + 1).toString()
                    blinkButton(wrongCounterButton, Color.RED)
                }
            } finally {
                noteInput.clear()
            }
        }

        buttonPanel.children.addAll(startButton, userInputBox, correctCounterButton, wrongCounterButton)
        return buttonPanel
    }

    private fun rollNextNote() {
        lastNote?.let { musicStaff.children.removeAll(it.graphicalElements) }

        val nextNoteIndex = Random.nextInt(notes.size)
        val nextNoteName = notes[nextNoteIndex]
        println(nextNoteName)

        val yPosition = if (nextNoteIndex % 2 == 0) {
            staffLines[nextNoteIndex / 2].startYProperty().get()
        } else {
            val lineAbove = staffLines[nextNoteIndex / 2].startYProperty().get()
            val lineBelow = staffLines[(nextNoteIndex / 2) + 1].startYProperty().get()
            (lineAbove + lineBelow) / 2
        }

        lastNote = Note(createMusicNote(nextNoteIndex, yPosition), nextNoteName)
        musicStaff.children.addAll(lastNote!!.graphicalElements)
    }

    private fun createMusicNote(nextNoteIndex: Int, yPosition: Double): List<Shape> {
        val shapesInNote = mutableListOf<Shape>()

        if(nextNoteIndex == 0 || nextNoteIndex == notes.size - 1) {
            // Add line in case the note falls outside the staff: if it doesn't, it will overlap and not show up
            val line = Line((MUSIC_STAFF_WIDTH / 2) - 10, yPosition, (MUSIC_STAFF_WIDTH / 2) + 10, yPosition)
            line.stroke = Color.BLACK
            line.strokeWidth = 2.0
            shapesInNote.add(line)
        }

        val noteCircle = Circle(MUSIC_STAFF_WIDTH / 2, yPosition, 7.0)
        noteCircle.fill = Color.RED
        shapesInNote.add(noteCircle)

        return shapesInNote
    }
}