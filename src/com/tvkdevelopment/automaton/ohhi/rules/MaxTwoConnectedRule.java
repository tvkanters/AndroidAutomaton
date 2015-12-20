package com.tvkdevelopment.automaton.ohhi.rules;

import java.util.HashMap;
import java.util.Map;

import com.tvkdevelopment.automaton.ohhi.BoardCoord;
import com.tvkdevelopment.automaton.ohhi.BoardState;
import com.tvkdevelopment.automaton.ohhi.Tile;

/**
 * A rule that ensures a maximum of two tiles of the same colour line up.
 */
public class MaxTwoConnectedRule implements Rule {

    /** The possible non-grey tile values */
    private static final Tile[] TILES = { Tile.RED, Tile.BLUE };

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final int boardSize = state.getSize();
        final Map<BoardCoord, Tile> actions = new HashMap<>();

        for (int y = 0; y < boardSize; ++y) {
            for (int x = 0; x < boardSize; ++x) {
                // Only fill in grey tiles
                if (state.getTile(new BoardCoord(x, y)) != Tile.GREY) {
                    continue;
                }

                for (final Tile tile : TILES) {
                    // Check if two adjacent tiles next to this one are the same colour or if it's in between two equals
                    if ((state.getTile(x + 1, y) == tile && state.getTile(x + 2, y) == tile)
                            || (state.getTile(x - 1, y) == tile && state.getTile(x - 2, y) == tile)
                            || (state.getTile(x, y + 1) == tile && state.getTile(x, y + 2) == tile)
                            || (state.getTile(x, y - 1) == tile && state.getTile(x, y - 2) == tile)
                            || (state.getTile(x + 1, y) == tile && state.getTile(x - 1, y) == tile)
                            || (state.getTile(x, y + 1) == tile && state.getTile(x, y - 1) == tile)) {
                        actions.put(new BoardCoord(x, y), (tile == Tile.RED ? Tile.BLUE : Tile.RED));
                    }
                }
            }
        }

        return actions;
    }

}
