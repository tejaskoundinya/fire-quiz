package com.tejaskoundinya.android.firequiz.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tejaskoundinya.android.firequiz.R;
import com.tejaskoundinya.android.firequiz.utils.Constants;
import com.tejaskoundinya.android.firequiz.waitgame.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameEditText = (EditText)findViewById(R.id.username_editText);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserNameToSharedPreference();
                Intent showGameListIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(showGameListIntent);
            }
        });
    }

    private void saveUserNameToSharedPreference() {
        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE).edit();
        sharedPreferencesEditor.putString(Constants.PREF_USERNAME_KEY, mUsernameEditText.getText().toString());
        sharedPreferencesEditor.commit();
    }
}
