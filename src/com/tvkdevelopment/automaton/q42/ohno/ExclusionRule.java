package com.tvkdevelopment.automaton.q42.ohno;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Direction;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * A rule that checks how many tiles in a certain direction need to be made blue when filling in everything that's
 * possible in the other directions.
 */
public class ExclusionRule extends OhnoRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final Map<BoardCoord, Tile> actions = new HashMap<>();
        final BoardStateOhno stateOhno = (BoardStateOhno) state;

        for (final BoardCoord coord : stateOhno.getRequirementTiles()) {
            final Integer requirement = stateOhno.getTileRequirement(coord);
            // If the amount of seen tiles is less than what's required, check if a direction should have tiles appended
            if (getAmountOfSeenTiles(state, coord) < requirement) {
                for (final Direction exclusionDirection : Direction.values()) {
                    final Set<BoardCoord> potentials = getPotentialSeenExcluded(state, coord, exclusionDirection);
                    final int lack = requirement - potentials.size();
                    // If the the other directions don't have enough potential tiles to complete the requirement, fill
                    // the excluded tiles for the amount left over
                    for (int i = 1; i <= lack; ++i) {
                        final BoardCoord targetCoord = coord.move(exclusionDirection, i);
                        if (state.getTile(targetCoord) != Tile.BLUE) {
                            actions.put(targetCoord, Tile.BLUE);
                        }
                    }
                }
            }
        }

        return actions;
    }

}
