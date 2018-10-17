package com.example.trentholsborg.blackjack;

import android.util.Log;

public class Blackjack
{
    DeckOfCards deckOfCards;
    Player player;
    private int playerHasAce = 0;
    private int dealerHasAce = 0;
    public boolean playerTurn = true;

    /**
     * Constructor to create the deck of cards and the Player that will play blackjack
     */
    public Blackjack()
    {
        deckOfCards = new DeckOfCards();
        player = new Player();
    }

    /**
     * Checks to see how many Aces are in the players hands
     * @return int amount of Aces in players hand
     */
    public int playerAces()
    {
        return playerHasAce;
    }

    /**
     * Decrements the amount of Aces in the players hand after its value was changes from 11 to 1
     */
    public void decrementPlayerAces()
    {
        playerHasAce--;
    }

    /**
     * Checks to see how many Aces are in the dealers hands
     * @return int amount of Aces in dealers hand
     */
    public int dealerAces()
    {
        return dealerHasAce;
    }

    /**
     * Decrements the amount of Aces in the players hand after its value was changes from 11 to 1
     */
    public void decrementDealerAces()
    {
        dealerHasAce--;
    }

    /**
     * Retrieves a new card from the deck and adds that card to the player's hand
     * @return the new dealt card
     */
    public Card getNewCardPlayer()
    {
        Card newCard = deckOfCards.dealCard();
        addToPlayerHand(newCard);
        return newCard;
    }

    /**
     * Calls the player function to add the dealt card to the ArrayList of the player's hand
     * @param dealtCard the new card
     */
    public void addToPlayerHand(Card dealtCard)
    {
        player.playerAddToHand(dealtCard);
    }

    /**
     * Retrieves a new card form the deck and adds that card to the dealer's hand
     * @return the new dealt card
     */
    public Card getNewCardDealer()
    {
        Card newCard = deckOfCards.dealCard();
        addToDealerHand(newCard);
        return newCard;
    }

    /**
     * Calls the player class function to add the dealt card to the ArrayList of the dealer's hand
     * @param dealtCard the new card
     */
    public void addToDealerHand(Card dealtCard)
    {
        player.dealerAddToHand(dealtCard);
    }

    /**
     * Calls the player class function to clear the ArrayList of the player's hand
     */
    public void clearPlayerHand()
    {
        player.playerClearHand();
    }

    /**
     * Calls the player class function to clear the ArrayList of the dealer's hand
     */
    public void clearDealerHand()
    {
        player.dealerClearHand();
    }

    /**
     * Calls the player class function to get the sum of the player's hand
     * @param player the user that is playing
     * @return int sum of the player's hand
     */
    public int getPlayerHandTotal(Player player)
    {
        return player.playerGetTotal();
    }

    /**
     * Calls the player class function to get the sum of the dealer's hand
     * @param player the dealer played by the computer
     * @return int sum of the dealer's hand
     */
    public int getDealerHandTotal(Player player)
    {
        return player.dealerGetTotal();
    }

    /**
     * Retrieves the number on the card and determines the black jack value of the card
     * 2-10 are the same, Face are 10, Ace could be 1 or 11
     * @param dealtCard the new card
     * @return the int blackjack value of the card
     */
    public int getCardNum(Card dealtCard)
    {
        if(dealtCard.cardNum <= 10 && dealtCard.cardNum >= 2 )
        {
            return dealtCard.getCardNum();
        }
        else if(dealtCard.cardNum > 10)
        {
            return dealtCard.getFaceCardNum();
        }
        else
        {
            if(playerTurn == true)
            {
                if (player.playerGetTotal() + 10 > 21)
                {
                    playerHasAce++;
                    return 1;
                }
                else
                {
                    playerHasAce++;
                    return 11;
                }
            }
            else
            {
                if (player.dealerGetTotal() + 10 > 21)
                {
                    dealerHasAce++;
                    return 1;
                }
                else
                {
                    dealerHasAce++;
                    return 11;
                }
            }
        }
    }


}
