import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.util.Duration

object Util {

    fun blinkButton(button: Button, blinkColour: Color) {
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