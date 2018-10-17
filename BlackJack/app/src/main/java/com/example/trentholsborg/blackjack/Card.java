package com.example.trentholsborg.blackjack;

/**
 * Add javadoc comment to describe the class with @author and @version. For example, 
 * A class to represent a Card object
 * @author Trent Holsborg
 * @version 1.0 10/05/2018
 */ 
public class Card
{
    public String suit;
    public int cardNum; // 1-Ace 11-Jack 12-Queen 13-King

    /**
     * Constructor that defines what each card will contain
     * @param cardSuit Spade, Club, Heart or Diamond
     * @param value the value is the number on the card, 2-10 Jack, Queen, King, Ace
     */
    public Card(String cardSuit, int value)
    {
        this.suit = cardSuit;
        this.cardNum = value;
    }

    /**
     * Gets the suit of the given card
     * @return the suit of the card
     */
    public String getSuit()
    {
        return this.suit;
    }

    /**
     * Gets the number that is on the card
     * @return the number on the card 2-10
     */
    public int getCardNum()
    {
        return this.cardNum;
    }

    /**
     * Gets the value of the face cards
     * @return All face cards have a value of 10
     */
    public int getFaceCardNum()
    {
        return 10;
    }


}
