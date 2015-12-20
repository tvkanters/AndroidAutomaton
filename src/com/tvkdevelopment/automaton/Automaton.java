package com.tvkdevelopment.automaton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            Thread.sleep(200);
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
     * Gets the RGBA colour of a single pixel indicated by the given coordinate. The coordinate is lookup up in the last
     * dumped screenshot, and thus relies on {@link Automaton#dumpScreen()} to be called first.
     *
     * @param coord
     *            The coordinate for the pixel to find the colour of
     * 
     * @return The RGBA colour of the targeted pixel
     */
    public static int[] getColor(final ScreenCoord coord) {
        try {
            final Process process = new ProcessBuilder("cmd", "/C",
                    "adb shell \"dd if=/storage/emulated/0/screen.dump bs=4 count=1 skip="
                            + (coord.y * SCREEN_WIDTH + coord.x) + " 2>/dev/null\"").start();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            final int[] rgba = new int[4];
            for (int i = 0; i < 4; ++i) {
                rgba[i] = reader.read();
            }
            return rgba;

        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
