package com.danya140.schedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.danya140.schedule.Logic.DayWeek;
import com.danya140.schedule.Logic.GetShedule;
import com.danya140.schedule.Logic.Info;
import com.danya140.schedule.Logic.Parser;

import org.jsoup.nodes.Document;


/**
 * Created by Данил on 19.02.2016.
 */
public class ScheduleActivity extends AppCompatActivity{

    private Button mDelButton;
    private static TextView mTrest;
    private static WebView mWebView;

    static Parser parser = new Parser();
    static GetShedule gts= new GetShedule();

    public static Document doc;
    protected static Info [][] schedule = new Info[6][6];
    protected static DayWeek dw = new DayWeek();
    static int WEEK=0;
    static String[] days;
    static String[] day = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
    static String[] dayDate = new String[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_schedule);

        //mTrest = (TextView) findViewById(R.id.textView);
        mWebView =(WebView) findViewById(R.id.web);


        getDayDate();
        gts.execute();


        /*mDelButton = (Button)findViewById(R.id.del_schedule_button);
        mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(Constants.FILE);
            }
        });*/
    }


    protected static void getDayDate(){
        for (int i = 0; i < dayDate.length; i++) {
            dayDate[i]=dw.getDate(false,i);
        }
    }

    public static void update(String finaldoc){
        //mTrest.setText(finaldoc);
    }

    public static void parsing(){
        schedule=parser.parse(doc);
        WEEK = parser.getWEEK();
        days = parser.getDays(day, dayDate);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(formHtml(),"text/html","CP-1251");
    }

    private static String formHtml(){
        String table="<!--<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">--><!DOCTYPE html><html><!--html xmlns=\"http://www.w3.org/1999/xhtml\">-->\n<table border=\"1\">\n";
        table+="\n <head>\n" +
                "    <meta content=\"text/html; charset=windows-1251\" http-equiv=\"Content-Type\"/>  </head> ";
        for (int d = 0; d < days.length; d++) {
            if (schedule[d]==null){break;}

            for (int i = 0; i < schedule.length; i++) {
                if(schedule[d][i]==null){continue;}

                table+="<tr>\n";
                if(i==0){table+="<td rowspan=\""+getLength(schedule[d])+"\">"+days[d]+"</td>\n";}

                table+="<td align=\"center\" style=\"font-size: 10px;\">"+schedule[d][i].getTIME()+schedule[d][i].getNUMBER()+"</td>\n";
                table+="<td align=\"center\" style=\"font-size: 10px;\">"+schedule[d][i].getNAME()+"</td>\n";
                table+="<td align=\"center\" style=\"font-size: 10px;\">"+schedule[d][i].getTeacherName()+"</td>\n";
                table+="<td align=\"center\" style=\"font-size: 10px;\">"+schedule[d][i].getCLASSROOM()+"</td>\n";
                table+="</tr>\n";
            }
        }
        table+="</table>\n</html>";
        return table;
    }

    private static int getLength(Info[] schedule){
        int length=0;
        for (int i = 0; i < schedule.length; i++) {
            if(schedule[i]!=null){
                length++;
            }
        }
        return length;
    }

}
