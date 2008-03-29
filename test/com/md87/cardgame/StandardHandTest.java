/*
 * StandardHandTest.java
 * JUnit 4.x based test
 *
 * Created on 26 July 2007, 21:14
 */

package com.md87.cardgame;

import com.md87.cardgame.hands.StandardHand;
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
public class StandardHandTest {
    
    public StandardHandTest() {
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
    public void compareToFlushes() {
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
        
        assertTrue("b < a", new StandardHand(b).compareTo(new StandardHand(a)) < 0);
        assertTrue("b = b", new StandardHand(b).compareTo(new StandardHand(b)) == 0);
        assertTrue("b = d", new StandardHand(b).compareTo(new StandardHand(d)) == 0);
        assertTrue("d < a", new StandardHand(d).compareTo(new StandardHand(a)) < 0);
    }
    
    @Test
    public void checkKickers() {
        Deck a = new Deck();
        a.add(new Card(Suit.CLUBS, Rank.KING));
        a.add(new Card(Suit.DIAMONDS, Rank.JACK));
        a.add(new Card(Suit.CLUBS, Rank.QUEEN));
        a.add(new Card(Suit.DIAMONDS, Rank.ACE));        
        a.add(new Card(Suit.CLUBS, Rank.SEVEN));
        a.add(new Card(Suit.DIAMONDS, Rank.SIX));        
        a.add(new Card(Suit.CLUBS, Rank.FIVE));        
        
        StandardHand h = new StandardHand(a);
        assertEquals(h.getKickers().size(), 5);
    }
    
    @Test
    public void checkFlushes() {
        Deck a = new Deck();
        a.add(new Card(Suit.CLUBS, Rank.ACE));
        a.add(new Card(Suit.CLUBS, Rank.KING));
        a.add(new Card(Suit.CLUBS, Rank.JACK));
        a.add(new Card(Suit.CLUBS, Rank.TEN));
        a.add(new Card(Suit.CLUBS, Rank.EIGHT));
        a.add(new Card(Suit.CLUBS, Rank.SEVEN));
        a.add(new Card(Suit.HEARTS, Rank.ACE));
        
        Deck b = new Deck();
        b.addAll(a);
        b.remove(6);
        b.remove(5);
        
        StandardHand h = new StandardHand(a);
        assertEquals("size = 5", h.getDeck().size(), 5);
        h.getDeck().removeAll(b);
        assertTrue("deck is correct", h.getDeck().isEmpty());
    }
    
    @Test
    public void checkNoPair() {
        Deck a = new Deck();
        
        a.add(new Card(Suit.DIAMONDS, Rank.NINE));
        a.add(new Card(Suit.HEARTS, Rank.QUEEN));
        a.add(new Card(Suit.SPADES, Rank.TEN));
        a.add(new Card(Suit.HEARTS, Rank.FIVE));
        a.add(new Card(Suit.SPADES, Rank.THREE));
        
        Deck b = new Deck(a);
        
        a.add(new Card(Suit.DIAMONDS, Rank.DEUCE));
        a.add(new Card(Suit.SPADES, Rank.ACE));
        b.add(new Card(Suit.CLUBS, Rank.DEUCE));
        b.add(new Card(Suit.HEARTS, Rank.EIGHT));
        
        StandardHand ha = new StandardHand(a);
        StandardHand hb = new StandardHand(b);
        
        assertEquals("Ranking = no pair", ha.getBestRank(), StandardHand.Ranking.NO_PAIR);
        assertEquals("Ranking = no pair", hb.getBestRank(), StandardHand.Ranking.NO_PAIR);
        assertEquals("Kickers = 5", ha.getKickers().size(), 5);
        assertEquals("Kickers = 5", hb.getKickers().size(), 5);
        assertTrue("ha > hb", ha.compareTo(hb) > 0);
    }
    
}
