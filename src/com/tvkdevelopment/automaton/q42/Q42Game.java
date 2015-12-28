package com.tvkdevelopment.automaton.q42;

import java.util.Map;

import com.tvkdevelopment.automaton.Automaton;
import com.tvkdevelopment.automaton.ScreenCoord;
import com.tvkdevelopment.automaton.TimeMonitor;

/**
 * An automaton to solve Q42's 0h h1 or 0h n0 puzzles.
 */
public abstract class Q42Game extends Automaton {

    /** Whether or not to play a single game or to repeatedly solve them */
    private static final boolean REPEAT = true;

    /** The top left coordinate on the board */
    private static final ScreenCoord TOP_LEFT = new ScreenCoord(55, 505);
    /** The bottom right coordinate on the board */
    private static final ScreenCoord BOTTOM_RIGHT = new ScreenCoord(1015, 1465);

    /** The amount of tiles in one line on the board */
    protected final int mSize;
    /** The pixel size of a single tile */
    protected final int mTileSize;
    /** The top left offset of the tile centre */
    protected final int mOffset;

    /**
     * Initialises the game for the given board size.
     *
     * @param size
     *            The amount of tiles in one line on the board
     */
    protected Q42Game(final int size) {
        mSize = size;
        mTileSize = (BOTTOM_RIGHT.x - TOP_LEFT.x) / mSize;
        mOffset = (int) (mTileSize * 0.25);
    }

    /**
     * Starts playing a game and repeats it if needs be.
     *
     * @param game
     *            The game to play
     */
    public static void start(final Q42Game game) {
        if (!REPEAT) {
            game.playGame();
        } else {
            while (true) {
                game.playGame();
                wait(8000);
            }
        }
    }

    /**
     * Starts a game and solves it.
     */
    public void playGame() {
        // Start a game
        System.out.println("Starting game...");
        tap(getStartButtonScreenCoord());
        wait(1800);

        // Store a screenshot that can be used to read the board state
        System.out.println("Loading board state...");
        TimeMonitor.start("Importing screen");
        dumpScreen();
        TimeMonitor.stop("Importing screen");

        // Set the pixel colours of each tile to construct the board state
        final BoardState state = initBoardState();
        TimeMonitor.start("Reading board state");
        for (int y = 0; y < mSize; ++y) {
            for (int x = 0; x < mSize; ++x) {
                state.setTile(new BoardCoord(x, y), Tile.fromRgb(getColour(getTileCoord(new BoardCoord(x, y)))));
            }
        }
        TimeMonitor.stop("Reading board state");

        // Allow implementations to adjust the board state based on the read values
        TimeMonitor.start("Finalising board state");
        finaliseBoardState(state);
        TimeMonitor.stop("Finalising board state");
        System.out.println(state);

        // Keep applying solver rules and follow the actions they gave until there are no more actions
        Map<BoardCoord, Tile> actions;
        boolean updated = true;
        TimeMonitor.register("Inputting actions");
        while (updated) {
            updated = false;

            for (final Rule rule : getRules()) {
                do {
                    System.out.println(rule.getClass().getSimpleName());

                    // Determine which actions to perform according to the current rule
                    TimeMonitor.start("Rule " + rule.getClass().getSimpleName());
                    actions = rule.apply(state);
                    TimeMonitor.stop("Rule " + rule.getClass().getSimpleName());

                    // Process all given actions
                    TimeMonitor.start("Inputting actions");
                    for (final BoardCoord position : actions.keySet()) {
                        System.out.println(position + " -> " + actions.get(position));
                        updated = true;

                        // Input the action on the device and in the state
                        setTile(state, position, actions.get(position));
                    }
                    TimeMonitor.stop("Inputting actions");
                } while (!actions.isEmpty());
            }

            System.out.println();
            System.out.println(state);
        }

        System.out.println("Finished!");

        System.out.println();
        TimeMonitor.printElapsed();
        System.out.println();
        System.out.println();
    }

    /**
     * Retrieves the screen coordinate for a tile's board coordinate.
     *
     * @param coord
     *            The board coordinate of the tile
     *
     * @return The screen coordinate of the tile
     */
    public ScreenCoord getTileCoord(final BoardCoord coord) {
        return new ScreenCoord(TOP_LEFT.x + mOffset + coord.x * mTileSize, TOP_LEFT.y + mOffset + coord.y * mTileSize);
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
    public void setTile(final BoardState state, final BoardCoord coord, final Tile tile) {
        final Tile currentTile = state.getTile(coord);

        // Determine the number of taps from the current tile
        int numTaps = getTileTapPosition(tile) - getTileTapPosition(currentTile);
        if (numTaps < 0) {
            numTaps += getTileTapOrder().length;
        }

        // Input the taps on the device
        final ScreenCoord tileCoord = getTileCoord(coord);
        for (int i = 0; i < numTaps; ++i) {
            tap(tileCoord);
        }

        // Update the state
        state.setTile(coord, tile);
    }

    /**
     * Retrieves the tap position of a tile. I.e., the amount of taps needed from default to reach it.
     *
     * @param tile
     *            The tile to check for
     *
     * @return The tap position of the given tile
     */
    private int getTileTapPosition(final Tile tile) {
        final Tile[] tapOrder = getTileTapOrder();
        for (int i = 0; i < tapOrder.length; ++i) {
            if (tile == tapOrder[i]) {
                return i;
            }
        }
        throw new RuntimeException("Tile " + tile + " has no tap position");
    }

    /**
     * @return The screen coordinate for the button to start a new game
     */
    protected abstract ScreenCoord getStartButtonScreenCoord();

    /**
     * @return The set of rules used to solve the puzzle
     */
    protected abstract Rule[] getRules();

    /**
     * @return The order in which tiles appear when tapping on them
     */
    protected abstract Tile[] getTileTapOrder();

    /**
     * @return The initial board state that gets populated based on the screen of the device
     */
    protected abstract BoardState initBoardState();

    /**
     * A callback that allows game-specific modifications of the board's state after the initial population
     *
     * @param state
     *            The board state after the initial population
     */
    protected abstract void finaliseBoardState(BoardState state);

}
