package edu.missouristate.mote;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Run random interactions with the data entry grid.
 */
public class MoteGuiTest extends Thread {

    // *************************************************************************
    // CONSTANTS
    // *************************************************************************
    // Y-coordinates of the menu items in the Measures menu.
    private static final int[] CELL_YS = {141, 157, 173, 189, 205, 221, 237,
        253, 269, 285};
    // Delay between robot events.
    private static final int LONG_DELAY = 100;
    // Y-coordinates of the menu items in the Measures menu.
    private static final int[] MEASURE_YS = {81, 100, 119, 138, 157, 176, 195, 214};
    // Delay between robot events.
    private static final int SHORT_DELAY = 50;
    private static final int MEASURE_TEST_COUNT = 16;
    private static final int CELL_TEST_COUNT = 20;
    // *************************************************************************
    // FIELDS
    // *************************************************************************
    // Robot for controlling the mouse and keyboard.
    private Robot bot;
    // Random number generator.
    private final Random rand;

    // *************************************************************************
    // CONSTRUCTORS
    // *************************************************************************
    /**
     * Initialize a new instance of a MoteGuiTest.
     */
    public MoteGuiTest() {
        super();
        rand = new Random();
        try {
            bot = new Robot();
        } catch (AWTException e) {
            bot = null;
        }
    }

    // *************************************************************************
    // PRIVATE METHODS
    // *************************************************************************
    private void cellSelectAll() {
        //bot.keyPress(KeyEvent.CTRL_MASK + KeyEvent.VK_A);
        //bot.keyRelease(KeyEvent.CTRL_MASK + KeyEvent.VK_A);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        bot.delay(LONG_DELAY);
    }

    private void clickMouse(final int count) {
        for (int index = 0; index < count; index++) {
            bot.mousePress(InputEvent.BUTTON1_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_MASK);
            bot.delay(SHORT_DELAY);
        }
        bot.delay(LONG_DELAY);
    }

    private void selectMeasuresMenu() {
        bot.mouseMove(70, 57);
        bot.delay(LONG_DELAY);
        clickMouse(1);
    }

    private void selectNothing() {
        bot.delay(LONG_DELAY);
        bot.mouseMove(300, 57);
        clickMouse(1);
    }

    private void selectRandomMeasure() {
        final int index = rand.nextInt(MEASURE_YS.length);
        final int yValue = MEASURE_YS[index];
        selectMeasuresMenu();
        bot.mouseMove(70, yValue);
        bot.delay(LONG_DELAY);
        clickMouse(1);
    }

    private void typeNumber(final int number) {
        switch (number) {
            case 0:
                bot.keyPress(KeyEvent.VK_0);
                bot.keyRelease(KeyEvent.VK_0);
                break;
            case 1:
                bot.keyPress(KeyEvent.VK_1);
                bot.keyRelease(KeyEvent.VK_1);
                break;
            case 2:
                bot.keyPress(KeyEvent.VK_2);
                bot.keyRelease(KeyEvent.VK_2);
                break;
            case 3:
                bot.keyPress(KeyEvent.VK_3);
                bot.keyRelease(KeyEvent.VK_3);
                break;
            case 4:
                bot.keyPress(KeyEvent.VK_4);
                bot.keyRelease(KeyEvent.VK_4);
                break;
            case 5:
                bot.keyPress(KeyEvent.VK_5);
                bot.keyRelease(KeyEvent.VK_5);
                break;
            case 6:
                bot.keyPress(KeyEvent.VK_6);
                bot.keyRelease(KeyEvent.VK_6);
                break;
            case 7:
                bot.keyPress(KeyEvent.VK_7);
                bot.keyRelease(KeyEvent.VK_7);
                break;
            case 8:
                bot.keyPress(KeyEvent.VK_8);
                bot.keyRelease(KeyEvent.VK_8);
                break;
            case 9:
                bot.keyPress(KeyEvent.VK_9);
                bot.keyRelease(KeyEvent.VK_9);
                break;
        }
    }
    
    private void typeRandomNavKey() {
        switch (rand.nextInt(7)) {
            case 0:
                bot.keyPress(KeyEvent.VK_UP);
                bot.keyRelease(KeyEvent.VK_UP);
                break;
            case 1:
                bot.keyPress(KeyEvent.VK_DOWN);
                bot.keyRelease(KeyEvent.VK_DOWN);
                break;
            case 2:
                bot.keyPress(KeyEvent.VK_TAB);
                bot.keyRelease(KeyEvent.VK_TAB);
                break;
            case 3:
                bot.keyPress(KeyEvent.VK_ENTER);
                bot.keyRelease(KeyEvent.VK_ENTER);
                break;
            case 4:
                bot.keyPress(KeyEvent.VK_SHIFT);
                bot.keyPress(KeyEvent.VK_TAB);
                bot.keyRelease(KeyEvent.VK_TAB);
                bot.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 5:
                bot.keyPress(KeyEvent.VK_SHIFT);
                bot.keyPress(KeyEvent.VK_ENTER);
                bot.keyRelease(KeyEvent.VK_ENTER);
                bot.keyRelease(KeyEvent.VK_SHIFT);
                break;
            default:
                break;
        }
    }
    
    private void typeRandomNumber(final int decimals) {
        // Left of decimal
        typeNumber(rand.nextInt(10));
        // Decimal
        bot.keyPress(KeyEvent.VK_PERIOD);
        bot.keyRelease(KeyEvent.VK_PERIOD);
        // Right of decimal
        for (int index = 0; index < Math.max(1, decimals); index++) {
            typeNumber(rand.nextInt(10));
        }
    }

    private void updateRandomCell() {
        // Select the cell
        final int index = rand.nextInt(CELL_YS.length);
        final int yValue = CELL_YS[index];
        bot.mouseMove(239, yValue);
        bot.delay(SHORT_DELAY);
        clickMouse(1);
        // Maybe use a navigation key
        //for (int i = 0; i < rand.nextInt(2); i++) {
        //    typeRandomNavKey();
        //}
        // Update it
        cellSelectAll();
        typeRandomNumber(5);
        //for (int i = 0; i < rand.nextInt(2); i++) {
        //    typeRandomNavKey();
        //}
        bot.delay(SHORT_DELAY);
    }

    // *************************************************************************
    // PUBLIC METHODS
    // *************************************************************************
    @Override
    public void run() {
        System.out.println("Running GUI test ...");
        selectNothing();
        for (int i = 0; i < MEASURE_TEST_COUNT; i++) {
            selectRandomMeasure();
            for (int j = 0; j < CELL_TEST_COUNT; j++) {
                System.out.println("Measure " + (i+1) + "/" + MEASURE_TEST_COUNT
                        + "   Cell " + (j+1) + "/" + CELL_TEST_COUNT);
                updateRandomCell();
            }
        }
    }
}