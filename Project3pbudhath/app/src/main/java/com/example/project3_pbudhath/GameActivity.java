package com.example.project3_pbudhath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class GameActivity extends AppCompatActivity {

    private GridView grid;

    private Button back_game;

    private int[][] board;

    private ArrayAdapter<String> adapter;

    private int player_id;

    private int mode;

    private int moveCount;

    public final int PLAYER1 = 1;
    public final int PLAYER2 = 2;
    public static final int MODE_ONLINE = 1;
    public static final int MODE_OFFLINE = 0;

    private boolean playing;

    public static final String GAME_MODE = "GAME_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mode = intent.getIntExtra(this.GAME_MODE, MODE_OFFLINE);
        setContentView(R.layout.activity_game);

        back_game = findViewById(R.id.Button_Game_Back);
        back_game.setOnClickListener(view -> finish());
        board = new int[3][3];
        grid = findViewById(R.id.Grid_Game);
        player_id = PLAYER1;
        moveCount = 0;
        playing = true;
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.grid_item, new String[]{"", "", "", "", "", "", "", "", ""});
        grid.setAdapter(adapter);
        if (mode == MODE_OFFLINE) {
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!playing){
                        return;
                    }
                    int x = i % 3;
                    int y = i / 3;

                    if (board[y][x] == 0) {
                        if (player_id == PLAYER1) {
                            ((TextView) view).setText("O");
                        }
                        if (player_id == PLAYER2) {
                            ((TextView) view).setText("X");
                        }

                        board[y][x] = player_id;
                        moveCount++;
                        update(y, x);
                    }
                }
            });
        }
    }

    public void update(int y, int x){
        int s = player_id;
        int n = board.length;
        for(int i = 0; i < n; i++){
            if(board[y][i] != s)
                break;
            if(i == n-1){
                win(false);
                return;
            }
        }

        for(int i = 0; i < n; i++){
            if(board[i][x] != s)
                break;
            if(i == n-1){
                win(false);
                return;
            }
        }

        if(x == y){
            for(int i = 0; i < n; i++){
                if(board[i][i] != s)
                    break;
                if(i == n-1){
                    win(false);
                    return;
                }
            }
        }

        if(x + y == n - 1){
            for(int i = 0; i < n; i++){
                if(board[i][(n-1)-i] != s)
                    break;
                if(i == n-1){
                    win(false);
                    return;
                }
            }
        }

        if(moveCount == (Math.pow(n, 2) - 1)){
            win(true);
        }

        if (player_id == PLAYER1) {
            player_id = PLAYER2;
        }else if (player_id == PLAYER2){
            player_id = PLAYER1;
        }
        ((TextView)findViewById(R.id.Text_Game)).setText("Player " + player_id + "'s Turn");
    }

    public void reset(){
        player_id = PLAYER1;
        ((TextView)findViewById(R.id.Text_Game)).setText("Player " + player_id + "'s Turn");
        board = new int[3][3];
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.grid_item, new String[]{"", "", "", "", "", "", "", "", ""});
        grid.setAdapter(adapter);
    }

    public void win(boolean draw){
        AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
        playing = false;
        if (draw){
            alert.setTitle("Its a draw!");
            alert.setMessage("Game over, its a draw! Click play again or exit.");
        }
        else {
            alert.setTitle("Player " + player_id + " has won!");
            alert.setMessage("Player " + player_id + " has won! Click play again or exit.");

        }
        alert.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reset();
            }
        });
        alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.show();
    }
}