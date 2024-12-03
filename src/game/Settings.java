package game;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Settings {
    private GameController gameController;

    // declare instance variables for gui
    private Scene scene;
    private VBox layout;
    private Label snakeSpeedLabel;
    private Label boardSizeLabel;
    private Label snakeColorLabel;
    private Label foodColorLabel;
    private ComboBox<GameController.SnakeSpeed> snakeSpeedOptionsBox;
    private ComboBox<GameController.BoardSize> boardSizeOptionsBox;
    private ComboBox<GameController.SnakeColor> snakeColorOptionsBox;
    private ComboBox<GameController.FoodColor> foodColorOptionsBox;
    private HBox snakeSpeedSetting;
    private HBox boardSizeSetting;
    private HBox snakeColorSetting;
    private HBox foodColorSetting;
    private Button apply;
    private Button cancel;

    // gameController obj passed in so settings apply to the same gameController
    // and not a new one
    public Settings(GameController gameController) {
        this.gameController = gameController;
    }

    // initialize all gui components and build gui
    public void initialize(Stage primaryStage) {
        snakeSpeedLabel = new Label("Set Snake Speed:");
        boardSizeLabel = new Label("Set Board Size:");
        snakeColorLabel = new Label("Set Snake Color:");
        foodColorLabel = new Label("Set Food Color:");

        snakeSpeedOptionsBox = new ComboBox<>(
            FXCollections.observableArrayList(GameController.SnakeSpeed.values())
        );
        boardSizeOptionsBox = new ComboBox<>(
            FXCollections.observableArrayList(GameController.BoardSize.values())
        );
        snakeColorOptionsBox = new ComboBox<>(
            FXCollections.observableArrayList(GameController.SnakeColor.values())
        );
        foodColorOptionsBox = new ComboBox<>(
            FXCollections.observableArrayList(GameController.FoodColor.values())
        );

        snakeSpeedSetting = new HBox(10, snakeSpeedLabel, snakeSpeedOptionsBox);
        boardSizeSetting = new HBox(10, boardSizeLabel, boardSizeOptionsBox);
        snakeColorSetting = new HBox(10, snakeColorLabel, snakeColorOptionsBox);
        foodColorSetting = new HBox(10, foodColorLabel, foodColorOptionsBox);

        setDefaultOptions();

        apply = new Button("Apply");
        cancel = new Button("Cancel");
        setButtonActions(gameController);

        layout = new VBox(20);
        layout.getChildren().addAll(
            snakeSpeedSetting, boardSizeSetting, snakeColorSetting, foodColorSetting, apply, cancel
        );
    
        scene = new Scene(layout, 400, 400);
        setStyles();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Settings");
        primaryStage.show();
    }

    // sets value of options boxes to defaults
    private void setDefaultOptions() {
        snakeSpeedOptionsBox.setValue(GameController.SnakeSpeed.NORMAL);
        boardSizeOptionsBox.setValue(GameController.BoardSize.MEDIUM);
        snakeColorOptionsBox.setValue(GameController.SnakeColor.GREEN);
        foodColorOptionsBox.setValue(GameController.FoodColor.RED);
    }

    private void setButtonActions(GameController gameController) {
        // applies changes made by user and closes gui
        apply.setOnAction(e -> {
            gameController.setSnakeSpeed(snakeSpeedOptionsBox.getValue());
            gameController.setBoardSize(boardSizeOptionsBox.getValue());
            gameController.setSnakeColor(snakeColorOptionsBox.getValue());
            gameController.setFoodColor(foodColorOptionsBox.getValue());
            
            Stage currentStage = (Stage) cancel.getScene().getWindow();
            currentStage.close();
        });

        // closes gui
        cancel.setOnAction(e -> {
            Stage currentStage = (Stage) cancel.getScene().getWindow();
            currentStage.close();
        });
    }

    // uses external css to style settings menu
    private void setStyles() {
        scene.getStylesheets().add("file:src/resources/css/settings.css");
        layout.getStyleClass().add("vbox-layout");
        snakeSpeedLabel.getStyleClass().add("settings-label");
        boardSizeLabel.getStyleClass().add("settings-label");
        snakeColorLabel.getStyleClass().add("settings-label");
        foodColorLabel.getStyleClass().add("settings-label");
        snakeSpeedOptionsBox.getStyleClass().add("combo-box");
        boardSizeOptionsBox.getStyleClass().add("combo-box");
        snakeColorOptionsBox.getStyleClass().add("combo-box");
        foodColorOptionsBox.getStyleClass().add("combo-box");
        snakeSpeedSetting.getStyleClass().add("hbox-settings");
        boardSizeSetting.getStyleClass().add("hbox-settings");
        snakeColorSetting.getStyleClass().add("hbox-settings");
        foodColorSetting.getStyleClass().add("hbox-settings");
        apply.getStyleClass().add("button");
        cancel.getStyleClass().add("button");
    }
}
