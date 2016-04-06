package com.tejaskoundinya.android.firequiz.waitgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity {

    private RecyclerView mWaitGameRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Firebase mFirebase;
    private List<GameWaitModel> mGameWaitModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebase = new Firebase(Constants.FIREBASE_BASE_URL);
        listenForNewGamesInWait();

        mWaitGameRecyclerView = (RecyclerView) findViewById(R.id.waiting_games_recycler_view);
        mWaitGameRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mWaitGameRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new WaitGameAdapter(mGameWaitModels);
        mWaitGameRecyclerView.setAdapter(mAdapter);

        mWaitGameRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent startGameIntent = new Intent(getApplicationContext(), GameActivity.class);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                GameWaitModel gameWaitModel = mGameWaitModels.get(position);
                gameWaitModel.setGameSlave(getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PREF_USERNAME_KEY, "placeholder"));
                mGameWaitModels.set(position, gameWaitModel);
                mFirebase.child(Constants.FIREBASE_TEST_PATH + Constants.FIREBASE_GAME_WAIT_PATH).setValue(mGameWaitModels);
                startGameIntent.putExtra(Constants.WAIT_GAME_SELECTED_ID_EXTRA_KEY, gson.toJson(mGameWaitModels.get(position), GameWaitModel.class));
                startActivity(startGameIntent);
            }
        }));

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewGameIntent = new Intent(getApplicationContext(), WaitGameActivity.class);
                startActivity(createNewGameIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method listens for games in wait queue
     */
    private void listenForNewGamesInWait() {
        mFirebase.child(Constants.FIREBASE_TEST_PATH + Constants.FIREBASE_GAME_WAIT_PATH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0) {
                    mGameWaitModels = null;
                    mAdapter = new WaitGameAdapter(mGameWaitModels);
                    mWaitGameRecyclerView.setAdapter(mAdapter);
                } else {
                    List<GameWaitModel> gameWaitModels = new ArrayList<GameWaitModel>();
                    GenericTypeIndicator<List<GameWaitModel>> t = new GenericTypeIndicator<List<GameWaitModel>>() {};
                    gameWaitModels = dataSnapshot.getValue(t);
                    mGameWaitModels = gameWaitModels;
                    mAdapter = new WaitGameAdapter(gameWaitModels);
                    mWaitGameRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
