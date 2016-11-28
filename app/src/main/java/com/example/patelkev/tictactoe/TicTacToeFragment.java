package com.example.patelkev.tictactoe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patelkev.tictactoe.Model.GameMove;
import com.example.patelkev.tictactoe.Model.GameState;
import com.example.patelkev.tictactoe.Utils.TicTacToeConstants;
import com.example.patelkev.tictactoe.Utils.TicTacToeDrawable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static com.example.patelkev.tictactoe.R.drawable.x;
import static com.example.patelkev.tictactoe.Utils.TicTacToeConstants.GameResultState.DRAW;
import static com.example.patelkev.tictactoe.Utils.TicTacToeConstants.GameResultState.LOSE;
import static com.example.patelkev.tictactoe.Utils.TicTacToeConstants.GameResultState.PLAYING;
import static com.example.patelkev.tictactoe.Utils.TicTacToeConstants.GameResultState.WINS;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicTacToeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicTacToeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicTacToeFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private TileClickHandler mClickHandler;
    private GameState gameState;
    private TicTacToeConstants.GameResultState gameResultState;
    private TextView tvResult;

    public interface TTTParentInterface {
        public String getCurrentSenderID();
        public void sendMessage(String messageToBeSent);
    }

    public static final int[] GAME_TILE_IDS = { R.id.GameTile01,
            R.id.GameTile02, R.id.GameTile03, R.id.GameTile04, R.id.GameTile05,
            R.id.GameTile06, R.id.GameTile07, R.id.GameTile08, R.id.GameTile09 };

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;
    TTTParentInterface mInterface;

    public TicTacToeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TicTacToeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicTacToeFragment newInstance(String param1, TTTParentInterface mInterface) {
        TicTacToeFragment fragment = new TicTacToeFragment();
        Bundle args = new Bundle();
        param1 = "[{\"sid\": \"s1\", \"move\": \"x\", \"position\": 0}, {\"sid\": \"s2\", \"move\": \"o\", \"position\": 8}, {\"sid\": \"s3\", \"move\": \"x\", \"position\": 5}]";
        args.putString(ARG_PARAM1, param1);
        fragment.mInterface = mInterface;
        fragment.setArguments(args);
        return fragment;
    }

    private void getAllTiles(View retView) {
        TicTacToeTile.setDrawableX(ContextCompat.getDrawable(this.getContext(), x));
        TicTacToeTile.setDrawableO(ContextCompat.getDrawable(this.getContext(), R.drawable.circle));
        TicTacToeTile.setDrawableBlank(ContextCompat.getDrawable(this.getContext(), R.drawable.blank));

        ArrayList<TicTacToeDrawable> tileList = new ArrayList<TicTacToeDrawable>(GAME_TILE_IDS.length);

        mClickHandler = new TileClickHandler();

        gameState = GameState.instanceFrom(mParam1);
        // Set the click handler for all the tiles and build up a list
        for(int i=0; i<GAME_TILE_IDS.length; i++) {
            TicTacToeTile curTile = (TicTacToeTile) retView.findViewById(GAME_TILE_IDS[i]);
            curTile.setState(gameState.getTileStateForTileNumber(i));
            curTile.setOnClickListener(mClickHandler);
            tileList.add(curTile);
        }
    }

    private void populateTVResult() {

        gameResultState = gameState.getGameResult(mInterface.getCurrentSenderID());

        if (gameResultState == PLAYING) {
            return;
        }

        if (gameResultState == WINS) {
            tvResult.setText("!! You Win !!");
            return;
        }

        if (gameResultState == DRAW) {
            tvResult.setText("Its a Draw");
            return;
        }

        if (gameResultState == LOSE) {
            tvResult.setText("You Lost");
            return;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View retView  = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        getAllTiles(retView);
        tvResult = (TextView) retView.findViewById(R.id.tvResult);
        populateTVResult();
        return retView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class TileClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // Click the tile
            TicTacToeDrawable tile = (TicTacToeDrawable) v;
            if (gameResultState != PLAYING) {
                return;
            }
            playerClickedTile(tile);
        }

        public void playerClickedTile(TicTacToeDrawable tile) {
            int tilenumber = 3*tile.getRow() + tile.getCol();
            String senderID = mInterface.getCurrentSenderID();
            if (!gameState.updateGameState(senderID, tilenumber)) { return; }
            GameMove lastMove = gameState.getLastMove();
            tile.setState(lastMove.tileState());
            Log.d("KEVINDEBUG", mInterface.getCurrentSenderID() +" clicked Tile " + tilenumber);
            updateStatusMessage();
        }

        public void updateStatusMessage() {
            mInterface.sendMessage(gameState.getJsonPayload());
            populateTVResult();
        }
    }
}
