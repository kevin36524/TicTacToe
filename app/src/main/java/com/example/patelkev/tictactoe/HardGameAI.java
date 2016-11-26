package com.example.patelkev.tictactoe;

import com.example.patelkev.tictactoe.Utils.GameAI;
import com.example.patelkev.tictactoe.Utils.GameManager;
import com.example.patelkev.tictactoe.Utils.TicTacToeConstants;
import com.example.patelkev.tictactoe.Utils.TicTacToeDrawable;

import java.util.ArrayList;

/**
 * Created by patelkev on 11/25/16.
 */

public class HardGameAI implements GameAI {

    /**
     * Play piece
     * Assumes the player is X and the computer is O
     */
    @Override
    public TicTacToeDrawable playPiece(GameManager gameManager) {

        ArrayList<TicTacToeDrawable> availableTiles = gameManager.getAvailableTiles();

        // Loop through all the tiles and to check for a win
        for(TicTacToeDrawable t : availableTiles) {
            if(gameManager.checkWin(TicTacToeConstants.TILE_STATE_O, t.getRow(), t.getCol())) {
                return t;
            }
        }

        // Loop through all the tiles and prevent a player win
        for(TicTacToeDrawable t : availableTiles) {
            if(gameManager.checkWin(TicTacToeConstants.TILE_STATE_X, t.getRow(), t.getCol())) {
                return t;
            }
        }

        // Default to picking a random tile
        TicTacToeDrawable t = availableTiles.get((int) (availableTiles.size()*Math.random()));

        return t;
    }

}
