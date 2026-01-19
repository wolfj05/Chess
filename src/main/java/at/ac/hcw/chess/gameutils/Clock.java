package at.ac.hcw.chess.gameutils;

import javafx.animation.AnimationTimer;

public class Clock {

    private long whiteTime;
    private long blackTime;

    private final long initialTimeMillis;

    private long lastUpdate = 0;
    private boolean runningWhite = true;

    private AnimationTimer timer;

    public Clock(long initialTimeMillis) {
        this.initialTimeMillis = initialTimeMillis;

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

    /**
     * start clock
     */
    public void start() {
        lastUpdate = 0;
        timer.start();
    }

    /**
     * stop clock
     */
    public void stop() {
        timer.stop();
    }

    /**
     * toggle turn
     */
    public void switchTurn() {
        runningWhite = !runningWhite;
    }

    /**
     * reset whole clock (both timers)
     */
    public void reset() {
        // 1) Timer stoppen
        timer.stop();

        // 2) Zeiten auf den Originalwert zurücksetzen
        whiteTime = initialTimeMillis;
        blackTime = initialTimeMillis;

        // 3) Weiße Seite beginnt
        runningWhite = true;

        // 4) lastUpdate zurücksetzen, damit keine langen Sprünge passieren
        lastUpdate = 0;
    }

    /** 
     * @return white time
     */
    public long getWhiteTime() {
        return whiteTime;
    }

    /** 
     * @return black time
     */
    public long getBlackTime() {
        return blackTime;
    }

    /** 
     * @param ms time
     * @return formatted String of the time
     */
    public String format(long ms) {
        long totalSec = ms / 1000;
        long min = totalSec / 60;
        long sec = totalSec % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
