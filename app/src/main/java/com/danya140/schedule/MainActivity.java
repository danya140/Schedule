package com.danya140.schedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkFile()){
            Intent schedule = new Intent(this,ScheduleActivity.class);
            startActivity(schedule);
        } else {
            Intent login = new Intent(this,AuthActivity.class);
            startActivity(login);
        }
    }

    private boolean checkFile(){
        File auth= new File(Constants.FILE);
        return auth.exists();
    }
}
