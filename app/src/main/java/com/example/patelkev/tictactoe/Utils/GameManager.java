package com.example.patelkev.tictactoe.Utils;

import java.util.ArrayList;

/**
 * Created by patelkev on 11/25/16.
 */

public interface GameManager {

    /**
     * Set the total list of tiles for the board
     * @param tileList
     */
    public void setTileList(ArrayList<TicTacToeDrawable> tileList);

    /**
     * Get a list of the available tiles for playing
     * @return An ArrayList of available tiles on the board
     */
    public ArrayList<TicTacToeDrawable> getAvailableTiles();

    /**
     * Resets the game manager
     */
    public void reset();

    /**
     * Set the GameAI to be used in the game
     * @param gameAI the AI to use
     */
    public void setGameAI(GameAI gameAI);

    /**
     * Called when a player clicks a tile
     * @param tile the tile clicked
     */
    public void playerClickedTile(TicTacToeDrawable tile);

    /**
     * Called when you want a tile to change value
     * @param tile
     */
    public void changeTileValue(TicTacToeDrawable tile, int value);

    /**
     * Set to determine who starts
     * @param nextTurn who starts the game
     */
    public void setStartingTurn(int startTurn);


    /**
     * Check to see if setting a tile would cause a win for that tile
     * @param tilePiece the piece that would be set
     * @param row the row to place the tile
     * @param col the column to place the tile
     * @return true if it would be a win, false if otherwise
     */
    public boolean checkWin(int tilePiece, int row, int col);

    /**
     * Get the current state of the game
     * @return An integer value representing the current state of the game
     */
    public int getGameState();
}
