package com.tejaskoundinya.android.firequiz.gameplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.tejaskoundinya.android.firequiz.R;
import com.tejaskoundinya.android.firequiz.models.GameModel;
import com.tejaskoundinya.android.firequiz.models.GameWaitModel;
import com.tejaskoundinya.android.firequiz.models.OptionModel;
import com.tejaskoundinya.android.firequiz.models.PlayerModel;
import com.tejaskoundinya.android.firequiz.models.QuestionModel;
import com.tejaskoundinya.android.firequiz.utils.Constants;

import static com.tejaskoundinya.android.firequiz.utils.LogUtils.LOGD;
import static com.tejaskoundinya.android.firequiz.utils.LogUtils.makeLogTag;

import java.util.logging.Logger;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(GameActivity.class);

    private GameModel mGameModel;
    private GameWaitModel mGameWaitModel;
    private Firebase mGameFirebase;

    private Button mOption1Button;
    private Button mOption2Button;
    private Button mOption3Button;
    private Button mOption4Button;

    private TextView mQuestionTextView;

    private TextView mMyScoreTextView;
    private TextView mOpponentScoreTextView;
    private TextView mGameNameTextView;

    private boolean mIsGameMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mOption1Button = (Button)findViewById(R.id.game_option1_button);
        mOption2Button = (Button)findViewById(R.id.game_option2_button);
        mOption3Button = (Button)findViewById(R.id.game_option3_button);
        mOption4Button = (Button)findViewById(R.id.game_option4_button);
        mQuestionTextView = (TextView)findViewById(R.id.game_question_textView);
        mMyScoreTextView = (TextView)findViewById(R.id.game_myScore_textView);
        mOpponentScoreTextView = (TextView)findViewById(R.id.game_opponentScore_textView);
        mGameNameTextView = (TextView)findViewById(R.id.game_gameName_textView);
        Gson gson = new Gson();
        mGameWaitModel = gson.fromJson(getIntent().getStringExtra(Constants.WAIT_GAME_SELECTED_ID_EXTRA_KEY), GameWaitModel.class);
        mIsGameMaster = getIntent().getBooleanExtra(Constants.WAIT_GAME_MASTER_KEY, false);
        if(isGameMaster()) {
            LOGD(TAG, "Initialize Game Model");
            initializeGameModel();
        }
        LOGD(TAG, "Initialize Firebase instance");
        initializeFirebase();
        setAnswerSelectionListener();
    }

    /**
     * Helper method that returns whether the player is the Game Master
     * @return
     */
    private boolean isGameMaster() {
        return mIsGameMaster;
    }

    /**
     * Method that initializes the GameModel
     * It makes the player the Game Master and sets config variables
     */
    private void initializeGameModel() {
        mGameModel = new GameModel();
        mGameModel.setId(mGameWaitModel.getId());
        mGameModel.setGameMaster(getPlayerModel(true));
        mGameModel.setGameSlave(getPlayerModel(false));
        mGameModel.setGenerateNewQuestion(true);
        mGameModel.setDidGameEnd(false);
        LOGD(TAG, "Game Model initialized");
    }

    /**
     * Method that returns the PlayerModel of the Game Master or Slave
     * @param isMaster Boolean value that signifies whther the player is a master of a slave
     * @return
     */
    private PlayerModel getPlayerModel(boolean isMaster) {
        PlayerModel playerModel = new PlayerModel();
        if(isMaster) {
            playerModel.setName(mGameWaitModel.getGameMaster());
        } else {
            playerModel.setName(mGameWaitModel.getGameSlave());
        }
        playerModel.setScore(0);
        return playerModel;
    }

    /**
     * Method that initializes the Firebase for the game to start
     */
    private void initializeFirebase() {
        mGameFirebase = new Firebase(Constants.FIREBASE_BASE_URL + Constants.FIREBASE_TEST_PATH + Constants.FIREBASE_GAME_ACTIVE_PATH + mGameWaitModel.getId());
        mGameFirebase.setValue(mGameModel);
        mGameFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGameModel = dataSnapshot.getValue(GameModel.class);
                if (mGameModel == null) {
                    return;
                }
                try {
                    if (mGameModel.isGenerateNewQuestion()) {
                        mGameModel.getGameMaster().setDidPlayerAnswer(false);
                        mGameModel.getGameMaster().setPlayerAnswerCorrect(false);
                        mGameModel.getGameSlave().setDidPlayerAnswer(false);
                        mGameModel.getGameSlave().setPlayerAnswerCorrect(false);
                    }
                    if (isGameMaster() && mGameModel.isGenerateNewQuestion()) {
                        generateNewQuestion();
                        mGameModel.setGenerateNewQuestion(false);
                        mGameFirebase.setValue(mGameModel);
                        return;
                    }
                    mQuestionTextView.setText(mGameModel.getQuestion().getQuestion());
                    mOption1Button.setText(mGameModel.getQuestion().getOptions().get(0).getOptionString());
                    mOption2Button.setText(mGameModel.getQuestion().getOptions().get(1).getOptionString());
                    mOption3Button.setText(mGameModel.getQuestion().getOptions().get(2).getOptionString());
                    mOption4Button.setText(mGameModel.getQuestion().getOptions().get(3).getOptionString());
                    mGameNameTextView.setText(mGameWaitModel.getGameName());
                    if (isGameMaster()) {
                        mMyScoreTextView.setText(String.valueOf(mGameModel.getGameMaster().getScore()));
                        mOpponentScoreTextView.setText(String.valueOf(mGameModel.getGameSlave().getScore()));
                        if (mGameModel.getGameMaster().isDidPlayerAnswer() && mGameModel.getGameSlave().isDidPlayerAnswer()) {
                            mGameModel.setGenerateNewQuestion(true);
                            mGameFirebase.child("generateNewQuestion").setValue(mGameModel.isGenerateNewQuestion());
                        }
                    } else {
                        mMyScoreTextView.setText(String.valueOf(mGameModel.getGameSlave().getScore()));
                        mOpponentScoreTextView.setText(String.valueOf(mGameModel.getGameMaster().getScore()));
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mGameFirebase.child("question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Helper method that generates a new question without repeating the previous question
     */
    private void generateNewQuestion() {
        GameManager gameManager = new GameManager();
        long previousQuestionId = -1;
        try {
            previousQuestionId = mGameModel.getQuestion().getId();
        } catch (NullPointerException e) {
            previousQuestionId = -1;
        }
        mGameModel.setQuestion(gameManager.generateRandomQuestion(previousQuestionId));
        mGameModel.getGameMaster().setDidPlayerAnswer(false);
        mGameModel.getGameMaster().setPlayerAnswerCorrect(false);
        mGameModel.getGameSlave().setDidPlayerAnswer(false);
        mGameModel.getGameSlave().setPlayerAnswerCorrect(false);
        LOGD(TAG, "New question generated");
    }

    /**
     * Method to set on click listeners for all option buttons
     * Makes answer selection for the user
     */
    private void setAnswerSelectionListener() {

        LOGD(TAG, "Setting up onclick listeners for answer option buttons");
        mOption1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Option 1 clicked");
                if(hasAlreadyAnswered()) {
                    return;
                }
                if(mGameModel == null) {
                    return;
                }
                if(isGameMaster()) {
                    mGameModel.getGameMaster().setDidPlayerAnswer(true);
                    mGameModel.getGameMaster().setPlayerAnswerCorrect(hasAnswereredCorrectly(1));
                    mGameFirebase.child("gameMaster").setValue(mGameModel.getGameMaster());
                } else {
                    mGameModel.getGameSlave().setDidPlayerAnswer(true);
                    mGameModel.getGameSlave().setPlayerAnswerCorrect(hasAnswereredCorrectly(1));
                    mGameFirebase.child("gameSlave").setValue(mGameModel.getGameSlave());
                }
            }
        });

        mOption2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Option 2 clicked");
                if(hasAlreadyAnswered()) {
                    return;
                }
                if(mGameModel == null) {
                    return;
                }
                if(isGameMaster()) {
                    mGameModel.getGameMaster().setDidPlayerAnswer(true);
                    mGameModel.getGameMaster().setPlayerAnswerCorrect(hasAnswereredCorrectly(2));
                    mGameFirebase.child("gameMaster").setValue(mGameModel.getGameMaster());
                } else {
                    mGameModel.getGameSlave().setDidPlayerAnswer(true);
                    mGameModel.getGameSlave().setPlayerAnswerCorrect(hasAnswereredCorrectly(2));
                    mGameFirebase.child("gameSlave").setValue(mGameModel.getGameSlave());
                }
            }
        });

        mOption3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Option 3 clicked");
                if(hasAlreadyAnswered()) {
                    return;
                }
                if(mGameModel == null) {
                    return;
                }
                if(isGameMaster()) {
                    mGameModel.getGameMaster().setDidPlayerAnswer(true);
                    mGameModel.getGameMaster().setPlayerAnswerCorrect(hasAnswereredCorrectly(3));
                    mGameFirebase.child("gameMaster").setValue(mGameModel.getGameMaster());
                } else {
                    mGameModel.getGameSlave().setDidPlayerAnswer(true);
                    mGameModel.getGameSlave().setPlayerAnswerCorrect(hasAnswereredCorrectly(3));
                    mGameFirebase.child("gameSlave").setValue(mGameModel.getGameSlave());
                }
            }
        });

        mOption4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGD(TAG, "Option 4 clicked");
                if(hasAlreadyAnswered()) {
                    return;
                }
                if(mGameModel == null) {
                    return;
                }
                if(isGameMaster()) {
                    mGameModel.getGameMaster().setDidPlayerAnswer(true);
                    mGameModel.getGameMaster().setPlayerAnswerCorrect(hasAnswereredCorrectly(4));
                    mGameFirebase.child("gameMaster").setValue(mGameModel.getGameMaster());
                } else {
                    mGameModel.getGameSlave().setDidPlayerAnswer(true);
                    mGameModel.getGameSlave().setPlayerAnswerCorrect(hasAnswereredCorrectly(4));
                    mGameFirebase.child("gameSlave").setValue(mGameModel.getGameSlave());
                }
            }
        });

    }

    /**
     * Method that checks whether the answer is correct
     * @param optionId The option ID of the selected option
     * @return True if the answer is right. False, otherwise.
     */
    private boolean hasAnswereredCorrectly(int optionId) {
        boolean answeredCorrectly = false;
        QuestionModel questionModel = mGameModel.getQuestion();
        for(OptionModel option : questionModel.getOptions()) {
            if(option.getOptionId() == optionId) {
                answeredCorrectly = option.isCorrectAnswer();
                break;
            }
        }
        if(answeredCorrectly) {
            Toast.makeText(getApplicationContext(), "Correct Answer!", Toast.LENGTH_LONG).show();
            if(isGameMaster()) {
                mGameModel.getGameMaster().setScore(mGameModel.getGameMaster().getScore() + 1);
            } else {
                mGameModel.getGameSlave().setScore(mGameModel.getGameSlave().getScore() + 1);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Answer!", Toast.LENGTH_LONG).show();
        }
        return answeredCorrectly;
    }

    /**
     * Method that tells whether the question is already answered by the player
     * @return True if already answered, false otherwise.
     */
    private boolean hasAlreadyAnswered() {
        boolean hasAnswered = false;
        if(isGameMaster()) {
            try {
                if(mGameModel.getGameMaster().isDidPlayerAnswer()) {
                    hasAnswered = true;
                }
            } catch (NullPointerException e) {

            }
        } else {
            try {
                if(mGameModel.getGameSlave().isDidPlayerAnswer()) {
                    hasAnswered = true;
                }
            } catch (NullPointerException e) {

            }
        }
        return hasAnswered;
    }

}
