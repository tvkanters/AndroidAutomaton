package com.tvkdevelopment.automaton.q42;

import java.awt.Color;

/**
 * A collection of possible tile values.
 */
public enum Tile {

    GREY,
    RED,
    BLUE;

    /**
     * Retrieves the appropriate tile value from a pixel colour
     *
     * @param colour
     *            The colour of a pixel in a tile
     *
     * @return The tile value
     */
    public static Tile fromRgb(final Color colour) {
        if (colour.getRed() > 150) {
            return RED;
        } else if (colour.getBlue() > 150) {
            return BLUE;
        } else {
            return GREY;
        }
    }

}
