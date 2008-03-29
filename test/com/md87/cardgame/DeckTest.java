/*
 * HandTest.java
 * JUnit 4.x based test
 *
 * Created on 26 July 2007, 21:14
 */

package com.md87.cardgame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class DeckTest {
    
    public DeckTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void compareTo() {
        Deck a = new Deck();
        Deck b = new Deck();
        Deck c = new Deck();
        Deck d = new Deck();
        
        c.add(new Card(Suit.CLUBS, Rank.ACE));
        c.add(new Card(Suit.CLUBS, Rank.TEN));
        c.add(new Card(Suit.CLUBS, Rank.SEVEN));
        c.add(new Card(Suit.CLUBS, Rank.DEUCE));
        
        b.addAll(c);
        b.add(new Card(Suit.CLUBS, Rank.THREE));
        b.add(new Card(Suit.HEARTS, Rank.ACE));
        
        a.addAll(c);
        a.add(new Card(Suit.CLUBS, Rank.KING));
        a.add(new Card(Suit.DIAMONDS, Rank.KING));
        
        d.addAll(c);
        d.add(new Card(Suit.CLUBS, Rank.THREE));
        d.add(new Card(Suit.HEARTS, Rank.THREE));
        
        Deck e = new Deck();
        Deck f = new Deck();
        e.add(new Card(Suit.HEARTS, Rank.ACE));
        f.add(new Card(Suit.CLUBS, Rank.THREE));
        
        assertTrue("b > a", b.compareTo(a) > 0);
        assertTrue("b = b", b.compareTo(b) == 0);
        assertTrue("b > d", b.compareTo(d) > 0);
        assertTrue("d < a", d.compareTo(a) < 0);
        assertTrue("e > f", e.compareTo(f) > 0);
    }
    
}
