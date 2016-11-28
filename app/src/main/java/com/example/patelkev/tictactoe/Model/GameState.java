package com.example.patelkev.tictactoe.Model;

import com.example.patelkev.tictactoe.Utils.TicTacToeConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by patelkev on 11/27/16.
 */

public class GameState {

    ArrayList<GameMove> moves;
    int[] currentTileState;
    HashSet<String> xSids;
    HashSet<String> oSids;

    public String getJsonPayload() {
        Gson gson = new GsonBuilder().create();
        return  gson.toJson(this.moves);
    }

    public static GameState instanceFrom(String jsonString) {
        Gson gson = new GsonBuilder().create();
        GameState newGameState = new GameState();
        GameMove[] movesArray = gson.fromJson(jsonString, GameMove[].class);
        newGameState.moves =  new ArrayList<GameMove>(Arrays.asList(movesArray));
        newGameState.populateSids();
        newGameState.populateCurrentTileState();
        return newGameState;
    }

    private void populateSids() {
        xSids = new HashSet<String>();
        oSids = new HashSet<String>();
        for (int i = 0; i < moves.size() ; i++) {
            GameMove move = moves.get(i);
            if (move.tileState() == TicTacToeConstants.TILE_STATE_X) {
                xSids.add(move.sid);
            } else {
                oSids.add(move.sid);
            }
        }
    }
    private void populateCurrentTileState() {
        currentTileState = new int[] {0,0,0,0,0,0,0,0,0};
        for (int i = 0; i < moves.size() ; i++) {
            currentTileState[moves.get(i).position] = moves.get(i).tileState();
        }
    }

    public int getTileStateForTileNumber(int tileNumber) {
        return currentTileState[tileNumber];
    }

    public GameMove getLastMove() {
        return moves.get(moves.size() - 1);
    }

    public int getWinner() {
        int retWinnerState = TicTacToeConstants.TILE_STATE_EMPTY;
        int[] cs = currentTileState;

        if (cs[0] == cs[1] && cs[1] == cs[2] && cs[0] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[0];
        }

        if (cs[3] == cs[4] && cs[4] == cs[5] && cs[3] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[3];
        }

        if (cs[6] == cs[7] && cs[7] == cs[8] && cs[6] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[6];
        }

        if (cs[0] == cs[3] && cs[3] == cs[6] && cs[0] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[0];
        }

        if (cs[1] == cs[4] && cs[4] == cs[7] && cs[1] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[1];
        }

        if (cs[2] == cs[5] && cs[5] == cs[8] && cs[2] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[2];
        }

        if (cs[0] == cs[4] && cs[4] == cs[8] && cs[0] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[0];
        }

        if (cs[2] == cs[4] && cs[4] == cs[6] && cs[2] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return cs[2];
        }

        return retWinnerState;

    }

    public TicTacToeConstants.GameResultState getGameResult(String sid) {
        TicTacToeConstants.GameResultState winnerState = TicTacToeConstants.GameResultState.PLAYING;
        int winnerTileState = getWinner();

        if (winnerTileState == TicTacToeConstants.TILE_STATE_EMPTY) {
            for (int i: currentTileState) {
                if (i == TicTacToeConstants.TILE_STATE_EMPTY) {
                    return TicTacToeConstants.GameResultState.PLAYING;
                }
            }
            return TicTacToeConstants.GameResultState.DRAW;
        }

        if (winnerTileState == TicTacToeConstants.TILE_STATE_O && oSids.contains(sid)) {
            return TicTacToeConstants.GameResultState.WINS;
        }

        if (winnerTileState == TicTacToeConstants.TILE_STATE_X && xSids.contains(sid)) {
            return TicTacToeConstants.GameResultState.WINS;
        }

        return TicTacToeConstants.GameResultState.LOSE;
    }

    public Boolean updateGameState(String sid, int position) {
        if (currentTileState[position] != TicTacToeConstants.TILE_STATE_EMPTY) {
            return false;
        }

        GameMove lastMove = getLastMove();
        int nextTileState = TicTacToeConstants.TILE_STATE_O;
        Set<String> imPermissibleSids = xSids;
        if (lastMove.tileState() == TicTacToeConstants.TILE_STATE_O) {
            nextTileState = TicTacToeConstants.TILE_STATE_X;
            imPermissibleSids = oSids;
        }

        if (imPermissibleSids.contains(sid)) {
            return false;
        }

        moves.add(new GameMove(sid, nextTileState, position));
        currentTileState[position] = nextTileState;
        if (nextTileState == TicTacToeConstants.TILE_STATE_X) {
            xSids.add(sid);
        } else {
            oSids.add(sid);
        }

        return true;
    }
}
