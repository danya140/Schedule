package com.danya140.schedule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danya140.schedule.Logic.GetShedule;
import com.danya140.schedule.Logic.Parser;

import org.jsoup.nodes.Document;


/**
 * Created by Данил on 19.02.2016.
 */
public class ScheduleActivity extends AppCompatActivity{

    private Button mDelButton;
    private static TextView mTrest;
    private String str;


    static Parser parser = new Parser();
    public static Document doc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_schedule);

        mTrest = (TextView) findViewById(R.id.textView);

        GetShedule gts= new GetShedule();
        gts.execute();


        mDelButton = (Button)findViewById(R.id.del_schedule_button);
        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(Constants.FILE);
            }
        });
    }

    public static void update(String finaldoc){
        mTrest.setText(finaldoc);
    }

    public static void parsing(){
        parser.parse(doc);
    }

}
