package com.tejaskoundinya.android.firequiz.waitgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.tejaskoundinya.android.firequiz.R;
import com.tejaskoundinya.android.firequiz.gameplay.GameActivity;
import com.tejaskoundinya.android.firequiz.models.GameWaitModel;
import com.tejaskoundinya.android.firequiz.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitGameActivity extends AppCompatActivity {

    private static final String TAG = WaitGameActivity.class.getSimpleName();

    private EditText mGameNameEditText;
    private Button mCreateGameButton;
    private Firebase mFirebase;
    private RelativeLayout mProgressLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_game);
        mProgressLinearLayout = (RelativeLayout)findViewById(R.id.wait_progress_layout);
        mGameNameEditText = (EditText)findViewById(R.id.game_name_edit_text);
        mCreateGameButton = (Button)findViewById(R.id.create_game_button);
        mCreateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGameToWaitQueue(mGameNameEditText.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mGameNameEditText.getWindowToken(), 0);
                setProgressBarIndeterminateVisibility(true);
                mProgressLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addGameToWaitQueue(final String gameName) {
        mFirebase = new Firebase(Constants.FIREBASE_BASE_URL + Constants.FIREBASE_TEST_PATH + Constants.FIREBASE_GAME_WAIT_PATH);
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    List<GameWaitModel> gameWaitModels = new ArrayList<GameWaitModel>();
                    GameWaitModel gameWaitModel = new GameWaitModel();
                    gameWaitModel.setId(System.currentTimeMillis());
                    Random random = new Random();
                    gameWaitModel.setGameMaster(getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PREF_USERNAME_KEY, "master" + random.nextInt()));
                    gameWaitModel.setGameName(gameName);
                    gameWaitModels.add(gameWaitModel);
                    Log.d(TAG, "Setting value: " + gameWaitModels);
                    mFirebase.setValue(gameWaitModels);
                } else {
                    GenericTypeIndicator<List<GameWaitModel>> t = new GenericTypeIndicator<List<GameWaitModel>>() {};
                    List<GameWaitModel> gameWaitModels = dataSnapshot.getValue(t);
                    GameWaitModel gameWaitModel = new GameWaitModel();
                    gameWaitModel.setId(System.currentTimeMillis());
                    Random random = new Random();
                    gameWaitModel.setGameMaster("master" + random.nextInt());
                    gameWaitModel.setGameName(gameName);
                    gameWaitModel.setGameSlave("");
                    gameWaitModels.add(gameWaitModel);
                    Log.d(TAG, "Setting value: " + gameWaitModels);
                    mFirebase.setValue(gameWaitModels);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GameWaitModel gameWaitModel = null;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    gameWaitModel = snapshot.getValue(GameWaitModel.class);
                }
                if(gameWaitModel == null) {
                    return;
                }
                if(gameWaitModel.getGameSlave() == null || gameWaitModel.getGameSlave().isEmpty()) {

                } else {
                    //setProgressBarIndeterminateVisibility(false);
                    Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                    Gson gson = new Gson();
                    startGameIntent.putExtra(Constants.WAIT_GAME_SELECTED_ID_EXTRA_KEY, gson.toJson(gameWaitModel, GameWaitModel.class));
                    startGameIntent.putExtra(Constants.WAIT_GAME_MASTER_KEY, true);
                    startActivity(startGameIntent);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
