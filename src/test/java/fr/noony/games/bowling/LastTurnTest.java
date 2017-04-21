/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.noony.games.bowling;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ahamon
 */
public class LastTurnTest {

    public LastTurnTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isSecondBallStrike method, of class LastTurn.
     */
    @Test
    public void testIsSecondBallStrike() {
        LastEditableTurnImpl instance = new LastEditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isSecondBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(0);
        instance.setBall3(0);
        expResult = false;
        result = instance.isSecondBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(4);
        instance.setBall2(6);
        instance.setBall3(0);
        expResult = false;
        result = instance.isSecondBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(6);
        instance.setBall3(4);
        expResult = false;
        result = instance.isSecondBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(10);
        instance.setBall3(0);
        expResult = true;
        result = instance.isSecondBallStrike();
        assertEquals(expResult, result);
    }

    /**
     * Test of isSecondBallSpare method, of class LastTurn.
     */
    @Test
    public void testIsSecondBallSpare() {
        LastEditableTurnImpl instance = new LastEditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isSecondBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(0);
        instance.setBall3(10);
        expResult = false;
        result = instance.isSecondBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        instance.setBall2(0);
        instance.setBall3(0);
        expResult = false;
        result = instance.isSecondBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(10);
        instance.setBall3(0);
        expResult = true;
        result = instance.isSecondBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(5);
        instance.setBall2(5);
        instance.setBall3(0);
        expResult = true;
        result = instance.isSecondBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(6);
        instance.setBall3(4);
        expResult = false;
        result = instance.isSecondBallSpare();
        assertEquals(expResult, result);
    }

    /**
     * Test of isSecondBallSplit method, of class LastTurn.
     */
    @Test
    public void testIsSecondBallSplit() {
        LastEditableTurnImpl instance = new LastEditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isSecondBallSplit();
        assertEquals(expResult, result);
        //
        instance.setBall2isSplit(true);
        expResult = true;
        result = instance.isSecondBallSplit();
        assertEquals(expResult, result);

    }

    /**
     * Test of isThirdBallStrike method, of class LastTurn.
     */
    @Test
    public void testIsThirdBallStrike() {
        LastEditableTurnImpl instance = new LastEditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isThirdBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(6);
        instance.setBall3(4);
        expResult = false;
        result = instance.isThirdBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        instance.setBall2(10);
        instance.setBall3(4);
        expResult = false;
        result = instance.isThirdBallStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(10);
        instance.setBall3(10);
        expResult = true;
        result = instance.isThirdBallStrike();
        assertEquals(expResult, result);
    }

    /**
     * Test of isThirdBallSplit method, of class LastTurn.
     */
    @Test
    public void testIsThirdBallSplit() {
        LastEditableTurnImpl instance = new LastEditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isThirdBallSplit();
        assertEquals(expResult, result);
        //
        instance.setBall3isSplit(true);
        expResult = true;
        result = instance.isThirdBallSplit();
        assertEquals(expResult, result);
    }

    /**
     * Test of isThirdBallSpare method, of class LastTurn.
     */
    @Test
    public void testIsThirdBallSpare() {
        LastEditableTurnImpl instance = new LastEditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isThirdBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(0);
        instance.setBall3(10);
        expResult = false;
        result = instance.isThirdBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        instance.setBall2(0);
        instance.setBall3(10);
        expResult = true;
        result = instance.isThirdBallSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        instance.setBall2(5);
        instance.setBall3(5);
        expResult = true;
        result = instance.isThirdBallSpare();
        assertEquals(expResult, result);
    }

}
