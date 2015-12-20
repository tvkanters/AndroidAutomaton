package com.tvkdevelopment.automaton.ohhi;

import com.tvkdevelopment.automaton.ScreenCoord;

/**
 * A model for a board coordinate. Though very similar to {@link ScreenCoord}, this separate class for board-specific
 * coordinates prevents confusion when using different types of coordinates.
 */
public class BoardCoord {

    /** The x coordinate on the board */
    public final int x;
    /** The y coordinate on the board */
    public final int y;

    /**
     * Wraps board coordinates in a model.
     *
     * @param x
     *            The x coordinate on the board
     * @param y
     *            The y coordinate on the board
     */
    public BoardCoord(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Wraps board coordinates in a model.
     *
     * @param x
     *            The x coordinate on the board
     * @param y
     *            The y coordinate on the board
     * @param flipped
     *            Whether or not the x and y coordinates should be flipped for convenience
     */
    public BoardCoord(final int x, final int y, final boolean flipped) {
        if (!flipped) {
            this.x = x;
            this.y = y;
        } else {
            this.x = y;
            this.y = x;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof BoardCoord)) {
            return false;
        }
        final BoardCoord other = (BoardCoord) obj;
        return (x == other.x && y == other.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return x + y * 31;
    }

}
