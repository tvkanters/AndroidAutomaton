package com.tvkdevelopment.automaton.q42;

import java.util.Map;

/**
 * An interface rule that tiles must adhere to. Used to infer tile values given a board state.
 */
public interface Rule {

    /**
     * Applies a rule to a board state to determine the values of unfilled tiles.
     *
     * @param state
     *            The current board state
     *
     * @return A mapping from board coordinates to tile values that they should have
     */
    Map<BoardCoord, Tile> apply(BoardState state);

}
