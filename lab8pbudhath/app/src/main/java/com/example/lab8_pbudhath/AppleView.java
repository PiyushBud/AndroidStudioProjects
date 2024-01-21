package com.example.lab8_pbudhath;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Random;

public class AppleView extends TileView {



    private static final String TAG = "AppleView";

    private Coordinate spot;


    /**
     * Current mode of application: READY to run, RUNNING, or you have already
     * lost. static final ints are used instead of an enum for performance
     * reasons.
     */
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int DONE = 3;



    /**
     * Current direction the snake is headed.
     */


    int WIDTH ;
    int HEIGHT ;
    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    private static final int RED_STAR = 1;

    private static final int GREEN_STAR = 3;

    /**
     * mScore: used to track the number of apples captured mMoveDelay: number of
     * milliseconds between snake movements. This will decrease as apples are
     * captured.
     */
    private long mScore = 0;     // score
    private long mMiss = 0;
    private long mMoveDelay;     // time between updates

    /**
     * mLastMove: tracks the absolute time when the snake last moved, and is used
     * to determine if a move should be made based on mMoveDelay.
     */
    private long mLastMove;

    /**
     * mStatusText: text shows to the user in some run states
     */
    private TextView mStatusText;

    /**
     * mSnakeTrail: a list of Coordinates that make up the snake's body
     * mAppleList: the secret location of the juicy apples the snake craves.
     */
    private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();

    /**
     * Everyone needs a little randomness in their life
     */
    private static final Random RNG = new Random();

    /**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     */
    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            AppleView.this.update();
            AppleView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };


    /**
     * Constructs a SnakeView based on inflation from XML
     *
     * @param context
     * @param attrs
     */
    public AppleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView();

    }

    public AppleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView();
    }

    private void initSnakeView() {
        setFocusable(true);

        Resources r = this.getContext().getResources();

        resetTiles(4);
        loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));

        loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));

    }


    private void initNewGame() {

        mAppleList.clear();

        // Four apples to start with
        addRandomApple();
        addRandomApple();
        addRandomApple();
        addRandomApple();

        mScore = 0;
        mMiss = 0;

        mMoveDelay = 2000;    // this controls time between updates

    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();
        WIDTH = this.getWidth();
        HEIGHT = this.getHeight();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (mMode == READY | mMode == DONE) {
                    /*
                     * At the beginning of the game, or the end of a previous one,
                     * we should start a new game.
                     */
                    initNewGame();
                    setMode(RUNNING);
                    update();
                    return (true);
                }else {
                    float x = event.getX();
                    float y = event.getY();
                    spot = new Coordinate((int)(x/mTileSize),(int)(y/mTileSize));
                    if (hits_apple(spot)) updateScore(true);
                    else updateScore(false);
                }
                break;
            }
        }
        invalidate();

        return true;
    }

    private Boolean hits_apple(Coordinate spot) {
        for (Coordinate c : mAppleList) {
            if (c.close_enough(spot)) return true;
        }
        return false;
    }

    private void updateScore(boolean hit){
        if (hit) {
            mScore++;
            mMoveDelay -= 300;
            if (mMoveDelay < 300){
                mMoveDelay = 300;
            }
        }
        else {
            mMiss++;
        }

        mStatusText.setText("\tScore = " + mScore + "\t Misses = " + mMiss);

        if (mMiss >= 3){
            setMode(DONE);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle( "Your Run is Over!" )
                    .setIcon(R.drawable.ic_launcher_background)
                    .setMessage("You got a score of " + mScore + "!")
                    .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            initNewGame();
                            setMode(RUNNING);
                            update();
                        }
                    }).show();
        }
    }





    /**
     * Sets the TextView that will be used to give information (such as "Game
     * Over" to the user.
     *
     * @param newView
     */
    public void setTextView(TextView newView) {
        mStatusText = newView;
    }

    /**
     * Updates the current mode of the application (RUNNING or PAUSED or the like)
     * as well as sets the visibility of textview for notification
     *
     * @param newMode
     */
    public void setMode(int newMode) {
        int oldMode = mMode;
        mMode = newMode;

        if (newMode == RUNNING & oldMode != RUNNING) {
            mStatusText.setText("\tScore = "+mScore+"\tMiss= "+mMiss);
            update();
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
        if (newMode == PAUSE) {
            str = res.getText(R.string.mode_pause);
        }
        if (newMode == READY) {
            str = res.getText(R.string.mode_ready);
        }
        if (newMode == DONE) {
            str = res.getString(R.string.mode_lose_prefix) + mScore
                    + res.getString(R.string.mode_lose_suffix);
        }

        mStatusText.setText(str);
        mStatusText.setVisibility(View.VISIBLE);
    }

    /**
     * Selects a random location within the garden that is not currently covered
     * by the snake. Currently _could_ go into an infinite loop if the snake
     * currently fills the garden, but we'll leave discovery of this prize to a
     * truly excellent snake-player.
     *
     */
    private void addRandomApple() {
        Coordinate newCoord = null;

        int newX = 1 + RNG.nextInt(mXTileCount - 2);
        int newY = 1 + RNG.nextInt(mYTileCount - 2);
        newCoord = new Coordinate(newX, newY);
        if (newCoord == null) {
            Log.e(TAG, "Somehow ended up with a null newCoord!");
        }
        mAppleList.add(newCoord);
    }


    /**
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's location.
     */
    public void update() {
        if (mMode == RUNNING) {
            long now = System.currentTimeMillis();
                if (now - mLastMove > mMoveDelay) {
                clearTiles();
                updateWalls();
                mAppleList.clear();
                addRandomApple(); addRandomApple(); addRandomApple(); addRandomApple();
                updateApples();
                mLastMove = now;
            }
            mRedrawHandler.sleep(mMoveDelay);
        }

    }

    /**
     * Draws some walls.
     *
     */
    private void updateWalls() {
        for (int x = 0; x < mXTileCount; x++) {
            setTile(GREEN_STAR, x, 0);
            setTile(GREEN_STAR, x, mYTileCount - 1);
        }
        for (int y = 1; y < mYTileCount - 1; y++) {
            setTile(GREEN_STAR, 0, y);
            setTile(GREEN_STAR, mXTileCount - 1, y);
        }
    }

    /**
     * Draws some apples.
     *
     */
    private void updateApples() {
        for (Coordinate c : mAppleList) {
            setTile(RED_STAR, c.x, c.y);
        }
    }


    /**
     * Simple class containing two integer values and a comparison function.
     * There's probably something I should use instead, but this was quick and
     * easy to build.
     *
     */
    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int newX, int newY) {
            x = newX;
            y = newY;
        }

        public boolean equals(Coordinate other) {
            if (x == other.x && y == other.y) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Coordinate: [" + x + "," + y + "]";
        }

        public boolean close_enough(Coordinate spot) {
            int diffx = Math.abs(spot.x - this.x);
            int diffy = Math.abs(spot.y - this.y);
            if (diffx < 3 && diffy < 3){
                return true;
            }
            return false;
        }
    }

}

