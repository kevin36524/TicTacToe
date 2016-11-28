package com.example.patelkev.tictactoe;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.EditText;

import com.example.patelkev.tictactoe.Utils.TicTacToeConstants;

public class NewTTTActivity extends FragmentActivity implements TicTacToeFragment.OnFragmentInteractionListener, TicTacToeFragment.TTTParentInterface {

    private EditText etSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ttt);
        etSender = (EditText) findViewById(R.id.etSender);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder, TicTacToeFragment.newInstance("param1", this));
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public String getCurrentSenderID() {
        return etSender.getText().toString();
    }

    @Override
    public void sendMessage(String messageToBeSent) {
        Log.d("KEVINDEBUG", "Send the message " + messageToBeSent);
    }
}
