package com.danya140.schedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Данил on 19.02.2016.
 */
public class AuthActivity extends AppCompatActivity {

    private Button mAuthButton;
    private EditText mLoginView;
    private EditText mPassView;
    private String mLoginString;
    private String mPassString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mLoginView = (EditText) findViewById(R.id.login_edit_text);
        mPassView = (EditText) findViewById(R.id.password_edit_text);

        mAuthButton = (Button) findViewById(R.id.auth_button);
        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginString = mLoginView.getText();
            }
        });
    }
}
