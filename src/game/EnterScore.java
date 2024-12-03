package game;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.data.FileManip;

// this class inherits methods from Leaderboard, since this class depends on leaderboard methods
public class EnterScore extends Leaderboard {
    private VBox layout;
    private Scene scene;
    private Label prompt;
    private TextField usernameField;
    private Button submit;

    // renders scorebox and handles submit action of saving data to file and closing out gui
    void initialize(Stage primaryStage, int score) {
        prompt = new Label("Enter Your Username:");
        usernameField = new TextField();
        submit = new Button("Submit");
        submit.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                saveScore(username, score);
                Leaderboard leaderboard = new Leaderboard();
                leaderboard.initialize(primaryStage);
            } else {
                prompt.setText("Username cannot be empty.");
            }
        });

        layout = new VBox(10, prompt, usernameField, submit);
        scene = new Scene(layout, 300, 200);
        setStyles();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Over");
        primaryStage.show();
    }

    private void setStyles() {
        scene.getStylesheets().add("file:src/resources/css/enterScoreBox.css");
        layout.getStyleClass().add("vbox-layout");
        prompt.getStyleClass().add("label-prompt");
        usernameField.getStyleClass().add("textfield-username");
        submit.getStyleClass().add("button");
    }

    // saves new score to file and displays on leaderboard
    private void saveScore(String username, int score) {
        ArrayList<LeaderboardEntry> users = getUsers();
        LeaderboardEntry newEntry = new LeaderboardEntry(username, score);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            list.add(users.get(i).toString());
        }
        list.add(newEntry.toString());
        FileManip.writeFile(list, "src\\resources\\data\\data.csv");
    }
}
