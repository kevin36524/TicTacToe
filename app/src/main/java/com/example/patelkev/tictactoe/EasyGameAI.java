package com.example.patelkev.tictactoe;

import com.example.patelkev.tictactoe.Utils.GameAI;
import com.example.patelkev.tictactoe.Utils.GameManager;
import com.example.patelkev.tictactoe.Utils.TicTacToeDrawable;

import java.util.ArrayList;

/**
 * Created by patelkev on 11/25/16.
 */
public class EasyGameAI implements GameAI {

    @Override
    public TicTacToeDrawable playPiece(GameManager gameManager) {

        // Pick a piece at random
        ArrayList<TicTacToeDrawable> availableTiles = gameManager.getAvailableTiles();
        TicTacToeDrawable t = availableTiles.get((int) (availableTiles.size()*Math.random()));

        return t;
    }

}
