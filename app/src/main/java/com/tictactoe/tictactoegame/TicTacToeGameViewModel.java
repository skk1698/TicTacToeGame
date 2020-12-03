package com.tictactoe.tictactoegame;

import android.widget.Button;

import androidx.annotation.IntRange;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TicTacToeGameViewModel extends ViewModel {
    public MutableLiveData<Button[][]> gridLiveData = new MutableLiveData<>();
    public String winner;
    @IntRange(from = 0) public int playerOneWinningPoints;
    @IntRange(from = 0) public int playerTwoWinningPoints;
    @IntRange(from = 0) public int playersMovesCount;
    public boolean isPlayerOneTurn;
    public boolean isDialogBoxOpen;
}
