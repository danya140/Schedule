package com.danya140.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Данил on 19.02.2016.
 */
public class AuthActivity extends AppCompatActivity {

    private Button mAuthButton;
    private Button mDelBuutton;
    private EditText mLoginView;
    private EditText mPassView;
    private TextView mTester;
    private String mLoginString;
    private String mPassString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mLoginView = (EditText) findViewById(R.id.login_edit_text);
        mPassView = (EditText) findViewById(R.id.password_edit_text);
        mTester =(TextView) findViewById(R.id.tester_auth);

        mAuthButton = (Button) findViewById(R.id.auth_button);
        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginString = mLoginView.getText().toString();
                mPassString = mPassView.getText().toString();
                mTester.setText(mLoginString + mPassString);
                AuthActivity.this.saveAuthInf(mLoginString, mPassString);
                Intent schedule = new Intent(AuthActivity.this,ScheduleActivity.class);
                startActivity(schedule);
            }
        });

        mDelBuutton = (Button) findViewById(R.id.del_button);
        mDelBuutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(Constants.FILE);
            }
        });
    }

    protected void saveAuthInf(String mLoginString, String mPassString){

        try {
            FileOutputStream outputStream = openFileOutput(Constants.FILE, Context.MODE_PRIVATE);
            outputStream.write(mLoginString.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.write(mPassString.getBytes());
            outputStream.close();
        } catch (IOException e){
        }
    }

}
