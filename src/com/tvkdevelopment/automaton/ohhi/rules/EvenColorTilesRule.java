package com.tvkdevelopment.automaton.ohhi.rules;

import java.util.HashMap;
import java.util.Map;

import com.tvkdevelopment.automaton.ohhi.BoardCoord;
import com.tvkdevelopment.automaton.ohhi.BoardState;
import com.tvkdevelopment.automaton.ohhi.Tile;

/**
 * A rule that ensures each line of tiles has the same amount of red and blue values.
 */
public class EvenColorTilesRule implements Rule {

    /** The possible non-grey tile values */
    private static final Tile[] TILES = { Tile.RED, Tile.BLUE };

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final int boardSize = state.getSize();
        final int tilesRequired = boardSize / 2;
        final Map<BoardCoord, Tile> actions = new HashMap<>();

        // Run the algorithm twice (once horizontally and once vertically)
        boolean flipped = false;
        for (int i = 0; i < 2; ++i) {
            for (int y = 0; y < boardSize; ++y) {
                // Initialise count
                final Map<Tile, Integer> count = new HashMap<>();
                for (final Tile tile : Tile.values()) {
                    count.put(tile, 0);
                }

                // Count the tiles
                for (int x = 0; x < boardSize; ++x) {
                    final Tile tile = state.getTile(new BoardCoord(x, y, flipped));
                    count.put(tile, count.get(tile) + 1);
                }

                // Skip full lines
                if (count.get(Tile.GREY) == 0) {
                    continue;
                }

                // Check if tiles need to be filled with a colour
                for (final Tile tile : TILES) {
                    if (tilesRequired - count.get(tile) == count.get(Tile.GREY)) {
                        // Find the open spots to fill
                        for (int x = 0; x < boardSize; ++x) {
                            final BoardCoord coord = new BoardCoord(x, y, flipped);
                            if (state.getTile(coord) == Tile.GREY) {
                                actions.put(coord, tile);
                            }
                        }
                    }
                }
            }

            flipped = true;
        }

        return actions;
    }

}
