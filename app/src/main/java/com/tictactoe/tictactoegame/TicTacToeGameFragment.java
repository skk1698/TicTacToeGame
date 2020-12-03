package com.tictactoe.tictactoegame;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.MessageFormat;

public class TicTacToeGameFragment extends Fragment {

    private static final String FIRST_PLAYER_WIN = "Winner : Player 1";
    private static final String SECOND_PLAYER_WIN = "Winner : Player 2";
    private static final String MATCH_DRAW = "Match Draw";
    private static final String FIRST_PLAYER_SYMBOL = "X";
    private static final String SECOND_PLAYER_SYMBOL = "O";
    @ColorInt private static final int FIRST_PLAYER_SYMBOL_COLOR = 0XFF1F84D5;
    @ColorInt private static final int SECOND_PLAYER_SYMBOL_COLOR = 0XFFF11A39;

    private TicTacToeGameViewModel ticTacToeGameViewModel;
    private Button[][] grid = new Button[3][3];
    private AppCompatImageView passPhoneToPlayerOne;
    private AppCompatImageView passPhoneToPlayerTwo;
    private TextView playerOneScoreBoard;
    private TextView playerTwoScoreBoard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ticTacToeGameView = inflater.inflate(R.layout.tic_tac_toe_game, container, false);
        playerOneScoreBoard = ticTacToeGameView.findViewById(R.id.player_one_score);
        playerTwoScoreBoard = ticTacToeGameView.findViewById(R.id.player_two_score);
        passPhoneToPlayerOne = ticTacToeGameView.findViewById(R.id.pass_phone_to_player_one);
        passPhoneToPlayerTwo = ticTacToeGameView.findViewById(R.id.pass_phone_to_player_two);

        ticTacToeGameViewModel = ViewModelProviders.of(this).get(TicTacToeGameViewModel.class);
        ticTacToeGameViewModel.gridLiveData.observe(this, new Observer<Button[][]>() {
            @Override
            public void onChanged(Button[][] dataMatrix) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        grid[i][j].setText(dataMatrix[i][j].getText());
                        grid[i][j].setTextColor(dataMatrix[i][j].getTextColors());
                    }
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String gridId = "grid_" + i + j;
                int resId = getResources().getIdentifier(gridId, "id", getActivity().getPackageName());
                grid[i][j] = ticTacToeGameView.findViewById(resId);

                grid[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((Button) v).getText().toString().equals("")) {
                            return;
                        }
                        ticTacToeGameViewModel.playersMovesCount++;

                        if (ticTacToeGameViewModel.isPlayerOneTurn) {
                            ((Button) v).setText(FIRST_PLAYER_SYMBOL);
                            ((Button) v).setTextColor(FIRST_PLAYER_SYMBOL_COLOR);
                        } else {
                            ((Button) v).setText(SECOND_PLAYER_SYMBOL);
                            ((Button) v).setTextColor(SECOND_PLAYER_SYMBOL_COLOR);
                        }

                        if (checkForWin()) {
                            if (ticTacToeGameViewModel.isPlayerOneTurn) {
                                playerOneWins();
                            } else {
                                playerTwoWins();
                            }
                        } else if (ticTacToeGameViewModel.playersMovesCount == 9) {
                            matchDraw();
                        } else {
                            switchPlayersTurn();
                        }
                        ticTacToeGameViewModel.gridLiveData.setValue(grid);
                    }
                });
            }
        }

        if (ticTacToeGameViewModel.isDialogBoxOpen) {
            showDialog();
        }

        passPhoneToPlayerOne.setVisibility(ticTacToeGameViewModel.isPlayerOneTurn ? View.VISIBLE : View.INVISIBLE);
        passPhoneToPlayerTwo.setVisibility(ticTacToeGameViewModel.isPlayerOneTurn ? View.INVISIBLE : View.VISIBLE);

        Button buttonReset = ticTacToeGameView.findViewById(R.id.reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        return ticTacToeGameView;
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = grid[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
                    || (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))) {
                return true;
            }
        }

        return (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
                || (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""));
    }

    private void updateScoreBoard() {
        playerOneScoreBoard.setText(MessageFormat.format("{0}", ticTacToeGameViewModel.playerOneWinningPoints));
        playerTwoScoreBoard.setText(MessageFormat.format("{0}", ticTacToeGameViewModel.playerTwoWinningPoints));
        resetTicTacToeBoard();
    }

    private void resetTicTacToeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j].setText("");
            }
        }
        ticTacToeGameViewModel.playersMovesCount = 0;
        switchPlayersTurn();
    }

    private void switchPlayersTurn() {
        ticTacToeGameViewModel.isPlayerOneTurn = !ticTacToeGameViewModel.isPlayerOneTurn;
        passPhoneToPlayerOne.setVisibility(ticTacToeGameViewModel.isPlayerOneTurn ? View.VISIBLE : View.INVISIBLE);
        passPhoneToPlayerTwo.setVisibility(ticTacToeGameViewModel.isPlayerOneTurn ? View.INVISIBLE : View.VISIBLE);
    }

    private void playerOneWins() {
        ticTacToeGameViewModel.playerOneWinningPoints++;
        ticTacToeGameViewModel.winner = FIRST_PLAYER_WIN;
        showDialog();
    }

    private void playerTwoWins() {
        ticTacToeGameViewModel.playerTwoWinningPoints++;
        ticTacToeGameViewModel.winner = SECOND_PLAYER_WIN;
        showDialog();
    }

    private void matchDraw() {
        ticTacToeGameViewModel.winner = MATCH_DRAW;
        showDialog();
    }

    private void resetGame() {
        ticTacToeGameViewModel.playerOneWinningPoints = 0;
        ticTacToeGameViewModel.playerTwoWinningPoints = 0;
        updateScoreBoard();
    }

    private void showDialog() {
        ticTacToeGameViewModel.isDialogBoxOpen = true;
        if (getContext() != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle(ticTacToeGameViewModel.winner)
                    .setPositiveButton(R.string.alert_dialog_restart_game, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ticTacToeGameViewModel.isDialogBoxOpen = false;
                            resetGame();
                        }
                    })
                    .setNegativeButton(R.string.alert_dialog_continue_game, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ticTacToeGameViewModel.isDialogBoxOpen = false;
                            updateScoreBoard();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ticTacToeGameViewModel.isDialogBoxOpen = false;
                            updateScoreBoard();
                        }
                    })
                    .show();
        }
    }
}