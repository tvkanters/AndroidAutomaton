package com.tvkdevelopment.automaton.q42.ohno;

import java.util.HashSet;
import java.util.Set;

import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Direction;
import com.tvkdevelopment.automaton.q42.Rule;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * A base rule for 0h n0 games with some general useful utilities.
 */
public abstract class OhnoRule implements Rule {

    /**
     * Retrieves the amount of tiles that are seen from the given coordinates. I.e., uninterrupted blue tiles
     * horizontally or vertically of the given coordinates.
     *
     * @param state
     *            The current board state
     * @param coord
     *            The coordinates from where to check
     *
     * @return The amount of seen tiles
     */
    protected int getAmountOfSeenTiles(final BoardState state, final BoardCoord coord) {
        int seen = 0;

        for (final Direction direction : Direction.values()) {
            for (int i = 1;; ++i) {
                final BoardCoord searchCoord = coord.move(direction, i);
                if (state.isWithinBounds(searchCoord) && state.getTile(searchCoord) == Tile.BLUE) {
                    ++seen;
                } else {
                    break;
                }
            }
        }

        return seen;
    }

    /**
     * Retrieves the open endings from the given coordinates. I.e., grey tiles directly after an uninterrupted line of
     * blue tiles from the given coordinates.
     *
     * @param state
     *            The current board state
     * @param coord
     *            The coordinates from where to check
     *
     * @return The coordinates of open endings
     */
    protected Set<BoardCoord> getOpenEndings(final BoardState state, final BoardCoord coord) {
        final Set<BoardCoord> openEndings = new HashSet<>();

        for (final Direction direction : Direction.values()) {
            search:
            for (int i = 1;; ++i) {
                final BoardCoord searchCoord = coord.move(direction, i);
                if (!state.isWithinBounds(searchCoord)) {
                    break;
                }
                switch (state.getTile(searchCoord)) {
                    case GREY:
                        openEndings.add(searchCoord);
                        // Fall-through
                    case RED:
                        break search;
                    default:
                        break;
                }
            }
        }

        return openEndings;
    }

    /**
     * Retrieves the coordinates of tiles that can be potentially seen by the tile at the given coordinates. Includes
     * tiles that are actually seen.
     *
     * @param state
     *            The current board state
     * @param coord
     *            The coordinates from where to check
     *
     * @return The potentially seen tile coordinates
     */
    protected Set<BoardCoord> getPotentialSeen(final BoardState state, final BoardCoord coord) {
        final Set<BoardCoord> potentials = new HashSet<>();

        for (final Direction direction : Direction.values()) {
            search:
            for (int i = 1;; ++i) {
                final BoardCoord searchCoord = coord.move(direction, i);
                if (!state.isWithinBounds(searchCoord)) {
                    break;
                }
                switch (state.getTile(searchCoord)) {
                    case GREY:
                    case BLUE:
                        potentials.add(searchCoord);
                        break;
                    case RED:
                        break search;
                }
            }
        }

        return potentials;
    }

    /**
     * Retrieves the coordinates of tiles that can be potentially seen by the tile at the given coordinates. Includes
     * tiles that are actually seen, but excludes any tile in the given direction.
     *
     * @param state
     *            The current board state
     * @param coord
     *            The coordinates from where to check
     * @param exclusion
     *            The direction to exclude from the check.
     *
     * @return The potentially seen tile coordinates
     */
    protected Set<BoardCoord> getPotentialSeenExcluded(final BoardState state, final BoardCoord coord,
            final Direction exclusion) {
        final Set<BoardCoord> potentials = new HashSet<>();

        for (final Direction direction : Direction.values()) {
            if (direction == exclusion) {
                continue;
            }

            search:
            for (int i = 1;; ++i) {
                final BoardCoord searchCoord = coord.move(direction, i);
                if (!state.isWithinBounds(searchCoord)) {
                    break;
                }
                switch (state.getTile(searchCoord)) {
                    case GREY:
                    case BLUE:
                        potentials.add(searchCoord);
                        break;
                    case RED:
                        break search;
                }
            }
        }

        return potentials;
    }

}
