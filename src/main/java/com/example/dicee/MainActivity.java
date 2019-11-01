package com.example.dicee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected int playerScore;
    protected int dealerScore;
    protected boolean playerTurn;
    protected boolean madeBet;
    protected int currentPlayerTotal;
    protected int playerTotal;
    protected int currentDealerTotal;
    protected int dealerTotal;
    protected int bet;

    protected Button hitButton;
    protected Button dealerHitButton;
    protected Button doubleButton;
    protected Button standButton;
    protected Button quitButton;

    protected ImageView leftDice;
    protected ImageView rightDice;
    protected EditText betEditText;
    protected TextView turnTextView;
    protected TextView playerScoreTextView;
    protected TextView dealerScoreTextView;

    protected int[] diceArray = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hitButton = (Button) findViewById(R.id.hitButton);
        dealerHitButton = (Button) findViewById(R.id.dealerPlayButton);
        doubleButton = (Button) findViewById(R.id.doubleButton);
        standButton = (Button) findViewById(R.id.standButton);
        quitButton = (Button) findViewById(R.id.quitButton);

        betEditText = (EditText) findViewById(R.id.betEditText);
        turnTextView = (TextView) findViewById(R.id.currentTurn);
        playerScoreTextView = (TextView) findViewById(R.id.playerScore);
        dealerScoreTextView = (TextView) findViewById(R.id.dealerScore);

        leftDice = (ImageView) findViewById(R.id.image_leftDice);
        rightDice = (ImageView) findViewById(R.id.image_rightDice);

        currentPlayerTotal = 0;
        playerTotal = 0;
        currentDealerTotal = 0;
        dealerTotal = 0;
        bet = 100;
        playerScore = 1000;
        dealerScore = 1000;

        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPlayerTurn();
            }
        });
        dealerHitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playDealerTurn();
            }
        });
        doubleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playDouble();
            }
        });
        standButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stand();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver();
            }
        });

        dealerHitButton.setClickable(true);
        hitButton.setClickable(false);
        doubleButton.setClickable(false);
        standButton.setClickable(false);

        makePlayerTurn();
    }

    public void playPlayerTurn() {
        betEditText.setEnabled(false);

        if (bet > playerScore) {
            bet = playerScore;
        }
        if (bet > dealerScore) {
            bet = dealerScore;
        }

        quitButton.setClickable(false);

        Random rng = new Random();
        int d1roll = rng.nextInt(6);
        int d2roll = rng.nextInt(6);
        playerTotal = d1roll + d2roll;

        leftDice.setImageResource(diceArray[d1roll]);
        rightDice.setImageResource(diceArray[d2roll]);

        if (playerTotal >= 10) {
            playerTotal = 10;
        }

        currentPlayerTotal += playerTotal;
        updateScoreDisplay(false, 69);

        if (currentPlayerTotal >= 21) {
            makeDealerTurn();
        }
    }

    public void playDealerTurn() {
        Random rng = new Random();
        currentDealerTotal = rng.nextInt(9) + 16;
        endRound(determineWinner());
    }

    public void playDouble() {
        betEditText.setEnabled(false);

        if (bet > playerScore) {
            bet = playerScore;
        }
        if (bet > dealerScore) {
            bet = dealerScore;
        }

        quitButton.setClickable(false);

        playPlayerTurn();
        bet *= 2;
        if (bet > playerScore) {
            bet = playerScore;
        }
        if (bet > dealerScore) {
            bet = dealerScore;
        }

        String betText = "" + bet;
        betEditText.setText(betText);
        makeDealerTurn();
    }

    public void stand() {
        betEditText.setEnabled(false);

        if (bet > playerScore) {
            bet = playerScore;
        }
        if (bet > dealerScore) {
            bet = dealerScore;
        }

        quitButton.setClickable(false);

        makeDealerTurn();
    }

    public void makePlayerTurn() {
        changeClickable(dealerHitButton);
        changeClickable(hitButton);
        changeClickable(doubleButton);
        changeClickable(standButton);
        //dealerHitButton.setClickable(false);
        //hitButton.setClickable(true);
        //doubleButton.setClickable(true);
        //standButton.setClickable(true);
        playerTurn = true;


        changeClickable(quitButton);
        betEditText.setEnabled(true);
        //quitButton.setClickable(true);
    }

    public void makeDealerTurn() {
        changeClickable(dealerHitButton);
        changeClickable(hitButton);
        changeClickable(doubleButton);
        changeClickable(standButton);
        //dealerHitButton.setClickable(true);
        //hitButton.setClickable(false);
        //doubleButton.setClickable(false);
        //standButton.setClickable(false);
        playerTurn = false;
    }

    public void changeClickable(Button b) {
        if (b.isClickable()) {
            b.setBackgroundColor(getResources().getColor(R.color.grey));
            b.setClickable(false);
        } else {
            b.setBackgroundColor(getResources().getColor(R.color.buttonBlue));
            b.setClickable(true);
        }
    }

    public void updateScoreDisplay(boolean roundOver, int winner) {
        String turnText = "Current player total: " + currentPlayerTotal
                + "\nCurrent dealer total: " + currentDealerTotal;

        String userScoreText = "User: " + playerScore;
        String dealerScoreText = "Dealer: " + dealerScore;
        playerScoreTextView.setText(userScoreText);
        dealerScoreTextView.setText(dealerScoreText);

        if (roundOver) {
            if (winner == 1) {
                turnText += "\nYou won! Enter a bet and press 'Hit' to play again!";
            }
            else if (winner == 0) {
                turnText += "\nIt's a tie. Play again? Enter a bet and press 'Hit'";
            }
            else {
                turnText += "\nYou lost...Enter a bet and press 'Hit' for another shot";
            }
        }
        turnTextView.setText(turnText);
    }

    public int determineWinner() {
        if (currentPlayerTotal > 21 || currentDealerTotal > 21) {
            if (currentPlayerTotal > 21 && currentDealerTotal > 21) {
                return 0;
            }
            else if (currentPlayerTotal > 21) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else if (currentPlayerTotal > currentDealerTotal) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public void endRound(int winner) {
        bet = Integer.parseInt(betEditText.getText().toString());

        if (winner == 1) {
            playerScore += bet;
            dealerScore -= bet;
        }
        else if (winner == -1) {
            playerScore -= bet;
            dealerScore += bet;
        }

        updateScoreDisplay(true, winner);

        if (playerScore == 0 || dealerScore == 0) {
            gameOver();
        }
        else {
            makePlayerTurn();
        }
    }

    public void gameOver() {
        dealerHitButton.setClickable(true);
        hitButton.setClickable(true);
        doubleButton.setClickable(true);
        standButton.setClickable(true);
        changeClickable(dealerHitButton);
        changeClickable(hitButton);
        changeClickable(doubleButton);
        changeClickable(standButton);
    }
}
