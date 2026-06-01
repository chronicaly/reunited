package dev.unityclient.util;

public final class Timer {
    private long time = System.currentTimeMillis();

    public boolean passed(long millis) {
        return System.currentTimeMillis() - time >= millis;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }
}
