package com.tvkdevelopment.automaton.q42.ohno;

import java.util.HashMap;
import java.util.Map;

import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Direction;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * A rule that fills tiles with red if filling them with blue would cause a tile to see more than its requirement.
 */
public class OverflowRule extends OhnoRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<BoardCoord, Tile> apply(final BoardState state) {
        final Map<BoardCoord, Tile> actions = new HashMap<>();
        final BoardStateOhno stateOhno = (BoardStateOhno) state;

        for (final BoardCoord coord : stateOhno.getRequirementTiles()) {
            final Integer requirement = stateOhno.getTileRequirement(coord);
            final int seen = getAmountOfSeenTiles(state, coord);
            // If a tile's requirement isn't completed, check if appending a blue tile in a direction would overflow it
            if (seen < requirement) {
                // Check for all directions that have an open ending
                for (final BoardCoord openEnding : getOpenEndings(state, coord)) {
                    final Direction direction = coord.getDirection(openEnding);
                    int potential = 1;

                    // Check potential extra tiles seen when adding a single blue tile
                    for (int i = coord.getDistance(openEnding) + 1;; ++i) {
                        final BoardCoord potentialCoord = coord.move(direction, i);
                        if (!state.isWithinBounds(potentialCoord) || state.getTile(potentialCoord) != Tile.BLUE) {
                            break;
                        }
                        ++potential;
                    }

                    // If needed, fill with red to prevent the tile requirement from overflowing by placing a red tile
                    if (seen + potential > requirement) {
                        actions.put(openEnding, Tile.RED);
                    }
                }
            }
        }

        return actions;
    }

}
