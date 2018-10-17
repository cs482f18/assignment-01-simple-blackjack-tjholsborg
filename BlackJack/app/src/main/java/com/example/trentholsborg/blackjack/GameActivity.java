package com.example.trentholsborg.blackjack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private Blackjack blackjack;
    private Player player;
    private Player dealer;

    private boolean pCardOneFilled = false;
    private boolean pCardTwoFilled = false;
    private boolean pCardThreeFilled = false;
    private boolean pCardFourFilled = false;
    private boolean pCardFiveFilled = false;

    private boolean dCardTwoFilled = false;
    private boolean dCardThreeFilled = false;
    private boolean dCardFourFilled = false;
    private boolean dCardFiveFilled = false;

    private String winnerString;


    /**
     * Create the instance of the Game's view
     * @param savedInstanceState the state of the View
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        player = new Player();
        dealer = new Player();
        blackjack = new Blackjack();

        int startValue = this.newGame();
        dealer.dealerAddTotal(startValue);
        DealerTotal();
    }

    /**
     * Function that is called by the Stand button. It switches from Player's turn to Dealer's turn.
     * The Dealer then draws cards to decide the game. Based on his cards the game reveals who won
     * @param v the view
     */
    public void Stand(View v)
    {
        blackjack.playerTurn = false;

        Button hitButton = findViewById(R.id.HitButton);
        Button standButton = findViewById(R.id.StandButton);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);

        for(int i = 0; i < 4; i++ )
        {
            if (gameOver() == false)
            {
                int cardValue = getDealtCard();

                if(cardValue == 11 && (dealer.dealerGetTotal() + 11) > 21)
                {
                    cardValue = 1;
                }
                if((blackjack.dealerAces() > 1) && (dealer.dealerGetTotal() + cardValue > 21))
                {
                    cardValue -= 10;
                    blackjack.decrementDealerAces();
                }

                dealer.dealerAddTotal(cardValue);
                DealerTotal();
            }
        }
        gameOver();
        TextView winnerText = findViewById(R.id.WinnerText);
        winnerText.setText(winnerText());
        winnerText.setVisibility(View.VISIBLE);

    }

    /**
     * Function that is called when the hit button is clicked. Hit gives the player a new card.
     * Then decides if the game is able to continue or is over. If it is over it displays
     * the winner.
     * @param v the view
     */
    public void Hit(View v)
    {
        if(pCardFiveFilled == false)
        {
            Log.w("MainActivity", "Hit clicked");
            int cardValue = getDealtCard();

            if(cardValue == 11 && (player.playerGetTotal() + 11) > 21)
            {
                cardValue = 1;
            }
            if((blackjack.playerAces() > 1) && (player.playerGetTotal() + cardValue > 21))
            {
                cardValue -= 10;
                blackjack.decrementPlayerAces();
            }

            player.playerAddTotal(cardValue);
            PlayerTotal();
        }

        if(pCardFiveFilled == true)
        {
            Stand(v);
        }

        Log.w("MainActivity", "Returns: " + gameOver());

        if(gameOver() == true)
        {
            TextView winnerText = findViewById(R.id.WinnerText);
            Button hitButton = findViewById(R.id.HitButton);
            Button standButton = findViewById(R.id.StandButton);
            winnerText.setText(winnerText());
            winnerText.setVisibility(View.VISIBLE);
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
        }
    }

    /**
     * Function that is called when New Game is clicked on the game screen.
     * The Board is reset entirely, both hands cleared and the dealer is given a new card to start
     * @param v the view
     */
    public void ClearBoardClick(View v)
    {
        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
        pCardOne.setImageResource(R.drawable.back);
        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
        pCardTwo.setImageResource(R.drawable.back);
        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
        pCardThree.setImageResource(R.drawable.back);
        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
        pCardFour.setImageResource(R.drawable.back);
        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
        pCardFive.setImageResource(R.drawable.back);

        ImageView dCardOne = findViewById(R.id.DealerCardOne);
        dCardOne.setImageResource(R.drawable.back);
        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
        dCardTwo.setImageResource(R.drawable.back);
        ImageView dCardThree = findViewById(R.id.DealerCardThree);
        dCardThree.setImageResource(R.drawable.back);
        ImageView dCardFour = findViewById(R.id.DealerCardFour);
        dCardFour.setImageResource(R.drawable.back);
        ImageView dCardFive = findViewById(R.id.DealerCardFive);
        dCardFive.setImageResource(R.drawable.back);

        TextView winnerText = findViewById(R.id.WinnerText);
        Button hitButton = findViewById(R.id.HitButton);
        Button standButton = findViewById(R.id.StandButton);
        winnerText.setVisibility(View.INVISIBLE);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);

        blackjack.clearPlayerHand();
        blackjack.clearDealerHand();
        player.playerResetTotal();
        dealer.dealerResetTotal();
        PlayerTotal();
        DealerTotal();


        pCardOneFilled = false;
        pCardTwoFilled = false;
        pCardThreeFilled = false;
        pCardFourFilled = false;
        pCardFiveFilled = false;
        dCardTwoFilled = false;
        dCardThreeFilled = false;
        dCardFourFilled = false;
        dCardFiveFilled = false;

        blackjack.playerTurn = true;

        int startValue = this.newGame();
        dealer.dealerAddTotal(startValue);
        DealerTotal();

    }

    /**
     * Sets the TextView of the Player's total in their hand.
     */
    public void PlayerTotal()
    {
        TextView pTotal = (TextView) findViewById(R.id.PlayerTotal);

        int total = blackjack.getPlayerHandTotal(this.player);
        pTotal.setText("Your Total: " + total);
    }

    /**
     * Sets the TextView of the Dealer's total in their hand
     */
    public void DealerTotal()
    {
        TextView dTotal = (TextView) findViewById(R.id.DealerTotal);

        int total = blackjack.getDealerHandTotal(this.dealer);
        dTotal.setText("Dealer Total: " + total);
    }

    /**
     * Returns the string of that shows who won the game
     * @return String winnerString
     */
    public String winnerText()
    {
        return winnerString;
    }

    /**
     * Checks the scenarios in which the game should stop because someone has won. Also sets the
     * winner String to portray who won.
     * @return boolean if the game is over
     */
    public boolean gameOver()
    {
        Log.w("MainActivity", "PlayerTotal" + player.playerGetTotal());
        if(player.playerGetTotal() > 21)
        {
            winnerString = "You busted";
            return true;
        }
        else if(player.playerGetTotal() == 21 && dealer.dealerGetTotal() != 21)
        {
            winnerString = "You win";
            return true;
        }
        else if(dealer.dealerGetTotal() > 21)
        {
            winnerString =  "You win";
            return true;
        }
        else if(dealer.dealerGetTotal() == 21 &&  player.playerGetTotal() == 21)
        {
            winnerString =  "Push";
            return true;
        }
        else if(player.playerGetTotal() < dealer.dealerGetTotal() && dealer.dealerGetTotal() < 22 && blackjack.playerTurn == false)
        {
            winnerString = "You Lose";
            return true;
        }
        else if(dealer.dealerGetTotal() > 16 && dealer.dealerGetTotal() < 21)
        {
            if(dealer.dealerGetTotal() > player.playerGetTotal())
            {
                winnerString = "You Lose";
                return true;
            }
            else if(dealer.dealerGetTotal() < player.playerGetTotal())
            {
                winnerString = "You Win";
                return true;
            }
            else
            {
                winnerString = "Push";
                return true;
            }
        }
        else
        {
            return false;
        }


    }

    /**
     * After the new game button is clicked and the board is cleared. This is used to deal out
     * a random first card to the Dealer
     * @return int value of the first card
     */
    public int newGame()
    {
        Card firstCard = blackjack.getNewCardDealer();

        if(firstCard.getCardNum() == 1)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ace_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ace_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ace_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ace_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 2)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.two_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.two_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.two_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.two_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 3)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 4)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.three_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 5)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.five_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.five_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.five_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.five_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 6)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.six_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.six_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.six_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.six_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 7)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.seven_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.seven_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.seven_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.seven_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 8)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.eight_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.eight_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.eight_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.eight_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 9)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.nine_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.nine_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.nine_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.nine_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 10)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ten_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ten_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ten_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.ten_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 11)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.jack_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.jack_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.jack_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.jack_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 12)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.queen_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.queen_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.queen_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.queen_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        else if(firstCard.getCardNum() == 13)
        {
            if(firstCard.getSuit() == "spade")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.king_of_spades);
            }
            else if(firstCard.getSuit() == "club")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.king_of_clubs);
            }
            else if(firstCard.getSuit() == "heart")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.king_of_hearts);
            }
            else if(firstCard.getSuit() == "diamond")
            {
                ImageView dCardOne = findViewById(R.id.DealerCardOne);
                dCardOne.setImageResource(R.drawable.king_of_diamonds);
            }

            return blackjack.getCardNum(firstCard);
        }
        return -1;
    }

    /**
     * Retrieves the dealt card and changes the ImageView according to the type of card that it is.
     * Also checks which Image Views have already been used
     * @return int value of the card that was dealt for total
     */
    public int getDealtCard()
    {
        Card topCard;

        if(blackjack.playerTurn == true)
        {
            topCard = blackjack.getNewCardPlayer();
            Log.w("MainActivity", "topCard = " + topCard.suit + topCard.cardNum);

            if(topCard.getCardNum() == 1)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ace_of_spades);
                    }

                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ace_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ace_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ace_of_diamonds);
                    }
                }

                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 2)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.two_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.two_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.two_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.two_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 3)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.three_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.three_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.three_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.three_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 4)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.four_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.four_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.four_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.four_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 5)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.five_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.five_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.five_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.five_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 6)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.six_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.six_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.six_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.six_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 7)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.seven_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.seven_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.seven_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.seven_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 8)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.eight_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.eight_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.eight_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.eight_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 9)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.nine_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.nine_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.nine_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.nine_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 10)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ten_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ten_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ten_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.ten_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 11)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.jack_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.jack_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.jack_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.jack_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 12)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.queen_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.queen_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.queen_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.queen_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 13)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.king_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.king_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne = findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.king_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(pCardOneFilled == false)
                    {
                        pCardOneFilled = true;
                        ImageView pCardOne =  findViewById(R.id.PlayerCardOne);
                        pCardOne.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(pCardTwoFilled == false)
                    {
                        pCardTwoFilled = true;
                        ImageView pCardTwo = findViewById(R.id.PlayerCardTwo);
                        pCardTwo.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(pCardThreeFilled == false)
                    {
                        pCardThreeFilled = true;
                        ImageView pCardThree = findViewById(R.id.PlayerCardThree);
                        pCardThree.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(pCardFourFilled == false)
                    {
                        pCardFourFilled = true;
                        ImageView pCardFour = findViewById(R.id.PlayerCardFour);
                        pCardFour.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(pCardFiveFilled == false)
                    {
                        pCardFiveFilled = true;
                        ImageView pCardFive = findViewById(R.id.PlayerCardFive);
                        pCardFive.setImageResource(R.drawable.king_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
        }
        else
        {
            topCard = blackjack.getNewCardDealer();
            if(topCard.getCardNum() == 1)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ace_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ace_of_spades);
                    }

                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ace_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ace_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ace_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ace_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ace_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ace_of_diamonds);
                    }
                }

                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 2)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.two_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.two_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.two_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.two_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.two_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.two_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.two_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.two_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 3)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.three_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.three_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.three_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.three_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.three_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.three_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.three_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.three_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 4)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.four_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.four_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.four_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.four_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.four_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.four_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.four_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.four_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 5)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.five_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.five_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.five_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.five_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.five_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.five_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.five_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.five_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 6)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.six_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.six_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.six_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.six_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.six_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.six_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.six_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.six_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 7)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.seven_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.seven_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.seven_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.seven_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.seven_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.seven_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.seven_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.seven_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 8)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.eight_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.eight_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.eight_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.eight_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.eight_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.eight_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.eight_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.eight_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 9)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.nine_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.nine_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.nine_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.nine_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.nine_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.nine_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.nine_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.nine_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 10)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ten_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ten_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ten_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ten_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ten_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ten_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.ten_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.ten_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 11)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.jack_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.jack_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.jack_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.jack_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.jack_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.jack_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.jack_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.jack_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 12)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.queen_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.queen_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.queen_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.queen_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.queen_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.queen_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.queen_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.queen_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
            if(topCard.getCardNum() == 13)
            {
                if(topCard.getSuit().equals("spade"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.king_of_spades);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.king_of_spades);
                    }
                }
                else if(topCard.getSuit().equals("club"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.king_of_clubs);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.king_of_clubs);
                    }
                }
                else if(topCard.getSuit().equals("heart"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.king_of_hearts);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.king_of_hearts);
                    }
                }
                else if(topCard.getSuit().equals("diamond"))
                {
                    if(dCardTwoFilled == false)
                    {
                        dCardTwoFilled = true;
                        ImageView dCardTwo = findViewById(R.id.DealerCardTwo);
                        dCardTwo.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(dCardThreeFilled == false)
                    {
                        dCardThreeFilled = true;
                        ImageView dCardThree = findViewById(R.id.DealerCardThree);
                        dCardThree.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(dCardFourFilled == false)
                    {
                        dCardFourFilled = true;
                        ImageView dCardFour = findViewById(R.id.DealerCardFour);
                        dCardFour.setImageResource(R.drawable.king_of_diamonds);
                    }
                    else if(dCardFiveFilled == false)
                    {
                        dCardFiveFilled = true;
                        ImageView dCardFive = findViewById(R.id.DealerCardFive);
                        dCardFive.setImageResource(R.drawable.king_of_diamonds);
                    }
                }
                return blackjack.getCardNum(topCard);
            }
        }
        return -1;
    }

}
