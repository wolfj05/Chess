package at.ac.hcw.chess.gameutils;

import javafx.animation.AnimationTimer;

public class Clock {

    private long whiteTime;
    private long blackTime;

    private long lastUpdate = 0;
    private boolean runningWhite = true;

    private AnimationTimer timer;

    public Clock(long initialTimeMillis) {
        whiteTime = initialTimeMillis;
        blackTime = initialTimeMillis;

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                long delta = (now - lastUpdate) / 1_000_000; // ns → ms
                lastUpdate = now;

                if (runningWhite) {
                    whiteTime -= delta;
                    if (whiteTime <= 0) {
                        whiteTime = 0;
                        stop();
                    }
                } else {
                    blackTime -= delta;
                    if (blackTime <= 0) {
                        blackTime = 0;
                        stop();
                    }
                }
            }
        };
    }

    public void start() {
        lastUpdate = 0;
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void switchTurn() {
        runningWhite = !runningWhite;
    }

    public long getWhiteTime() {
        return whiteTime;
    }

    public long getBlackTime() {
        return blackTime;
    }

    public String format(long ms) {
        long totalSec = ms / 1000;
        long min = totalSec / 60;
        long sec = totalSec % 60;
        return String.format("%02d:%02d", min, sec);
    }
}