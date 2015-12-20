package com.tvkdevelopment.automaton;

/**
 * A model for a screen coordinate.
 */
public class ScreenCoord {

    /** The x coordinate on the screen */
    public final int x;
    /** The y coordinate on the screen */
    public final int y;

    /**
     * Wraps screen coordinates in a model.
     *
     * @param x
     *            The x coordinate on the screen
     * @param y
     *            The y coordinate on the screen
     */
    public ScreenCoord(final int x, final int y) {
        this.x = x;
        this.y = y;
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
        if (!(obj instanceof ScreenCoord)) {
            return false;
        }
        final ScreenCoord other = (ScreenCoord) obj;
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
