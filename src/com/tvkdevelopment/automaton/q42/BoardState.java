package com.tvkdevelopment.automaton.q42;

/**
 * A model class for the current state of the board.
 */
public class BoardState {

    /** The board tile values */
    protected final Tile[][] mBoard;
    /** The size (width or height) of the square board */
    protected final int mSize;

    /**
     * Creates a new empty board of a given size.
     *
     * @param size
     *            The size (width or height) of the square board
     */
    public BoardState(final int size) {
        mBoard = new Tile[size][size];
        mSize = size;
    }

    /**
     * Updates a tile value of the board state.
     *
     * @param coord
     *            The coordinate of the tile to update
     * @param tile
     *            The new tile value
     */
    public void setTile(final BoardCoord coord, final Tile tile) {
        mBoard[coord.x][coord.y] = tile;
    }

    /**
     * Retrieves a tile value for the given coordinates.
     *
     * @param coord
     *            The board coordinate for the tile
     *
     * @return The tile value
     */
    public Tile getTile(final BoardCoord coord) {
        return mBoard[coord.x][coord.y];
    }

    /**
     * Retrieves a tile value for the given coordinates. This method is lenient in that it accepts coordinates that are
     * out of bounds, and simply returns {@link Tile#GREY} instead of throwing an exception.
     *
     * @param x
     *            The x coordinate for the tile
     * @param y
     *            The x coordinate for the tile
     *
     * @return The tile value or {@link Tile#GREY} if the coordinate is out of bounds
     */
    public Tile getTile(final int x, final int y) {
        if (!isWithinBounds(x, y)) {
            return Tile.GREY;
        }
        return mBoard[x][y];
    }

    /**
     * Checks if the given coordinates are within the bounds of the board.
     *
     * @param coord
     *            The coordinates to check
     *
     * @return True iff the coordinates are within the bounds of the board
     */
    public boolean isWithinBounds(final BoardCoord coord) {
        return isWithinBounds(coord.x, coord.y);
    }

    /**
     * Checks if the given coordinates are within the bounds of the board.
     *
     * @param x
     *            The x coordinate to check
     * @param y
     *            The y coordinate to check
     *
     * @return True iff the coordinates are within the bounds of the board
     */
    public boolean isWithinBounds(final int x, final int y) {
        return (x >= 0 && y >= 0 && x < mSize && y < mSize);
    }

    /**
     * @return The size (width or height) of the square board
     */
    public int getSize() {
        return mSize;
    }

    /**
     * @return A 2D representation of the tile values
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (int y = 0; y < mSize; ++y) {
            for (int x = 0; x < mSize; ++x) {
                switch (mBoard[x][y]) {
                    case RED:
                    case BLUE:
                        builder.append(mBoard[x][y].name().charAt(0) + " ");
                        break;
                    case GREY:
                        builder.append(". ");
                        break;
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }

}
