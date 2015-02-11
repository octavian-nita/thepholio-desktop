package org.thepholio;

/**
 * @author Octavian Theodor Nita (https://github.com/octavian-nita)
 * @version 1.0, Jan 27, 2015
 */
public class Stopwatch {

    public static final Stopwatch SW = new Stopwatch();

    private volatile long start;

    private volatile long stop;

    public long elapsed() {
        return stop - start;
    }

    public long elapsedSeconds() {
        return elapsed() / 1000;
    }

    public Stopwatch reset() {
        start = stop = 0;
        return this;
    }

    public Stopwatch start() {
        start = System.currentTimeMillis();
        return this;
    }

    public Stopwatch stop() {
        stop = System.currentTimeMillis();
        return this;
    }

    public Stopwatch restart() {
        return reset().start();
    }

    public void report() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return elapsedSeconds() + "s (" + elapsed() + "ms)";
    }
}
