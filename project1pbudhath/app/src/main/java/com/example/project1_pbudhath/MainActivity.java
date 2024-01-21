package com.example.project1_pbudhath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private GridLayout word_grid;
    private GridLayout letter_grid;

    private int position;
    private int row;
    
    private boolean complete;

    private int RED = Color.parseColor("#800020");
    private int YELLOW = Color.parseColor("#FFBF00");
    private int GREEN = Color.parseColor("#097969");


    private String word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        word_grid = findViewById(R.id.word_grid);
        letter_grid = findViewById(R.id.button_grid);
        word = (new Words()).get_word();
        position = 0;
        row = 0;
        complete = false;
    }

    public void letterClick(View v){
        if(complete){
            return;
        }
        if (v.getId() == R.id.del_button){
            decrement();
            updateText("");
            return;
        }
        Button clicked = (Button)v;
        updateText(clicked.getText().toString());
        increment(false);
    }

    public void onDone(View v){
        if(complete){
            return;
        }
        if(position % 5 != 4){
            Toast.makeText(this, "Please enter a 5 letter word.", Toast.LENGTH_SHORT);
            return;
        }

        int correct_count = 0;

        for (int i = position - 4; i <= position; i++){
            TextView tv = ((TextView)word_grid.getChildAt(i));
            String letter = tv.getText().toString();
            if(word.contains(letter)){
                if(word.indexOf(letter) == (i % 5)){
                    tv.setTextColor(GREEN);
                    // Get correct button from numerical value of letter as index
                    letter_grid.getChildAt(((int)((letter).charAt(0))) - 65)
                            .setBackgroundColor(GREEN);
                    correct_count++;
                }
                else{
                    tv.setTextColor(YELLOW);
                    letter_grid.getChildAt(((int)((letter).charAt(0))) - 65)
                            .setBackgroundColor(YELLOW);
                }
            }
            else{
                tv.setTextColor(RED);
                letter_grid.getChildAt(((int)((letter).charAt(0))) - 65)
                        .setBackgroundColor(RED);
            }
        }

        if(correct_count == 5){
            endGame(true);
        }
        increment(true);
        if(position >= (7*5)){
            endGame(false);
        }
    }

    private void endGame(boolean win){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        if(win){
            dialog.setTitle("You Win!")
                    .setIcon(R.drawable.ic_launcher_background)
                    .setMessage("Play Again?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            restartGame();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            complete = true;
                            dialoginterface.cancel();
                        }
                    }).show();
        }

        else{

        }
    }

    private void restartGame(){
        word = (new Words()).get_word();
        position = 0;
        row = 0;
        complete = false;
        for(int i = 0; i < 30; i++){
            TextView tv = ((TextView)word_grid.getChildAt(i));
            tv.setText("_");
            tv.setTextColor(Color.BLACK);
        }
        for(int i = 0; i < 26; i++){
            Button button = ((Button)letter_grid.getChildAt(i));
            button.setBackgroundColor(Color.parseColor("#454545"));
        }

    }

    private void updateText(String s){
        if(s.equals("")){
            ((TextView)word_grid.getChildAt(position)).setText("_");
            ((TextView)word_grid.getChildAt(position)).setTextColor(Color.BLACK);
        }
        else{
            ((TextView)word_grid.getChildAt(position)).setText(s);
        }
    }

    private void increment(boolean nextLine){
        if(nextLine){
            row++;
            position = (row * 5);
            return;
        }

        if((position) % 5 != 4){
            position++;
        }
    }

    private void decrement(){
        if (position % 5 != 0){
            position--;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help_item){
            helpAlertView("How to play Wordle");
        }
        if (item.getItemId() == R.id.restart_item){
            restartGame();
        }
        return super.onOptionsItemSelected(item);
    }

    private void helpAlertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle( message )
                .setMessage("6 chances to guess a 5 letter word. If letter guessed is in the word," +
                        "the letter is turned yellow. If letter does not exist, it is turned red." +
                        "If the letter is in the word and in the right place of the word, the letter is green.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                }).show();
    }
}