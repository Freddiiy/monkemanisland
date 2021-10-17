package com.monkeman.game;

public class Score {
    public static int score = 0;
    public static int highScore = 0;

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Score.score = score;
    }

    public static int getHighScore() {
        return highScore;
    }
    public static void updateHighScore() {
        if (highScore < score) {
            highScore = score;
        }
    }
}
