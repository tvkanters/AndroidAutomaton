package com.tvkdevelopment.automaton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * The base class for automatons that input events onto an Android device to execute tasks.
 */
public class Automaton {

    /** The width of the Android device */
    public static final int SCREEN_WIDTH = 1080;

    /**
     * Inputs a single tap event through ADB and waits a moment for it to finish processing.
     *
     * @param target
     *            The target coordinate for the tap
     */
    public static void tap(final ScreenCoord target) {
        try {
            new ProcessBuilder("cmd", "/C", "adb shell input tap " + target.x + " " + target.y).start();
            Thread.sleep(180);
        } catch (final IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inputs a swipe tap event through ADB and waits a moment for it to finish processing.
     *
     * @param from
     *            The coordinate for the start of the swipe
     * @param to
     *            The coordinate for the end of the swipe
     */
    public static void swipe(final ScreenCoord from, final ScreenCoord to) {
        try {
            new ProcessBuilder("cmd", "/C",
                    "adb shell input touchscreen swipe " + from.x + " " + from.y + " " + to.x + " " + to.y).start();
            Thread.sleep(500);
        } catch (final IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Waits a moment.
     *
     * @param ms
     *            The amount of time to wait in milliseconds
     */
    public static void wait(final int ms) {
        System.out.println("wait(" + ms + ");");
        try {
            Thread.sleep(ms);
        } catch (final InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stores a screenshot of the Android device on local storage and waits a moment for it to finish processing.
     */
    public static void dumpScreen() {
        try {
            new ProcessBuilder("cmd", "/C", "adb shell screencap /storage/emulated/0/screen.dump").start();
            Thread.sleep(500);
        } catch (final IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the RGBA colours of a single pixels indicated by the given coordinates. The coordinates are looked up in the
     * last dumped screenshot, and thus relies on {@link Automaton#dumpScreen()} to be called first. Since ADB commands
     * have a lot of overhead, it is best to request all pixel colours at once.
     *
     * @param coords
     *            The coordinates for the pixels to find the colour of
     *
     * @return The RGBA colour of each targeted pixel
     */
    public static int[][] getColours(final List<ScreenCoord> coords) {
        // Construct the commands
        final List<StringBuilder> stringBuilders = new LinkedList<StringBuilder>();
        StringBuilder stringBuilder = null;
        for (final ScreenCoord coord : coords) {
            // Ensure there's a string builder to put the command in
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder();
                stringBuilders.add(stringBuilder);
            }

            // Add the command
            stringBuilder.append("dd if=/storage/emulated/0/screen.dump bs=4 count=1 skip="
                    + (coord.y * SCREEN_WIDTH + coord.x) + " 2>/dev/null;");

            // Split up the commands when they get too long - shell commands that are too long won't return anything
            if (stringBuilder.length() > 900) {
                stringBuilder = null;
            }
        }

        final int[][] rgba = new int[coords.size()][4];
        int rgbaIndex = 0;
        try {
            // Execute each shell command
            for (final StringBuilder command : stringBuilders) {
                final Process process = new ProcessBuilder("cmd", "/C", "adb shell \"" + command.toString() + "\"")
                        .start();

                // Read the command's result and store the colour values
                final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                int colour;
                while ((colour = reader.read()) != -1) {
                    rgba[rgbaIndex / 4][rgbaIndex % 4] = colour;
                    ++rgbaIndex;
                }
            }
            return rgba;

        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
