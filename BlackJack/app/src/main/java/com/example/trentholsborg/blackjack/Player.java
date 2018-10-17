package com.example.trentholsborg.blackjack;

import android.util.Log;

import java.util.ArrayList;

public class Player
{
    private int playerTotal = 0;
    private int dealerTotal = 0;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;

    /**
     * Constructor that creates the ArrayList of the dealer's hand and player's hand
     */
    public Player()
    {
        playerHand = new ArrayList<Card>();
        dealerHand = new ArrayList<Card>();
    }

    /**
     * Adds the given card to the Player's hand
     * @param newCard the card given to the player
     */
    public void playerAddToHand(Card newCard)
    {
        playerHand.add(newCard);
    }

    /**
     * Adds the given card to the Dealer's hand
     * @param newCard the card given to the dealer
     */
    public void dealerAddToHand(Card newCard)
    {
        dealerHand.add(newCard);
    }

    /**
     * Gets the sum of the cards in the player's hand
     * @return int sum of the player's hand
     */
    public int playerGetTotal()
    {
        return playerTotal;
    }

    /**
     * Gets the sum of the cards in the dealer's hand
     * @return int sum of the dealer's hand
     */
    public int dealerGetTotal()
    {
        return dealerTotal;
    }

    /**
     * Resets the sum of the player's hand back to 0
     */
    public void playerResetTotal()
    {
        playerTotal = 0;
    }

    /**
     * Resets the sum of the dealer's hand back to 0
     */
    public void dealerResetTotal()
    {
        dealerTotal = 0;
    }

    /**
     * Clears out the player's ArrayList
     */
    public void playerClearHand()
    {
        playerHand.clear();
    }

    /**
     * Clears out the dealer's ArrayList
     */
    public void dealerClearHand()
    {
        dealerHand.clear();
    }

    /**
     * Adds the new cards value to the sum of the player's hand
     * @param value the new cards blackjack value
     */
    public void playerAddTotal(int value)
    {
        this.playerTotal += value ;
    }

    /**
     * Adds the new cards value to the sum of the dealer's hand
     * @param value the new cards blackjack value
     */
    public void dealerAddTotal(int value)
    {
        this.dealerTotal += value;
    }

}
