package com.tvkdevelopment.automaton.q42.ohno;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.tvkdevelopment.automaton.ScreenCoord;
import com.tvkdevelopment.automaton.q42.BoardCoord;
import com.tvkdevelopment.automaton.q42.BoardState;
import com.tvkdevelopment.automaton.q42.Q42Game;
import com.tvkdevelopment.automaton.q42.Rule;
import com.tvkdevelopment.automaton.q42.Tile;

/**
 * An automaton to solve 0h n0 puzzles.
 */
public class Ohno extends Q42Game {

    /** The amount of tiles in one line on the board */
    private static final int SIZE = 9;

    /** The rule set used to determine tile value and solve the puzzle */
    private static final Rule[] RULES = { new ExclusionRule(), new OverflowRule(), new CompletedRule(),
            new FillRule() };
    /** A mapping of start button coordinates for board size */
    private static final Map<Integer, ScreenCoord> START_BUTTON_MAP = new HashMap<Integer, ScreenCoord>();
    /** The order in which tiles appear when tapping on them */
    private static final Tile[] TILE_TAP_ORDER = { Tile.GREY, Tile.BLUE, Tile.RED };
    /** A set of cached images that can be used as a reference set for number images */
    private static final BufferedImage[] NUMBERS = new BufferedImage[SIZE];
    /** Whether or not to print info about the character recognition process */
    private static final boolean PRINT_CHARACTER_RECOGNITION = false;

    static {
        // Map the button coordinates for each board size
        START_BUTTON_MAP.put(4, new ScreenCoord(335, 885));
        START_BUTTON_MAP.put(5, new ScreenCoord(535, 885));
        START_BUTTON_MAP.put(6, new ScreenCoord(735, 885));
        START_BUTTON_MAP.put(7, new ScreenCoord(335, 1070));
        START_BUTTON_MAP.put(8, new ScreenCoord(535, 1070));
        START_BUTTON_MAP.put(9, new ScreenCoord(735, 1070));

        // Read the image numbers for character recognition
        try {
            for (int i = 0; i < SIZE; ++i) {
                NUMBERS[i] = ImageIO.read(new File("numbers/" + (i + 1) + ".png"));
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(final String[] args) {
        start(new Ohno());
    }

    /**
     * Initialises a new 0h h1 puzzle.
     */
    public Ohno() {
        super(SIZE);
    }

    /**
     * Reads a number from the dumped screen at the given coordinates.
     *
     * @param coord
     *            The coordinates on the board to read the number at
     *
     * @return The number that was found at the given coordinates
     */
    public int readNumber(final BoardCoord coord) {
        final ScreenCoord screenCoord = getTileCoord(coord);

        // Get a rough idea of where the number is
        final int approxSize = (int) (mTileSize * 0.45);
        final ScreenCoord approxCenter = new ScreenCoord(screenCoord.x - mOffset + mTileSize / 2,
                screenCoord.y - mOffset + mTileSize / 2);
        final int searchLeftStart = approxCenter.x - approxSize / 2;

        // Locate the top of the number
        int top = approxCenter.y;
        topSearch:
        while (true) {
            // Check if there's a white pixel on the current row
            for (int i = 0; i <= approxSize; ++i) {
                if (getColour(new ScreenCoord(searchLeftStart + i, top)).equals(Color.WHITE)) {
                    // If there's a white pixel, the number hasn't ended yet
                    --top;
                    continue topSearch;
                }
            }

            // No white pixel means the number ended
            ++top;
            break;
        }
        // Locate the bottom of the number
        int bottom = approxCenter.y;
        bottomSearch:
        while (true) {
            // Check if there's a white pixel on the current row
            for (int i = 0; i <= approxSize; ++i) {
                if (getColour(new ScreenCoord(searchLeftStart + i, bottom)).equals(Color.WHITE)) {
                    // If there's a white pixel, the number hasn't ended yet
                    ++bottom;
                    continue bottomSearch;
                }
            }

            // No white pixel means the number ended
            --bottom;
            break;
        }

        // Having the top and bottom of the number, we know its height
        final int height = bottom - top;
        if (height <= 0) {
            throw new RuntimeException("Number not found at " + coord);
        }

        // Locate the left and right of the number
        boolean numberDetected = false;
        int left = -1;
        int right = -1;
        widthSearch:
        for (int x = searchLeftStart;; ++x) {
            // Check if there's a white pixel on the current column
            for (int y = top; y <= bottom; ++y) {
                if (getColour(new ScreenCoord(x, y)).equals(Color.WHITE)) {
                    // If there's a white pixel and we hadn't seen it before, we found the left side of the number
                    if (!numberDetected) {
                        numberDetected = true;
                        left = x;
                    }
                    continue widthSearch;
                }
            }
            // If there wasn't any white pixel and we already find the left side of the number, we now found the right
            if (numberDetected) {
                right = x;
                break;
            }
        }

        // Using the number's top, height and width, we can find the top left of the centred number
        final ScreenCoord topLeft = new ScreenCoord((left + (right - left) / 2) - height / 2, top);

        // Create a 10x10 image of the number
        final Color[][] colours = new Color[10][10];
        for (int y = 0; y < 10; ++y) {
            for (int x = 0; x < 10; ++x) {
                colours[x][y] = getColour(new ScreenCoord(topLeft.x + x * height / 9, topLeft.y + y * height / 9));
                if (PRINT_CHARACTER_RECOGNITION) {
                    System.out.print(colours[x][y].getRed() > 100 ? "X" : ".");
                }
            }
            if (PRINT_CHARACTER_RECOGNITION) {
                System.out.println();
            }
        }

        // Compare the cropped image to the reference set
        final int[] errors = new int[SIZE];
        for (int y = 0; y < 10; ++y) {
            for (int x = 0; x < 10; ++x) {
                final Color colour = colours[x][y];
                for (int n = 0; n < SIZE; ++n) {
                    errors[n] += Math.pow(colour.getRed() - new Color(NUMBERS[n].getRGB(x, y)).getRed(), 2);
                }
            }
        }

        // Determine the best match
        int bestMatchNumber = -1;
        int bestMatchError = Integer.MAX_VALUE;
        for (int n = 0; n < SIZE; ++n) {
            if (errors[n] < bestMatchError) {
                bestMatchNumber = n;
                bestMatchError = errors[n];
            }
        }
        if (PRINT_CHARACTER_RECOGNITION) {
            System.out.println("Errors: " + Arrays.toString(errors));
            System.out.println("Best match: " + (bestMatchNumber + 1));
        }

        return bestMatchNumber + 1;
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
        return new BoardStateOhno(SIZE);
    }

    /**
     * Checks for numbers at each blue tile and sets them as requirements.
     */
    @Override
    protected void finaliseBoardState(final BoardState state) {
        final BoardStateOhno stateOhno = (BoardStateOhno) state;

        for (int y = 0; y < SIZE; ++y) {
            for (int x = 0; x < SIZE; ++x) {
                final BoardCoord coord = new BoardCoord(x, y);
                if (stateOhno.getTile(coord) == Tile.BLUE) {
                    stateOhno.setTileRequirement(coord, readNumber(coord));
                }
            }
        }
    }

}
