package com.tvkdevelopment.automaton.ohhi;

/**
 * A collection of possible tile values in tap order.
 */
public enum Tile {

    GREY,
    RED,
    BLUE;

    /**
     * Retrieves the appropriate tile value from an RGB(A) colour
     *
     * @param rgba
     *            The RGB(A) colour of a pixel in a tile
     *
     * @return The tile value
     */
    public static Tile fromRgb(final int[] rgba) {
        if (rgba[0] > 60000) {
            return RED;
        } else if (rgba[2] > 60000) {
            return BLUE;
        } else {
            return GREY;
        }
    }

}
