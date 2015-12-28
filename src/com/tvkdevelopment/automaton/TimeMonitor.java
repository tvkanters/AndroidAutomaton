package com.tvkdevelopment.automaton;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class for monitor how long certain actions take in a stopwatch-like fashion.
 */
public class TimeMonitor {

    /** The registered keys, stored in order for printing purposes */
    private static final Set<String> sKeys = new LinkedHashSet<>();
    /** The active monitor's start times for each key */
    private static final Map<String, Long> sStartTimes = new HashMap<>();
    /** The elapsed times, stored upon stopping a key */
    private static final Map<String, Long> sElapsed = new HashMap<>();

    /**
     * Registers a key. Useful for defining the printing order.
     *
     * @param key
     *            The key to register
     */
    public static void register(final String key) {
        sKeys.add(key);
    }

    /**
     * Starts monitor time mapped by the given key.
     *
     * @param key
     *            The key used to monitor time
     */
    public static void start(final String key) {
        if (sStartTimes.containsKey(key)) {
            throw new RuntimeException("Key " + key + " already started");
        }
        register(key);
        sStartTimes.put(key, System.currentTimeMillis());
    }

    /**
     * Stops monitor time mapped by the given key and stores the amount of elapsed time.
     *
     * @param key
     *            The key used to monitor time
     *
     * @return The amount of milliseconds elapsed
     */
    public static long stop(final String key) {
        final long endTime = System.currentTimeMillis();

        if (!sStartTimes.containsKey(key)) {
            throw new RuntimeException("Key " + key + " not started");
        }

        final long elapsed = endTime - sStartTimes.get(key);
        sElapsed.put(key, getElapsed(key) + elapsed);
        sStartTimes.remove(key);
        return elapsed;
    }

    /**
     * Retrieves the total elapsed time for the given key.
     *
     * @param key
     *            The key used to monitor time
     *
     * @return The total amount of milliseconds elapsed
     */
    public static long getElapsed(final String key) {
        return sElapsed.getOrDefault(key, 0L);
    }

    /**
     * Prints the total elapsed times for all monitored keys.
     */
    public static void printElapsed() {
        System.out.println("--- Time elapsed ---");

        final long totalElapsed = sElapsed.values().stream().mapToLong(e -> e.longValue()).sum();

        final int longestKeyLength = sKeys.stream().mapToInt(k -> k.length()).max().getAsInt();
        for (final String key : sKeys) {
            System.out.print(key);
            System.out.format("%" + (longestKeyLength - key.length() + 1) + "s", "");

            final long elapsed = getElapsed(key);
            final String elapsedStr = String.valueOf(elapsed);
            System.out.format("%" + (7 - elapsedStr.length()) + "s", "");
            System.out.print(elapsedStr + "ms");

            final int percent = (int) (100 * elapsed / totalElapsed);
            final String percentStr = String.valueOf(percent);
            System.out.format("%" + (4 - percentStr.length()) + "s", "");
            System.out.print(percent + "%");
            System.out.println();
        }
    }

}
