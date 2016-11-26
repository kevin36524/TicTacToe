package com.example.patelkev.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.patelkev.tictactoe.Utils.GameManager;
import com.example.patelkev.tictactoe.Utils.TicTacToeConstants;
import com.example.patelkev.tictactoe.Utils.TicTacToeDrawable;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final int[] GAME_TILE_IDS = { R.id.GameTile01,
            R.id.GameTile02, R.id.GameTile03, R.id.GameTile04, R.id.GameTile05,
            R.id.GameTile06, R.id.GameTile07, R.id.GameTile08, R.id.GameTile09 };

    private GameManager mGameManager;
    private TileClickHandler mClickHandler;
    private EasyGameAI mEasyGameAI;
    private HardGameAI mHardGameAI;
    private TextView mTextViewStatus;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the main content view
        setContentView(R.layout.main);

        // Set the drawables for the tiles

        TicTacToeTile.setDrawableX(ContextCompat.getDrawable(getApplicationContext(), R.drawable.x));
        TicTacToeTile.setDrawableO(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle));
        TicTacToeTile.setDrawableBlank(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blank));

        ArrayList<TicTacToeDrawable> tileList = new ArrayList<TicTacToeDrawable>(GAME_TILE_IDS.length);
        mClickHandler = new TileClickHandler();

        // Set the click handler for all the tiles and build up a list
        for(int i=0; i<GAME_TILE_IDS.length; i++) {
            TicTacToeTile curTile = (TicTacToeTile) findViewById(GAME_TILE_IDS[i]);
            curTile.setOnClickListener(mClickHandler);
            tileList.add(curTile);
        }

        mEasyGameAI = new EasyGameAI();
        mHardGameAI = new HardGameAI();

        // Default to using the EasyGameAI
        mGameManager = new ClassicGameManager(tileList, mEasyGameAI);
        CheckBox playerGoesFirst = (CheckBox) findViewById(R.id.checkBoxPlayerFirst);
        if(playerGoesFirst.isChecked())
            mGameManager.setStartingTurn(TicTacToeConstants.TURN_PLAYER);
        else
            mGameManager.setStartingTurn(TicTacToeConstants.TURN_COMPUTER);


        // Set whether the player should go first or not
        playerGoesFirst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    mGameManager.setStartingTurn(TicTacToeConstants.TURN_PLAYER);
                else
                    mGameManager.setStartingTurn(TicTacToeConstants.TURN_COMPUTER);
            }
        });

        // Start new easy game
        Button easyButton = (Button) findViewById(R.id.buttonEasyGame);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the AI to the easy AI
                mGameManager.setGameAI(mEasyGameAI);
                mGameManager.reset();
                updateStatusMessage();
            }
        });

        // Start new hard game
        Button hardButton = (Button) findViewById(R.id.buttonHardGame);
        hardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Set the AI to the hard AI
                mGameManager.setGameAI(mHardGameAI);
                mGameManager.reset();
                updateStatusMessage();
            }
        });

        // Save the text view for the status
        mTextViewStatus = (TextView) findViewById(R.id.textViewStatusMessage);

    }

    /**
     * Will set the status message based on the current game state
     */
    public void updateStatusMessage() {

        // Get the games current state
        int gameState = mGameManager.getGameState();
        Resources res = getResources();

        if(gameState == TicTacToeConstants.GAME_STATE_PLAYING) {
            mTextViewStatus.setText(res.getString(R.string.playing_game));
        } else if(gameState == TicTacToeConstants.GAME_STATE_PLAYER_WINS) {
            mTextViewStatus.setText(res.getString(R.string.player_wins));
        } else if (gameState == TicTacToeConstants.GAME_STATE_COMPUTER_WINS) {
            mTextViewStatus.setText(res.getString(R.string.computer_wins));
        } else if (gameState == TicTacToeConstants.GAME_STATE_CATS_GAME) {
            mTextViewStatus.setText(res.getString(R.string.tie_game));
        }
    }

    /**
     * Private class to handle when a tile is clicked
     * @author Dominic
     *
     */
    private class TileClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mGameManager.getGameState() == TicTacToeConstants.GAME_STATE_PLAYING
                    && v instanceof TicTacToeDrawable) {

                // Click the tile
                TicTacToeDrawable tile = (TicTacToeDrawable) v;
                if(tile.getState() == TicTacToeConstants.TILE_STATE_EMPTY) {
                    mGameManager.playerClickedTile(tile);
                }

                // Update the status message
                updateStatusMessage();
            }
        }

    }
}
