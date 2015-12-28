package com.tvkdevelopment.automaton.q42;

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
     * Gets the resulting coordinates after moving from the current ones a certain distance into the given direction.
     *
     * @param direction
     *            The direction to move in
     * @param distance
     *            The distance to move
     *
     * @return The resulting coordinates
     */
    public BoardCoord move(final Direction direction, final int distance) {
        switch (direction) {
            case NORTH:
                return new BoardCoord(x, y - distance);
            case EAST:
                return new BoardCoord(x + distance, y);
            case SOUTH:
                return new BoardCoord(x, y + distance);
            case WEST:
                return new BoardCoord(x - distance, y);
            default:
                throw new RuntimeException("Invalid direction " + direction);
        }
    }

    /**
     * Checks which direction the given coordinates are in relation to the current ones.
     *
     * @param coord
     *            The coordinates to check for
     *
     * @return The direction that the coordinates are in or null if there's no direction to describe it
     */
    public Direction getDirection(final BoardCoord coord) {
        if (coord.x == x) {
            if (coord.y < y) {
                return Direction.NORTH;
            }
            if (coord.y > y) {
                return Direction.SOUTH;
            }
        }
        if (coord.y == y) {
            if (coord.x < x) {
                return Direction.WEST;
            }
            if (coord.x > x) {
                return Direction.EAST;
            }
        }
        return null;
    }

    /**
     * Calculates the Manhattan distance between the current and given coordinates.
     * 
     * @param coord
     *            The coordinates to find the distance of
     *
     * @return The Manhattan distance between the coordinates
     */
    public int getDistance(final BoardCoord coord) {
        return Math.abs(x - coord.x) + Math.abs(y - coord.y);
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
