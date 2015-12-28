package com.tvkdevelopment.automaton.q42.ohhi;

import java.util.HashMap;
import java.util.Map;

import com.tvkdevelopment.automaton.ScreenCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Q42Game;
import com.tvkdevelopment.automaton.q42.Rule;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * An automaton to solve 0h h1 puzzles.
 */
public class Ohhi extends Q42Game {

    /** The amount of tiles in one line on the board */
    private static final int SIZE = 12;

    /** The rule set used to determine tile value and solve the puzzle */
    private static final Rule[] RULES = { new EvenColorTilesRule(), new MaxTwoConnectedRule(), new NoEvenLinesRule() };
    /** A mapping of start button coordinates for board size */
    private static final Map<Integer, ScreenCoord> START_BUTTON_MAP = new HashMap<Integer, ScreenCoord>();
    /** The order in which tiles appear when tapping on them */
    private static final Tile[] TILE_TAP_ORDER = { Tile.GREY, Tile.RED, Tile.BLUE };

    static {
        // Map the button coordinates for each board size
        START_BUTTON_MAP.put(4, new ScreenCoord(335, 885));
        START_BUTTON_MAP.put(6, new ScreenCoord(535, 885));
        START_BUTTON_MAP.put(8, new ScreenCoord(735, 885));
        START_BUTTON_MAP.put(10, new ScreenCoord(335, 1070));
        START_BUTTON_MAP.put(12, new ScreenCoord(535, 1070));
    }

    public static void main(final String[] args) {
        start(new Ohhi());
    }

    /**
     * Initialises a new 0h h1 puzzle.
     */
    public Ohhi() {
        super(SIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScreenCoord getStartButtonScreenCoord() {
        return START_BUTTON_MAP.get(SIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Rule[] getRules() {
        return RULES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Tile[] getTileTapOrder() {
        return TILE_TAP_ORDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BoardState initBoardState() {
        return new BoardState(SIZE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finaliseBoardState(final BoardState state) {}

}
