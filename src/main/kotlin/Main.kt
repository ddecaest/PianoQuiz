import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import pianokeypractice.PianoKeyPracticePanel

class PianoApp : Application() {

    private lateinit var primaryStage: Stage

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        primaryStage.title = "Piano Practice Pizzazz"

        val root = VBox()
        root.spacing = 25.0
        root.padding = Insets(10.0)

        val introductorySelectionPanel = IntroductorySelectionPanelFactory.createSelectionPanel(
            onKeyPracticeButtonClicked = { createAndShowKeyPracticeScreen() },
            onMusicNotationPracticeButtonClicked = { createAndShowMusicNotationPracticeScreen() },
            onExitButtonClicked = { this.primaryStage.close() }
        )
        root.children.add(introductorySelectionPanel)

        primaryStage.scene = Scene(root, 600.0, 400.0)
        primaryStage.show()
    }

    private fun createAndShowKeyPracticeScreen() {
        val root = primaryStage.scene.root as VBox
        PianoKeyPracticePanel.createAndShow(root)
    }

    private fun createAndShowMusicNotationPracticeScreen() {
        // TODO
        this.primaryStage.close()
    }
}

fun main() {
    launch(PianoApp::class.java)
}