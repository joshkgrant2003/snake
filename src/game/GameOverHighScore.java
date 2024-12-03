package game;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameOverHighScore extends GameOverAbstract {
    public GameOverHighScore(Stage primaryStage, GameController gameController) {
        super(primaryStage, gameController);
    }

    @Override
    protected VBox createContent() {
        VBox content = new VBox(10);
        content.getStyleClass().add("vbox-content");
        Label message = createLabelWithIcon("New High Score!", "/resources/images/highscoreIcon.png");
        message.getStyleClass().add("label-message");
        content.getChildren().add(message);
        return content;
    }
}
