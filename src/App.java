import game.Start;
import game.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        // this is the only gameController that will be made, the same one must be
        // passed thru the program for the colors to update correctly
        Start startScreen = new Start(new GameController());
        startScreen.initialize(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
