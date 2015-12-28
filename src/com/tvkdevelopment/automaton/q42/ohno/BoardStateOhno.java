package com.tvkdevelopment.automaton.q42.ohno;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;

/**
 * A model class for the current state of the board specifically for 0h n0 games, which in addition to tiles has numeric
 * values.
 */
public class BoardStateOhno extends BoardState {

    /** The tile requirements for the initial tiles with a number on them */
    private final Map<BoardCoord, Integer> mTileRequirement = new HashMap<>();

    /**
     * Creates a new empty board of a given size.
     *
     * @param size
     *            The size (width or height) of the square board
     */
    public BoardStateOhno(final int size) {
        super(size);
    }

    /**
     * Sets the tile requirements for an initial tile with a number on it.
     *
     * @param coord
     *            The coordinates of the tile with a requirement
     * @param requirement
     *            The required (i.e., amount of other blue tiles it must see)
     */
    public void setTileRequirement(final BoardCoord coord, final Integer requirement) {
        mTileRequirement.put(coord, requirement);
    }

    /**
     * Retrieves the tile requirements for an initial tile with a number on it
     *
     * @param coord
     *            The coordinates of the tile with a requirement
     *
     * @return The required (i.e., amount of other blue tiles it must see)
     */
    public Integer getTileRequirement(final BoardCoord coord) {
        return mTileRequirement.get(coord);
    }

    /**
     * @return The list of coordinates for tiles with a requirement
     */
    public Set<BoardCoord> getRequirementTiles() {
        return mTileRequirement.keySet();
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
                    case BLUE:
                        final BoardCoord coord = new BoardCoord(x, y);
                        if (mTileRequirement.containsKey(coord)) {
                            builder.append(mTileRequirement.get(coord) + " ");
                            break;
                        }
                        // Fall-through
                    case RED:
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
