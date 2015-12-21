package com.tvkdevelopment.automaton.ohhi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tvkdevelopment.automaton.Automaton;
import com.tvkdevelopment.automaton.ScreenCoord;
import com.tvkdevelopment.automaton.ohhi.rules.EvenColorTilesRule;
import com.tvkdevelopment.automaton.ohhi.rules.MaxTwoConnectedRule;
import com.tvkdevelopment.automaton.ohhi.rules.NoEvenLinesRule;
import com.tvkdevelopment.automaton.ohhi.rules.Rule;

/**
 * An automaton to solve 0h h1 puzzles.
 */
public class Ohhi extends Automaton {

    /** The amount of tiles in one line on the board */
    private static final int SIZE = 12;
    /** Whether or not to play a single game or to repeatedly solve them */
    private static final boolean REPEAT = true;

    /** The top left coordinate on the board */
    private static final ScreenCoord TOP_LEFT = new ScreenCoord(55, 505);
    /** The bottom right coordinate on the board */
    private static final ScreenCoord BOTTOM_RIGHT = new ScreenCoord(1015, 1465);

    /** The pixel size of a single tile */
    private static final int TILE_SIZE = (BOTTOM_RIGHT.x - TOP_LEFT.x) / SIZE;
    /** The top left offset of the tile centre */
    private static final int OFFSET = TILE_SIZE / 2;

    /** The rule set used to determine tile value and solve the puzzle */
    private static final Rule[] RULES = { new EvenColorTilesRule(), new MaxTwoConnectedRule(), new NoEvenLinesRule() };
    /** A mapping of start button coordinates for board size */
    private static final Map<Integer, ScreenCoord> START_BUTTON_MAP = new HashMap<Integer, ScreenCoord>();

    static {
        // Map the button coordinates for each board size
        START_BUTTON_MAP.put(4, new ScreenCoord(335, 885));
        START_BUTTON_MAP.put(6, new ScreenCoord(535, 885));
        START_BUTTON_MAP.put(8, new ScreenCoord(735, 885));
        START_BUTTON_MAP.put(10, new ScreenCoord(335, 1070));
        START_BUTTON_MAP.put(12, new ScreenCoord(535, 1070));
    }

    public static void main(final String[] args) {
        if (!REPEAT) {
            playGame();
        } else {
            while (true) {
                playGame();
                wait(5500);
            }
        }
    }

    /**
     * Starts a game and solves it.
     */
    public static void playGame() {
        // Start a game
        System.out.println("Starting game...");
        tap(START_BUTTON_MAP.get(SIZE));
        wait(2000);

        // Store a screenshot that can be used to read the board state
        System.out.println("Loading board state...");
        dumpScreen();

        // Find the coordinates that we need the pixel colours of to construct the board state
        final BoardState state = new BoardState(SIZE);
        final List<ScreenCoord> coords = new LinkedList<ScreenCoord>();
        for (int y = 0; y < SIZE; ++y) {
            for (int x = 0; x < SIZE; ++x) {
                coords.add(getTileCoord(new BoardCoord(x, y)));
            }
        }

        // Load the pixel colours for all tile coordinates and put them in the state
        final int[][] pixelColours = getColours(coords);
        for (int y = 0; y < SIZE; ++y) {
            for (int x = 0; x < SIZE; ++x) {
                state.setTile(new BoardCoord(x, y), Tile.fromRgb(pixelColours[y * SIZE + x]));
            }
        }
        System.out.println(state);

        // Keep applying solver rules and follow the actions they gave until there are no more actions
        Map<BoardCoord, Tile> actions;
        boolean updated = true;
        while (updated) {
            updated = false;

            for (final Rule rule : RULES) {
                do {
                    System.out.println(rule.getClass().getSimpleName());

                    // Determine which actions to perform according to the current rule
                    actions = rule.apply(state);

                    // Process all given actions
                    for (final BoardCoord position : actions.keySet()) {
                        System.out.println(position + " -> " + actions.get(position));
                        updated = true;

                        // Input the action on the device and in the state
                        setTile(state, position, actions.get(position));
                    }
                } while (!actions.isEmpty());
            }

            System.out.println(state);
        }

        System.out.println("Finished!");
    }

    /**
     * Retrieves the screen coordinate for a tile's board coordinate.
     *
     * @param coord
     *            The board coordinate of the tile
     *
     * @return The screen coordinate of the tile
     */
    public static ScreenCoord getTileCoord(final BoardCoord coord) {
        return new ScreenCoord(TOP_LEFT.x + OFFSET + coord.x * TILE_SIZE, TOP_LEFT.y + OFFSET + coord.y * TILE_SIZE);
    }

    /**
     * Updates a tile both in the state and on the device
     *
     * @param x
     *            The x position of the tile
     * @param y
     *            The y position of the tile
     * @param tile
     *            The new tile
     */
    public static void setTile(final BoardState state, final BoardCoord coord, final Tile tile) {
        final Tile currentTile = state.getTile(coord);

        // Determine the number of taps from the current tile
        int numTaps = tile.ordinal() - currentTile.ordinal();
        if (numTaps < 0) {
            numTaps += 3;
        }

        // Input the taps on the device
        final ScreenCoord tileCoord = getTileCoord(coord);
        for (int i = 0; i < numTaps; ++i) {
            tap(tileCoord);
        }

        // Update the state
        state.setTile(coord, tile);
    }

}
