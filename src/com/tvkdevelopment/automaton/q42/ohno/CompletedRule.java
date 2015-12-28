package com.tvkdevelopment.automaton.q42.ohno;

import java.util.HashMap;
import java.util.Map;

import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * A rule that closes open endings of completed requirement tiles with a red tile.
 */
public class CompletedRule extends OhnoRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final Map<BoardCoord, Tile> actions = new HashMap<>();
        final BoardStateOhno stateOhno = (BoardStateOhno) state;

        for (final BoardCoord coord : stateOhno.getRequirementTiles()) {
            // If a tile's requirement is completed, fill the open endings with red
            if (getAmountOfSeenTiles(state, coord) == stateOhno.getTileRequirement(coord)) {
                for (final BoardCoord openEnding : getOpenEndings(state, coord)) {
                    actions.put(openEnding, Tile.RED);
                }
            }
        }

        return actions;
    }

}
