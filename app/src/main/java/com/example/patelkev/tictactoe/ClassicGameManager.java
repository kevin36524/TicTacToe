package com.example.patelkev.tictactoe;

import com.example.patelkev.tictactoe.Utils.GameAI;
import com.example.patelkev.tictactoe.Utils.GameManager;
import com.example.patelkev.tictactoe.Utils.TicTacToeConstants;
import com.example.patelkev.tictactoe.Utils.TicTacToeDrawable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by patelkev on 11/25/16.
 */

public class ClassicGameManager implements GameManager {

    // Constants
    public static final int NUM_ROWS = 3;
    public static final int NUM_COLS = 3;

    private int mStartingTurn;
    private ArrayList<TicTacToeDrawable> mTileList;

    private ArrayList<TicTacToeDrawable> mAvailableTiles;
    private GameAI mGameAI;
    private int mGameState;

    /**
     *
     * @param tileList
     *            All the tiles on the gameboard. Must be non-null
     * @param gameAI
     *            The default gameAI for the game. Must be non-null
     */
    public ClassicGameManager(ArrayList<TicTacToeDrawable> tileList,
                              GameAI gameAI) {
        mGameAI = gameAI;
        mTileList = tileList;

        // Init available tiles
        mAvailableTiles = new ArrayList<TicTacToeDrawable>(mTileList);
        Collections.copy(mAvailableTiles, mTileList);

        // Initialize the turn and game state
        mStartingTurn = TicTacToeConstants.TURN_PLAYER;
        mGameState = TicTacToeConstants.GAME_STATE_PLAYING;

    }

    @Override
    public void playerClickedTile(TicTacToeDrawable tile) {

        // The player is always x
        if (tile.getState() == TicTacToeConstants.TILE_STATE_EMPTY)
            changeTileValue(tile, TicTacToeConstants.TILE_STATE_X);

        if (mGameState == TicTacToeConstants.GAME_STATE_PLAYING) {

            // have the gameAI play
            TicTacToeDrawable t = mGameAI.playPiece(this);
            changeTileValue(t, TicTacToeConstants.TILE_STATE_O);
        }

    }

    @Override
    public void setStartingTurn(int startTurn) {
        mStartingTurn = startTurn;
    }

    @Override
    public boolean checkWin(int tileState, int row, int col) {

        int northSouth = 0;
        int eastWest = 0;
        int northwestSoutheast = 0;
        int northeastSouthwest = 0;

        // Loop through all the pieces
        for (TicTacToeDrawable curTile : mTileList) {
            int curRow = curTile.getRow();
            int curCol = curTile.getCol();
            int curState = curTile.getState();

            if (curState == tileState) {

                if (curCol == col) {
                    if (curRow < row || curRow > row)
                        northSouth++;
                } else if (curRow == row) {
                    if (curCol < col || curCol > col)
                        eastWest++;
                } else if ((curRow - row) == (curCol - col)) {
                    northwestSoutheast++;
                } else if ((curRow - row) == -(curCol - col)) {
                    northeastSouthwest++;
                }
            }
        }

        // Check for a win in all the possible directions
        if (northSouth == 2)
            return true;
        if (eastWest == 2)
            return true;
        if (northwestSoutheast == 2)
            return true;
        if (northeastSouthwest == 2)
            return true;

        return false;
    }

    @Override
    public void setGameAI(GameAI gameAI) {
        mGameAI = gameAI;
    }

    @Override
    public void setTileList(ArrayList<TicTacToeDrawable> tileList) {
        mTileList = tileList;

        mAvailableTiles = new ArrayList<TicTacToeDrawable>(mTileList);
        Collections.copy(mAvailableTiles, mTileList);

    }

    @Override
    public void reset() {
        // Change all the tiles back to being available
        mAvailableTiles = new ArrayList<TicTacToeDrawable>(mTileList);
        Collections.copy(mAvailableTiles, mTileList);

        // Loop through and set all the tiles to empty
        for (TicTacToeDrawable curTile : mAvailableTiles)
            curTile.setState(TicTacToeConstants.TILE_STATE_EMPTY);

        // Set the game state to ready
        mGameState = TicTacToeConstants.GAME_STATE_PLAYING;

        // If the computer starts, have them play
        if (mStartingTurn == TicTacToeConstants.TURN_COMPUTER) {
            TicTacToeDrawable t = mGameAI.playPiece(this);
            changeTileValue(t, TicTacToeConstants.TILE_STATE_O);
        }

    }

    @Override
    public void changeTileValue(TicTacToeDrawable tile, int value) {
        // Set the state and update the list of available tiles
        tile.setState(value);
        mAvailableTiles.remove(tile);

        // Check for the win
        if (checkWin(value, tile.getRow(), tile.getCol())) {
            if (value == TicTacToeConstants.TILE_STATE_X) {
                mGameState = TicTacToeConstants.GAME_STATE_PLAYER_WINS;
            } else if (value == TicTacToeConstants.TILE_STATE_O) {
                mGameState = TicTacToeConstants.GAME_STATE_COMPUTER_WINS;
            }
        }

        // Check for a cats game
        if (mGameState == TicTacToeConstants.GAME_STATE_PLAYING
                && mAvailableTiles.size() <= 0) {
            mGameState = TicTacToeConstants.GAME_STATE_CATS_GAME;
        }
    }

    @Override
    public ArrayList<TicTacToeDrawable> getAvailableTiles() {
        return mAvailableTiles;
    }

    @Override
    public int getGameState() {
        return mGameState;
    }
}
