package com.tvkdevelopment.automaton.roots;

import com.tvkdevelopment.automaton.Automaton;
import com.tvkdevelopment.automaton.ScreenCoord;

/**
 * An automaton for the initial phase of level 35 of the game Roots.
 *
 * While I knew strategically how to beat the level, I couldn't succeed in inputting the commands fast enough resulting
 * in a stale-mate. This class does that for you up to a point where you can finish the rest of the level manually.
 */
public class Roots35 extends Automaton {

    /** The amount of columns in the level */
    private static final int COLUMN_MAX = 10;
    /** The amount of rows in the level */
    private static final int ROW_MAX = 5;

    /** The top left screen coordinate of the level */
    private static final ScreenCoord TOP_LEFT = new ScreenCoord(150, 150);
    /** The bottom right screen coordinate of the level */
    private static final ScreenCoord BOTTOM_RIGHT = new ScreenCoord(1775, 950);

    /** The screen coordinate interval between each node */
    private static final ScreenCoord INTERVAL = new ScreenCoord((BOTTOM_RIGHT.x - TOP_LEFT.x) / COLUMN_MAX,
            (BOTTOM_RIGHT.y - TOP_LEFT.y) / ROW_MAX);

    public static void main(final String[] args) {
        System.out.println("START");

        tap(new ScreenCoord(1500, 950));
        wait(800);

        swipe(10, 0, 8, 0);
        swipe(10, 5, 8, 3);
        wait(13500);

        // Initial rush
        swipe(8, 0, 6, 0);
        swipe(8, 3, 6, 1);
        wait(11500);
        swipe(6, 0, 4, 0);
        swipe(6, 1, 4, 1);
        wait(8500);
        swipe(4, 0, 3, 0);
        swipe(4, 1, 3, 2);

        // Back up and spread
        swipe(4, 0, 6, 0);
        swipe(6, 0, 5, 0);
        swipe(6, 0, 7, 1);

        swipe(6, 0, 8, 0);
        swipe(8, 0, 7, 0);
        swipe(8, 0, 8, 1);

        swipe(6, 1, 8, 3);
        swipe(8, 3, 7, 2);
        swipe(8, 3, 8, 2);

        swipe(8, 0, 10, 0);
        swipe(10, 0, 9, 0);
        swipe(10, 0, 10, 1);

        swipe(8, 3, 10, 5);
        swipe(10, 5, 9, 4);
        swipe(10, 5, 10, 4);

        wait(1000);

        // Spread
        swipe(5, 0, 6, 0);
        swipe(7, 1, 6, 0);
        swipe(6, 0, 4, 0);
        swipe(7, 1, 5, 0);
        wait(1000);

        // Reinforce
        swipe(7, 0, 5, 0);
        swipe(8, 1, 6, 0);
        swipe(7, 2, 5, 0);
        swipe(8, 2, 6, 1);

        swipe(7, 0, 8, 0);
        swipe(8, 1, 8, 0);
        swipe(7, 2, 8, 3);
        swipe(8, 2, 8, 3);
        swipe(8, 0, 6, 0);
        swipe(8, 3, 6, 1);

        // Attack the back
        swipe(5, 0, 0, 0);
        wait(1000);

        // Reinforce
        swipe(9, 0, 7, 0);
        swipe(10, 1, 10, 2);
        swipe(10, 1, 9, 1);
        swipe(9, 4, 7, 2);
        swipe(10, 4, 10, 3);
        swipe(10, 4, 9, 3);

        swipe(10, 1, 10, 0);
        swipe(9, 0, 10, 0);
        swipe(10, 0, 8, 0);
        swipe(10, 4, 10, 5);
        swipe(9, 4, 10, 5);
        swipe(10, 5, 8, 3);

        // Attack the back
        swipe(5, 0, 0, 1);
        wait(1000);

        // Reinforce
        swipe(10, 2, 8, 1);
        swipe(9, 1, 7, 0);
        swipe(10, 3, 9, 2);
        swipe(9, 3, 7, 2);
        wait(1000);

        // Attack the back
        swipe(0, 0, 1, 0);
        swipe(0, 0, 0, 1);
        wait(1000);

        // Reinforce
        swipe(9, 2, 10, 3);
        swipe(10, 3, 8, 2);
        swipe(9, 2, 7, 1);

        System.out.println("END");
    }

    /**
     * Retrieves the screen coordinates for a node.
     *
     * @param column
     *            The column of the node
     * @param row
     *            The row of the node
     *
     * @return The screen coordinates
     */
    private static ScreenCoord getNode(final int column, final int row) {
        final int x = TOP_LEFT.x + column * INTERVAL.x;
        final int y = TOP_LEFT.y + row * INTERVAL.y
                + (column > COLUMN_MAX / 2 ? COLUMN_MAX - column : column) * INTERVAL.y / 2;
        return new ScreenCoord(x, y);
    }

    /**
     * Performs a swipe action from one node's coordinates to another, resulting in an attack (or reinforcement)
     * 
     * @param fromColumn
     *            The column of the sending node
     * @param fromRow
     *            The row of the sending node
     * @param toColumn
     *            The column of the receiving node
     * @param toRow
     *            The row of the receiving node
     */
    public static void swipe(final int fromColumn, final int fromRow, final int toColumn, final int toRow) {
        System.out.println("swipe(" + fromColumn + "," + fromRow + ", " + toColumn + "," + toRow + ");");
        swipe(getNode(fromColumn, fromRow), getNode(toColumn, toRow));
    }

}
