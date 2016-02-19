package com.danya140.schedule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Данил on 19.02.2016.
 */
public class ScheduleActivity extends AppCompatActivity{

    private Button mDelButton;
    private TextView mTrest;
    private String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_schedule);

        mTrest = (TextView) findViewById(R.id.textView);

        /*Thread downloadThread = new Thread(){
            public void run(){
                try {
                    mTrest.setText(Jsoup.connect("http://cabinet.sut.ru/").get().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };*/


        class Task extends AsyncTask<Void,Void,Void>{
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    str = Jsoup.connect("http://cabinet.sut.ru/").get().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ScheduleActivity.this.update(str);
            }
        }

        Task mt =new Task();
        mt.execute();

        mDelButton = (Button)findViewById(R.id.del_schedule_button);
        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(Constants.FILE);
            }
        });
    }

    public void update(String text){
        mTrest.setText(text);
    }
}
