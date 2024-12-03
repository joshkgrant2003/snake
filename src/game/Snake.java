package game;

import java.util.LinkedList;
import java.util.Queue;

public class Snake {
    // instance variables
    private LinkedList<Segment> body;
    private Queue<Direction> directionQueue;
    private Direction currentDirection;
    private boolean isFirstMove;
    private int score = 1;

    // constructor creates body and sets starting position and direction
    public Snake(int startX, int startY) {
        body = new LinkedList<>();
        body.add(new Segment(startX, startY));
        directionQueue = new LinkedList<>();
        currentDirection = Direction.UP;
        isFirstMove = true;
    }

    // getter for body
    public LinkedList<Segment> getBody() {
        return body;
    }

    // getter for score
    public int getScore() {
        return score;
    }

    // moves snake in current direction
    public void move() {
        // update current direction before moving
        // this is needed to process rapid succession of direction changes
        if (!directionQueue.isEmpty()) {
            currentDirection = directionQueue.poll();
        }

        Segment head = body.getFirst();
        int newX = head.x;
        int newY = head.y;

        switch (currentDirection) {
            case UP:
                newY -= 1;
                break;
            case DOWN:
                newY += 1;
                break;
            case LEFT:
                newX -= 1;
                break;
            case RIGHT:
                newX += 1;
                break;
        }

        // add new head position, then remove last to simulate movement
        body.addFirst(new Segment(newX, newY));
        body.removeLast();
    }

    // grows snake, called every time food is eaten
    public void grow() {
        Segment tail = body.getLast();
        Segment newTail = new Segment(tail.x, tail.y);
        
        switch (currentDirection) {
            case UP -> newTail.y += 1;
            case DOWN -> newTail.y -= 1;
            case LEFT -> newTail.x += 1;
            case RIGHT -> newTail.x -= 1;
        }
        
        body.addLast(newTail);
        score++;
    }

    // checks for if head node has hit another part of the snake
    public boolean hasSelfCollided() {
        Segment head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    // sets direction to new direction based on user input
    public void setDirection(Direction newDirection) {
        // must ensure snake cannot reverse completely backwards
        if ((currentDirection == Direction.UP && newDirection != Direction.DOWN) ||
            (currentDirection == Direction.DOWN && newDirection != Direction.UP) ||
            (currentDirection == Direction.LEFT && newDirection != Direction.RIGHT) ||
            (currentDirection == Direction.RIGHT && newDirection != Direction.LEFT)) {
            directionQueue.offer(newDirection);
        } else if (isFirstMove) {
            directionQueue.offer(newDirection);
            isFirstMove = false;
        }
    }

    // check if head is at same coordinate as food
    public boolean isEatingFood(Food food) {
        if (body.getFirst().x == food.getX() &&
            body.getFirst().y == food.getY()) {
            return true;
        }
        return false;
    }
 
    // internal class represents each segment of snake
    public static class Segment {
        private int x, y;

        Segment(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        // allows us to check when two segments are equal
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            else if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Segment segment = (Segment) obj;
            return x == segment.x && y == segment.y;
        }

        // must be overridden when equals() is overridden to maintain "contract" between equals() and hashCode()
        @Override
        public int hashCode() {
            return 31 * (x + y);
        }
    }

    // enum is a group of constants that don't change
    // this enum allows us to define our 4 "moveable" directions
    enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }
}
