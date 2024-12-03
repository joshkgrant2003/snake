package game;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Start {
    private GameController gameController;

    // initialize gui components
    private Scene scene;
    private VBox layout;
    private ImageView logo;
    private Button play;
    private Button leaderboard;
    private Button settings;
    private Button exit;

    // constructor will handle initializing components
    public Start(GameController gameController) {
        // pass in gameController to keep same obj thruout runtime
        this.gameController = gameController;
        layout = new VBox(20);
        logo = new ImageView(new Image("file:src/resources/images/Snake_Logo.jpg"));
        play = createButtonWithIcon(" Play", "/resources/images/playIcon.png");
        leaderboard = createButtonWithIcon(" Leaderboard", "/resources/images/leaderboardIcon.png");
        settings = createButtonWithIcon(" Settings", "/resources/images/settingsIcon.png");
        exit = createButtonWithIcon(" Exit", "/resources/images/exitIcon.png");
    }

    // sets styles, button functionality, and layout
    public void initialize(Stage primaryStage) {
        setButtonActions(primaryStage);
        layout.getChildren().addAll(logo, play, leaderboard, settings, exit);
        scene = new Scene(layout, 600, 650);
        setStyles();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake");
    }

    // adds styles for start gui components
    private void setStyles() {
        scene.getStylesheets().add("file:src/resources/css/start.css");
        layout.getStyleClass().add("vbox-layout");
        logo.getStyleClass().add("image-view");
        play.getStyleClass().add("button");
        leaderboard.getStyleClass().add("button");
        settings.getStyleClass().add("button");
        exit.getStyleClass().add("button");
    }

    // create button with icon for UX improvement
    private Button createButtonWithIcon(String text, String iconPath) {
        ImageView icon = new ImageView(new Image(getClass().getResource(iconPath).toExternalForm()));
        icon.setFitHeight(20);
        icon.setFitWidth(20);

        Button button = new Button(text, icon);
        return button;
    }

    // adds functionality for buttons to go to other guis
    private void setButtonActions(Stage primaryStage) {
        play.setOnAction(e -> {
            showGame(primaryStage);
        });

        leaderboard.setOnAction(e -> {
            showLeaderboard();
        });

        settings.setOnAction(e -> {
            showSettings(primaryStage);
        });

        exit.setOnAction(e -> {
            System.exit(0);
        });
    }

    // initializes main game gui when user presses play button
    private void showGame(Stage primaryStage) {
        gameController.initialize(primaryStage);
    }

    // shows leaderboard in front of start gui
    private void showLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.initialize(new Stage());
    }

    // shows settings menu in front of start gui
    private void showSettings(Stage primaryStage) {
        Settings settings = new Settings(gameController);
        settings.initialize(new Stage());
    }
}
