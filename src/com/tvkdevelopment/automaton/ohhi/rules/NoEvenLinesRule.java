package com.tvkdevelopment.automaton.ohhi.rules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tvkdevelopment.automaton.ohhi.BoardCoord;
import com.tvkdevelopment.automaton.ohhi.BoardState;
import com.tvkdevelopment.automaton.ohhi.Tile;

/**
 * A rule that ensures no two lines contain equal tile values.
 */
public class NoEvenLinesRule implements Rule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final int boardSize = state.getSize();
        final Map<BoardCoord, Tile> actions = new HashMap<>();

        // Run the algorithm twice (once horizontally and once vertically)
        boolean flipped = false;
        for (int i = 0; i < 2; ++i) {
            final List<Integer> linesFilled = new LinkedList<>();
            final List<Integer> linesPending = new LinkedList<>();

            // Collect all filled or pending lines (where pending lines can be completed by matching filled lines)
            for (int y = 0; y < boardSize; ++y) {
                int countGrey = 0;
                for (int x = 0; x < boardSize; ++x) {
                    if (state.getTile(new BoardCoord(x, y, flipped)) == Tile.GREY) {
                        ++countGrey;
                    }
                }

                if (countGrey == 0) {
                    linesFilled.add(y);
                } else if (countGrey == 2) {
                    linesPending.add(y);
                }
            }

            // Check if there's a filled line matching a pending line
            for (final Integer linePending : linesPending) {

                // Collect all grey tiles in the pending line
                final List<Integer> tilesGrey = new LinkedList<>();
                for (int x = 0; x < boardSize; ++x) {
                    if (state.getTile(new BoardCoord(x, linePending, flipped)) == Tile.GREY) {
                        tilesGrey.add(x);
                    }
                }

                lineFilledLoop:
                for (final Integer lineFilled : linesFilled) {
                    // Compare all individual tiles of the lines and skip to the next filled line if one doesn't match
                    for (int x = 0; x < boardSize; ++x) {
                        final Tile pendingTile = state.getTile(new BoardCoord(x, linePending, flipped));
                        if (pendingTile != Tile.GREY
                                && pendingTile != state.getTile(new BoardCoord(x, lineFilled, flipped))) {
                            continue lineFilledLoop;
                        }
                    }

                    // When a line matches, determine the new grey tile values
                    for (final Integer tileGrey : tilesGrey) {
                        actions.put(new BoardCoord(tileGrey, linePending, flipped),
                                (state.getTile(new BoardCoord(tileGrey, lineFilled, flipped)) == Tile.RED ? Tile.BLUE
                                        : Tile.RED));
                    }
                }
            }

            flipped = true;
        }

        return actions;
    }
}
