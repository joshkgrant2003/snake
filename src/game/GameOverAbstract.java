package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// abstract class does heavy lifting for our game over guis
public abstract class GameOverAbstract {
    protected GameController gameController;

    // gui components needed for Game Over screen
    protected Stage primaryStage;
    protected BorderPane root;
    protected VBox buttonsBox;
    protected Scene scene;
    protected Button playAgain;
    protected Button leaderboard;
    protected Button settings;
    protected Button exit;

    public GameOverAbstract(Stage primaryStage, GameController gameController) {
        this.primaryStage = primaryStage;
        this.gameController = gameController;
    }

    // initializes gui components, sets actions, styles, and scenes
    public void initialize() {
        root = new BorderPane();
        root.setCenter(createContent());

        buttonsBox = new VBox(10);
        playAgain = createButtonWithIcon(" Play Again", "/resources/images/playIcon.png");
        leaderboard = createButtonWithIcon(" Leaderboard", "/resources/images/leaderboardIcon.png");
        settings = createButtonWithIcon(" Settings", "/resources/images/settingsIcon.png");
        exit = createButtonWithIcon(" Exit", "/resources/images/exitIcon.png");
        setButtonActions(primaryStage);
        buttonsBox.getChildren().addAll(playAgain, leaderboard, settings, exit);

        root.setBottom(buttonsBox);
        scene = new Scene(root, 600, 400);
        setStyles();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // allows for external css for styling
    protected void setStyles() {
        scene.getStylesheets().add("file:src/resources/css/gameover.css");
        root.getStyleClass().add("borderpane-root");
        buttonsBox.getStyleClass().add("vbox-buttonsbox");
        playAgain.getStyleClass().add("button");
        leaderboard.getStyleClass().add("button");
        settings.getStyleClass().add("button");
        exit.getStyleClass().add("button");
    }

    // function to create buttons with an icon next to them
    protected Button createButtonWithIcon(String text, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
        icon.setFitHeight(20);
        icon.setFitWidth(20);

        Button button = new Button(text, icon);
        return button;
    }

    // function to create labels with an icon next to them
    protected Label createLabelWithIcon(String text, String iconPath) {
        Label label = new Label(text);
        
        Image icon = new Image(getClass().getResourceAsStream(iconPath));
        ImageView iconView = new ImageView(icon);

        iconView.setFitHeight(25);
        iconView.setFitWidth(25);

        label.setGraphic(iconView);
        label.setContentDisplay(ContentDisplay.RIGHT);

        return label;
    }

    // abstract function will be implemented by all child classes
    protected abstract VBox createContent();

    // allows buttons to change guis or exit program
    protected void setButtonActions(Stage primaryStage) {
        playAgain.setOnAction(e -> {
            showGame(primaryStage);
        });

        leaderboard.setOnAction(e -> {
            showLeaderboard();
        });

        settings.setOnAction(e -> {
            showSettings();
        });

        exit.setOnAction(e -> {
            System.exit(0);
        });
    }

    // initializes game gui
    protected void showGame(Stage primaryStage) {
        gameController.initialize(primaryStage);
    }

    // initializes leaderboard gui
    protected void showLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.initialize(new Stage());
    }

    // initializes settings gui
    protected void showSettings() {
        Settings settings = new Settings(gameController);
        settings.initialize(new Stage());
    }
}
