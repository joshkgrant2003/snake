package game;

import java.util.ArrayList;
import java.util.Random;

public class Food {
    // coordinates used to place on canvas
    private int x;
    private int y;

    // constructor needs snakeCoordinates to ensure food doesn't render where snake is
    public Food(int boardWidth, int boardHeight, ArrayList<int[]> snakeCoordinates) {
        placeRandomly(boardWidth, boardHeight, snakeCoordinates);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // ensures random placement of food while also avoiding where snake's body is
    public void placeRandomly(int boardWidth, int boardHeight, ArrayList<int[]> snakeCoordinates) {
        Random random = new Random();
        boolean isOccupied;

        do {
            this.x = random.nextInt(boardWidth);
            this.y = random.nextInt(boardHeight);

            isOccupied = false;
            for (int[] coord : snakeCoordinates) {
                if (coord[0] == this.x && coord[1] == this.y) {
                    isOccupied = true;
                    break;
                }
            }
        } while (isOccupied);
    }
}