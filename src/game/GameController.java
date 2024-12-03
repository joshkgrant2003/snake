package game;

import game.Snake.Segment;
import java.util.ArrayList;
import java.util.InputMismatchException;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class GameController {
    // instance variables that can be changed by user in settings menu
    private int TILE_SIZE = 50;
    private int WIDTH = 18;
    private int HEIGHT = 14;
    private long REFRESH_RATE = 100_000_000;
    private Color snakeColor = Color.GREEN;
    private Color foodColor = Color.RED;
    private boolean isSnakeColorRandom = false;
    private boolean isFoodColorRandom = false;
    private final Color[] colorList = {Color.RED, Color.YELLOW, Color.ORANGE, Color.GREEN, Color.BLUE, Color.PURPLE, Color.WHITE};
    
    // gui components
    private Leaderboard leaderboard = new Leaderboard();
    private int HIGHSCORE = leaderboard.getHighscore();
    private Snake snake;
    private Food food;
    private boolean isRunning = false;
    private AnimationTimer timer;
    private Canvas gridCanvas;
    private Canvas gameCanvas;
    private GraphicsContext gridGc;
    private GraphicsContext gameGc;
    private VBox root;
    private Scene scene;
    private Label scoreLabel;

    public void initialize(Stage primaryStage) {
        // init snake in middle of screen and food at random location
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        food = new Food(WIDTH, HEIGHT, getSnakeCoordinates());

        // grid canvas and gc is used for static elements of board, while
        // game canvas and gc is for dynamic elements such as snake and food
        gridCanvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        gameCanvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        gridGc = gridCanvas.getGraphicsContext2D();
        gameGc = gameCanvas.getGraphicsContext2D();

        // draw initial state of game
        initialDraw();

        // initialize and add gui components
        scoreLabel = new Label("Score: " + snake.getScore());
        root = new VBox();
        root.getChildren().addAll(scoreLabel, new Pane(gridCanvas, gameCanvas));

        // create scene, add styles, and add listeners for moving snake
        scene = new Scene(root);
        setStyles();
        scene.setOnKeyPressed(event -> {
            if (!isRunning) {
                isRunning = true;
            }

            if (event.getCode() == KeyCode.UP) {
                snake.setDirection(Snake.Direction.UP);
            } else if (event.getCode() == KeyCode.DOWN) {
                snake.setDirection(Snake.Direction.DOWN);
            } else if (event.getCode() == KeyCode.LEFT) {
                snake.setDirection(Snake.Direction.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                snake.setDirection(Snake.Direction.RIGHT);
            }
        });

        // setup game loop using AnimationTimer()
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // interval of (REFRESH_RATE / 1_000_000) ms
                if (now - lastUpdate >= REFRESH_RATE) {
                    if (isRunning) {
                        snake.move();

                        // handle food being eaten
                        if (snake.isEatingFood(food)) {
                            snake.grow();
                            food.placeRandomly(WIDTH, HEIGHT, getSnakeCoordinates());
                            updateScore();
                        }

                        draw();
                    
                        // check for collisions
                        if (snake.hasSelfCollided() || isOutOfBounds(snake.getBody().getFirst()) || hasWon()) {
                            stop();
                            isRunning = false;
                            EnterScore enterScore = new EnterScore();
                            enterScore.initialize(new Stage(), snake.getScore());

                            if (hasWon()) {
                                new GameOverWinner(primaryStage, getThisGameController()).initialize();
                            } else if (snake.getScore() > HIGHSCORE) {
                                new GameOverHighScore(primaryStage, getThisGameController()).initialize();
                            } else {
                                new GameOverBase(primaryStage, getThisGameController()).initialize();
                            }
                        }
                    }
                    lastUpdate = now;
                }
            }
        };

        timer.start();
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initialDraw() {
        // draw background
        gridGc.setFill(Color.valueOf("#0a0e19"));
        gridGc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // draw gridlines
        gridGc.setStroke(Color.valueOf("#040811"));
        for (int x = 0; x < WIDTH; x++) {
            gridGc.strokeLine(x * TILE_SIZE, 0, x * TILE_SIZE, HEIGHT * TILE_SIZE);
        }
        for (int y = 0; y < HEIGHT; y++) {
            gridGc.strokeLine(0, y * TILE_SIZE, WIDTH * TILE_SIZE, y * TILE_SIZE);
        }

        // draw initial snake
        gameGc.setFill(snakeColor);
        drawSnakeHead(snake.getBody().getFirst(), TILE_SIZE);

        // draw initial food
        gameGc.setFill(foodColor);
        gameGc.fillOval(
            food.getX() * TILE_SIZE + 5,
            food.getY() * TILE_SIZE + 5,
            TILE_SIZE - 10,
            TILE_SIZE - 10
        );
    }

    private void draw() {
        // clear the canvas
        gameGc.setFill(Color.valueOf("#0a0e19"));
        gameGc.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        // handle random color if snake is random color
        if (isSnakeColorRandom) {
            snakeColor = colorList[(int) (Math.random() * colorList.length)];
        }
        
        // draw the snake
        Segment head = snake.getBody().getFirst();
        for (Segment segment : snake.getBody()) {
            gameGc.setFill(snakeColor);
            if (segment == head) {
                drawSnakeHead(head, TILE_SIZE);
            } else {
                gameGc.fillRect(
                    (segment.getX() * TILE_SIZE) + 4,
                    (segment.getY() * TILE_SIZE) + 4,
                    TILE_SIZE - 8,
                    TILE_SIZE - 8
                );
            }
        }

        // handle random color if food is random color
        if (isFoodColorRandom) {
            foodColor = colorList[(int) (Math.random() * colorList.length)];
        }

        // draw the food
        gameGc.setFill(foodColor);
        gameGc.fillOval(
            food.getX() * TILE_SIZE + 5,
            food.getY() * TILE_SIZE + 5,
            TILE_SIZE - 10,
            TILE_SIZE - 10
        );
    }

    // helper function to style snake head
    private void drawSnakeHead(Segment head, int TILE_SIZE) {
        gameGc.fillRect(
            (head.getX() * TILE_SIZE) + 4,
            (head.getY() * TILE_SIZE) + 4,
            TILE_SIZE - 8,
            TILE_SIZE - 8
        );

        // the values must be different dependent on board size
        int eyeX, eyeY, eyeWidth, eyeHeight, pupilX, pupilY, pupilWidth, pupilHeight;
        int mouthLineWidth, mouthX, mouthY, mouthWidth, mouthHeight;
        int mouthStartAngle = 180, mouthArcExtent = 180;
        
        if (TILE_SIZE == 80) {   // small board size
            eyeX = 15;
            eyeY = 20;
            eyeWidth = 12;
            eyeHeight = 12;
            pupilX = 20;
            pupilY = 24;
            pupilWidth = 5;
            pupilHeight = 5;
            mouthLineWidth = 3;
            mouthX = 18;
            mouthY = 25;
            mouthWidth = 35; // 30
            mouthHeight = 10;
        } else if (TILE_SIZE == 50) {   // medium board size
            eyeX = 10;
            eyeY = 12;
            eyeWidth = 8;
            eyeHeight = 8;
            pupilX = 13;
            pupilY = 15;
            pupilWidth = 4;
            pupilHeight = 4;
            mouthLineWidth = 2;
            mouthX = 10;
            mouthY = 18;
            mouthWidth = 20;
            mouthHeight = 8;
        } else if (TILE_SIZE == 40) {   // large board size
            eyeX = 8;
            eyeY = 10;
            eyeWidth = 6;
            eyeHeight = 6;
            pupilX = 10;
            pupilY = 12;
            pupilWidth = 3;
            pupilHeight = 3;
            mouthLineWidth = 2;
            mouthX = 7;
            mouthY = 14;
            mouthWidth = 15;
            mouthHeight = 6;
        } else {
            throw new InputMismatchException("Unsupported TILE_SIZE: " + TILE_SIZE);
        }

        // left eye
        gameGc.setFill(Color.WHITE);
        gameGc.fillOval(
            head.getX() * TILE_SIZE + eyeX,
            head.getY() * TILE_SIZE + eyeY,
            eyeWidth,
            eyeHeight
        );

        // right eye
        gameGc.fillOval(
            head.getX() * TILE_SIZE + TILE_SIZE - eyeX - eyeWidth,
            head.getY() * TILE_SIZE + eyeY,
            eyeWidth,
            eyeHeight
        );

        // left pupil
        gameGc.setFill(Color.BLACK);
        gameGc.fillOval(
            head.getX() * TILE_SIZE + pupilX,
            head.getY() * TILE_SIZE + pupilY,
            pupilWidth,
            pupilHeight
        );

        // right pupil
        gameGc.fillOval(
            head.getX() * TILE_SIZE + TILE_SIZE - pupilX - pupilWidth,
            head.getY() * TILE_SIZE + pupilY,
            pupilWidth,
            pupilHeight
        );

        // mouth
        gameGc.setStroke(Color.BLACK);
        gameGc.setLineWidth(mouthLineWidth);
        gameGc.strokeArc(
            head.getX() * TILE_SIZE + mouthX,
            head.getY() * TILE_SIZE + TILE_SIZE - mouthY,
            TILE_SIZE - mouthWidth,
            mouthHeight,
            mouthStartAngle,
            mouthArcExtent,
            ArcType.ROUND
        );
    }

    // set styles for gamecontroller gui components
    private void setStyles() {
        scene.getStylesheets().add("file:src/resources/css/gameController.css");
        root.getStyleClass().add("vbox-root");
        scoreLabel.getStyleClass().add("score-label");
    }

    // gets [x, y] pair of where snake is to ensure food doesn't render there
    private ArrayList<int[]> getSnakeCoordinates() {
        ArrayList<int[]> coordinates = new ArrayList<>();
        for (Segment segment : snake.getBody()) {
            int[] coordPair = {segment.getX(), segment.getY()};
            coordinates.add(coordPair);
        }
        return coordinates;
    }

    // sets speed of snake based on user input
    public void setSnakeSpeed(SnakeSpeed newSpeed) {
        if (newSpeed == SnakeSpeed.SLOW) {
            REFRESH_RATE = 150_000_000;
        } else if (newSpeed == SnakeSpeed.NORMAL) {
            REFRESH_RATE = 100_000_000;
        } else if (newSpeed == SnakeSpeed.FAST) {
            REFRESH_RATE = 50_000_000;
        } else {
            throw new InputMismatchException();
        }
    }

    // sets board size to small, medium, or large
    public void setBoardSize(BoardSize newSize) {
        if (newSize == BoardSize.SMALL) {
            WIDTH = 10;
            HEIGHT = 8;
            TILE_SIZE = 80;
        } else if (newSize == BoardSize.MEDIUM) {
            WIDTH = 18;
            HEIGHT = 14;
            TILE_SIZE = 50;
        } else if (newSize == BoardSize.LARGE) {
            WIDTH = 24;
            HEIGHT = 17;
            TILE_SIZE = 40;
        } else {
            throw new InputMismatchException();
        }
    }

    // sets color based on what user chooses
    public void setSnakeColor(SnakeColor newColor) {
        if (newColor == SnakeColor.DISCO) {
            isSnakeColorRandom = true;
        } else {
            isSnakeColorRandom = false;
            switch (newColor) {
                case RED -> snakeColor = Color.RED;
                case YELLOW -> snakeColor = Color.YELLOW;
                case ORANGE -> snakeColor = Color.ORANGE;
                case GREEN -> snakeColor = Color.GREEN;
                case BLUE -> snakeColor = Color.BLUE;
                case PURPLE -> snakeColor = Color.PURPLE;
                case WHITE -> snakeColor = Color.WHITE;
                case DISCO -> {} // do nothing bc this is handled later
            }
        }
    }

    // sets color based on what user chooses
    public void setFoodColor(FoodColor newColor) {
        if (newColor == FoodColor.DISCO) {
            isFoodColorRandom = true;
        } else {
            isFoodColorRandom = false;
            switch (newColor) {
                case RED -> foodColor = Color.RED;
                case YELLOW -> foodColor = Color.YELLOW;
                case ORANGE -> foodColor = Color.ORANGE;
                case GREEN -> foodColor = Color.GREEN;
                case BLUE -> foodColor = Color.BLUE;
                case PURPLE -> foodColor = Color.PURPLE;
                case WHITE -> foodColor = Color.WHITE;
                case DISCO -> {} // do nothing bc this is handled later
            }
        }
    }

    // updates score as snake grows
    private void updateScore() {
        scoreLabel.setText("Score: " + snake.getScore());
    }

    // detects winner
    private boolean hasWon() {
        int gridSize = HEIGHT * WIDTH;
        return snake.getBody().size() == gridSize;
    }

    // checks for collision with border
    private boolean isOutOfBounds(Segment head) {
        return head.getX() < 0 || head.getX() >= WIDTH || head.getY() < 0 || head.getY() >= HEIGHT;
    }

    // for the colors to update correctly, the same GameController() object must be
    // used all throughout the program
    private GameController getThisGameController() {
        return this;
    }

    // used to set refresh rate
    enum SnakeSpeed {
        SLOW, NORMAL, FAST;
    }

    // constants used to define size of board
    enum BoardSize {
        SMALL, MEDIUM, LARGE;
    }

    // used to set color of snake segments
    enum SnakeColor {
        RED, YELLOW, ORANGE, GREEN, BLUE, PURPLE, WHITE, DISCO;
    }

    // used to set food color
    enum FoodColor {
        RED, YELLOW, ORANGE, GREEN, BLUE, PURPLE, WHITE, DISCO;
    }
}
