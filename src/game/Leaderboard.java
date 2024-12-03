package game;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.data.FileManip;

public class Leaderboard {
    // data structures and gui components needed for leaderboard
    private ArrayList<LeaderboardEntry> users;
    private ObservableList<LeaderboardEntry> entries;
    private TableView<LeaderboardEntry> tableView;
    private TableColumn<LeaderboardEntry, String> nameColumn;
    private TableColumn<LeaderboardEntry, Integer> scoreColumn;
    private VBox layout;
    private Scene scene;

    // loads data on object initialization
    public Leaderboard() {
        try {
            loadData("src\\resources\\data\\data.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // initializes gui components and renders them
    public void initialize(Stage primaryStage) {
        entries = FXCollections.observableArrayList(users);
        tableView = new TableView<>(entries);

        nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(250);

        scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setMinWidth(110);

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(scoreColumn);

        layout = new VBox(tableView);
        scene = new Scene(layout, 400, 300);
        setStyles();
        primaryStage.setTitle("Leaderboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // adds external css to gui components
    private void setStyles() {
        scene.getStylesheets().add("file:src/resources/css/leaderboard.css");
        tableView.getStyleClass().add("table-view");
        nameColumn.getStyleClass().add("table-column");
        scoreColumn.getStyleClass().add("table-column");
        layout.getStyleClass().add("vbox-layout");
    }

    // loads data from file and sorts it by score
    private void loadData(String filename) throws FileNotFoundException {
        users = new ArrayList<>();
        ArrayList<String> list = FileManip.readFile(filename);

        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] fields = str.split(",");
            LeaderboardEntry user = new LeaderboardEntry(
                fields[0].trim(),
                Integer.parseInt(fields[1].trim())
            );
            users.add(user);
        }

        // sorts from greatest -> least
        users.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
    }

    // needed for EnterScore child class
    protected ArrayList<LeaderboardEntry> getUsers() {
        return users;
    }

    // gets highscore from users arraylist, called in GameController
    public int getHighscore() {
        if (users.isEmpty()) {
            return 0;
        }
        return users.get(0).getScore();
    }
}
