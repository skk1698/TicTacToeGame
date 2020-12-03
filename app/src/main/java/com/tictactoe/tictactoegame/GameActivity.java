package com.tictactoe.tictactoegame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final Button ticTacToeGameEngine = findViewById(R.id.tic_tac_toe_game_engine);

        ticTacToeGameEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticTacToeGameEngine.setVisibility(View.GONE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                TicTacToeGameFragment ticTacToeGameFragment = new TicTacToeGameFragment();
                fragmentManager.beginTransaction().replace(R.id.container, ticTacToeGameFragment).commit();
            }
        });
    }
}