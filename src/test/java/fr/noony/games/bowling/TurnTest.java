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
public class TurnTest {

    public TurnTest() {
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
     * Test of getNbThrows method, of class Turn.
     */
    @Test
    public void testGetNbThrows() {
        EditableTurnImpl instance = new EditableTurnImpl();
        int expResult = 0;
        int result = instance.getNbThrows();
        assertEquals(expResult, result);
        //

    }

    /**
     * Test of isStrike method, of class Turn.
     */
    @Test
    public void testIsStrike() {
        EditableTurnImpl instance = new EditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isStrike();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        expResult = true;
        result = instance.isStrike();
        assertEquals(expResult, result);
    }

    /**
     * Test of isSpare method, of class Turn.
     */
    @Test
    public void testIsSpare() {
        EditableTurnImpl instance = new EditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        expResult = false;
        result = instance.isSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        instance.setBall2(0);
        expResult = false;
        result = instance.isSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(5);
        instance.setBall2(5);
        expResult = true;
        result = instance.isSpare();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(10);
        expResult = true;
        result = instance.isSpare();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNbPins method, of class Turn.
     */
    @Test
    public void testGetNbPins() {
        EditableTurnImpl instance = new EditableTurnImpl();
        int expResult = 0;
        int result = instance.getNbPins();
        assertEquals(expResult, result);
        //
        instance.setBall1(5);
        instance.setBall2(5);
        expResult = 10;
        result = instance.getNbPins();
        assertEquals(expResult, result);
        //
        instance.setBall1(10);
        instance.setBall2(5);
        expResult = 10;
        result = instance.getNbPins();
        assertEquals(expResult, result);
        //
        instance.setBall1(0);
        instance.setBall2(10);
        expResult = 10;
        result = instance.getNbPins();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNbPinForThrow method, of class Turn.
     */
    @Test
    public void testGetNbPinForThrow() {
        int throwNumber = 1;
        EditableTurnImpl instance = new EditableTurnImpl();
        int expResult = 0;
        int result = instance.getNbPinForThrow(throwNumber);
        assertEquals(expResult, result);
        //
        throwNumber = 2;
        expResult = 0;
        result = instance.getNbPinForThrow(throwNumber);
        assertEquals(expResult, result);
        //
        throwNumber = 0;
        try {
            result = instance.getNbPinForThrow(throwNumber);
            assertEquals(expResult, result);
            fail("Should throw an exception");
        } catch (Exception e) {
            // ok
        }

    }

    /**
     * Test of isSplit method, of class Turn.
     */
    @Test
    public void testIsSplit() {
        EditableTurnImpl instance = new EditableTurnImpl();
        boolean expResult = false;
        boolean result = instance.isSplit();
        assertEquals(expResult, result);
        //
        instance.setBall1(8);
        instance.setIsSplit(true);
        expResult = true;
        result = instance.isSplit();
        assertEquals(expResult, result);
        //TODO: add logic so min pin for split?
    }

}
