package com.tvkdevelopment.automaton.ohhi.rules;

import java.util.Map;

import com.tvkdevelopment.automaton.ohhi.BoardCoord;
import com.tvkdevelopment.automaton.ohhi.BoardState;
import com.tvkdevelopment.automaton.ohhi.Tile;

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
