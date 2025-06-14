/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dhonl
 */
public class ModelTest {

    private Model model;

    @Before
    public void setUp() {
        DatabaseManager dbManager = new DatabaseManager();
        WordProvider wordProvider = new WordProvider(dbManager);
        WordValidator wordValidator = new WordValidator(dbManager);
        StatSaver statSaver = new StatSaver(dbManager);
        StreakCounter streakCounter = new StreakCounter(dbManager);

        model = new Model(wordProvider, wordValidator, statSaver, streakCounter);
    }

    @Test
    public void testAddLetter() {
        model.addLetter('H');
        model.addLetter('I');
        assertEquals("HI", model.getCurrentWord());
    }

    @Test
    public void testRemoveLastLetter() {
        model.addLetter('A');
        model.addLetter('B');
        model.removeLastLetter();
        assertEquals("A", model.getCurrentWord());
    }

    @Test
    public void testMaxWordLength() { // testing 5-letter word limit
        for (char c : "HELLOWORLD".toCharArray()) {
            model.addLetter(c);
        }
        assertEquals(5, model.getCurrentWord().length());
    }

    @Test
    public void testFeedback() {
        model.getFeedback("there", "three", 5);
        assertEquals("VV??V", model.getFeedback());
    }

    @Test
    public void testFeedback2() { //ensure duplicate yellows work properly (not showing XX??X)
        model.getFeedback("crane", "steer", 5);
        assertEquals("XX?X?", model.getFeedback()); // assuming 5-letter word limit
    }

    @Test
    public void testFeedback3() { //ensure duplicate yellows work properly (not showing VXV?X)
        model.getFeedback("steal", "speed", 5);
        assertEquals("VXVXX", model.getFeedback()); // assuming 5-letter word limit
    }

    @Test
    public void testWordDB() {
        assertTrue(model.isValidGuessWord("apple"));
    }

    @Test
    public void testWordDB2() { //testing a word in word list, but not in answer list
        assertTrue(model.isValidGuessWord("strum"));
    }

    @Test
    public void testGuessListContains() {
        model.addGuessList("apple");
        model.addGuessList("strum");
        model.addGuessList("sigma");
        assertTrue(model.GuessListContains("strum"));
    }
    
    @Test
    public void testGetGuessList() {
        model.addGuessList("apple");
        model.addGuessList("strum");
        model.addGuessList("sigma");
        assertEquals("strum", model.getGuessList(1));
    }
}
