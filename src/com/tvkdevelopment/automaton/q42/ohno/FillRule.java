package com.tvkdevelopment.automaton.q42.ohno;

import java.util.HashMap;
import java.util.Map;

import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * A rule that fills the board with red tiles if all requirements are completed.
 */
public class FillRule extends OhnoRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final Map<BoardCoord, Tile> actions = new HashMap<>();
        final BoardStateOhno stateOhno = (BoardStateOhno) state;

        // Ensure all tile requirements are completed
        for (final BoardCoord coord : stateOhno.getRequirementTiles()) {
            final Integer requirement = stateOhno.getTileRequirement(coord);
            final int seen = getAmountOfSeenTiles(state, coord);
            if (seen < requirement) {
                return actions;
            }
        }

        // With all tile requirements completed, fill the remaining squares with red
        for (int y = 0; y < state.getSize(); ++y) {
            for (int x = 0; x < state.getSize(); ++x) {
                if (state.getTile(x, y) == Tile.GREY) {
                    actions.put(new BoardCoord(x, y), Tile.RED);
                }
            }
        }

        return actions;
    }

}
