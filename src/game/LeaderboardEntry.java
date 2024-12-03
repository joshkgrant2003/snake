package game;

// this class holds a user's name and score when they finish a game
public class LeaderboardEntry {
    private String name;
    private final int score;

    public LeaderboardEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        // used to convert object to .csv format
        return name + "," + score;
    }
}
