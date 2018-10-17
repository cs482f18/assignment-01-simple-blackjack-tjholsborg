package com.example.trentholsborg.blackjack;

/** 
 * Add Javadoc on top of every class
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class DeckOfCards
{
   /** Add javadoc to describe your instance varaible */
    private ArrayList<Card> deck;

    /**
     * Constructor to create the new deck of cards
     */
    public DeckOfCards()
    {
        newDeck();
    }

    /**
     * Instantiates the ArrayList of cards to be the deck.
     * Then calls another function to place cards into the deck ArrayList
     */
    public void newDeck()
    {
        deck = new ArrayList<Card>();
        cardsIntoNewDeck();
    }

    /**
     * Randomly selects a card from the ArrayList to be dealt to the user
     * @return the Card that was randomly chosen
     */
    public Card dealCard()
    {
        Random randIndex = new Random();
        Card dealtCard;

        int cardsRemaining = deck.size() - 1;
        int i = randIndex.nextInt(cardsRemaining - 1);
        dealtCard = deck.get(i);
        deck.remove(i);

        return dealtCard;
    }

    /**
     * Places the 52 cards into the deck based on their suit first then by number on the card
     * Jack - 11, Queen - 12, King - 13, Ace - 1
     */
    public void cardsIntoNewDeck()
    {
        Card createdCard;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 1; j < 14; j++)
            {
                if (i == 0)
                {
                    createdCard = new Card("spade", j);
                }
                else if (i == 1)
                {
                    createdCard= new Card("club", j);
                }
                else if (i == 2)
                {
                    createdCard = new Card("heart", j);
                }
                else
                {
                    createdCard = new Card("diamond", j);
                }

                deck.add(createdCard);
            }
        }


    }


}
