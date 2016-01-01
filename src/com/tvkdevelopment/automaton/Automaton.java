package com.tvkdevelopment.automaton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

/**
 * The base class for automatons that input events onto an Android device to execute tasks.
 */
public class Automaton {

    /** The last read screenshot, stored for quick access */
    private static BufferedImage mLastScreenShot;

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
     * Stores a screenshot of the Android device on local storage pulls it to the executeable's direction to load it
     * into memory.
     */
    public static void dumpScreen() {
        executeAndWait("adb shell screencap -p /storage/emulated/0/screen.png");
        executeAndWait("adb pull /storage/emulated/0/screen.png screen.png");
        try {
            mLastScreenShot = ImageIO.read(new File("screen.png"));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets the colour of a single pixel indicated by the given coordinates. The coordinates are looked up in the last
     * dumped screenshot, and thus relies on {@link #dumpScreen()} to be called first.
     *
     * @param coord
     *            The coordinates for the pixel to find the colour of
     *
     * @return The colour of the targeted pixel
     */
    public static Color getColour(final ScreenCoord coord) {
        return new Color(mLastScreenShot.getRGB(coord.x, coord.y));
    }

    /**
     * Executes a shell command and waits for it to complete.
     *
     * @param command
     *            The command to execute
     */
    private static void executeAndWait(final String command) {
        try {
            final ProcessBuilder pb = new ProcessBuilder("cmd", "/C", command);
            pb.redirectErrorStream(true);

            final Process p = pb.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (reader.readLine() != null) {
                // Eat line
            }

            p.waitFor();
        } catch (final IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }

}
