package com.danya140.schedule;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.danya140.schedule.Logic.DayWeek;
import com.danya140.schedule.Logic.Info;
import com.danya140.schedule.Logic.Parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Данил on 19.02.2016.
 */
public class ScheduleActivity extends AppCompatActivity{

    private Button mDelButton;
    private static EditText mTrest;
    private static WebView mWebView;

    static Parser parser = new Parser();

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

        getDayDate();
        GetShedule gts = new GetShedule();
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

    public static void parsing(){
        days = parser.getDays(doc,day, dayDate);
        WEEK = parser.getWEEK();
        schedule=parser.parse(doc);
    }

    public void createLayout(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout = createDaysInLayout(layout);

        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        HorizontalScrollView scrollView  = new HorizontalScrollView(this);
        scrollView.addView(layout);

        setContentView(scrollView);
    }

    LinearLayout createDaysInLayout(LinearLayout layout){

        for (int d = 0; d < schedule.length; d++) {
            if (schedule[d]==null){break;}
            LinearLayout daysLayout = new LinearLayout(this);
            daysLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView day = new TextView(this);
            day.setText(days[d]);
            day.setPadding(Constants.PADDING,Constants.PADDING,Constants.PADDING,Constants.PADDING*2);

            daysLayout.addView(day);

            LinearLayout infosLayout = new LinearLayout(this);
            infosLayout.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i <schedule[0].length; i++) {
                if(schedule[d][i]==null){continue;}
                TableRow infoLayout = new TableRow(this);

                TextView time = new TextView(this);
                time.setPadding(Constants.PADDING+5,Constants.PADDING,Constants.PADDING,Constants.PADDING);

                TextView name= new TextView(this);
                name.setPadding(Constants.PADDING,Constants.PADDING,Constants.PADDING,Constants.PADDING);

                TextView teacher = new TextView(this);
                teacher.setPadding(Constants.PADDING,Constants.PADDING,Constants.PADDING,Constants.PADDING);

                TextView classRoom = new TextView(this);
                classRoom.setPadding(Constants.PADDING,Constants.PADDING,Constants.PADDING,Constants.PADDING);

                time.setText(schedule[d][i].getTIME());
                name.setText(schedule[d][i].getNAME());
                teacher.setText(schedule[d][i].getTeacherName());
                classRoom.setText(schedule[d][i].getCLASSROOM());

                infoLayout.addView(time);
                infoLayout.addView(name);
                infoLayout.addView(teacher);
                infoLayout.addView(classRoom);

                infoLayout.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                infosLayout.addView(infoLayout);
            }

            infosLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            daysLayout.addView(infosLayout);
            daysLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            daysLayout.setPadding(Constants.PADDING*2,Constants.PADDING*2,Constants.PADDING*2,Constants.PADDING*2);

            layout.addView(daysLayout);
        }

        return layout;
    }

    class GetShedule extends AsyncTask<Document,Document,Document> {

        //public Document documents;
        public String string;
        public Intent schedule;

        @Override
        protected Document doInBackground(Document... params) {
            try{
                ScheduleActivity.this.doc = getHtml("daniilhacker@mail.ru","199617");

            } catch (IOException ex){

            }

            return doc;
        }

        private Document getHtml(String login,String pass) throws IOException{
            Document doc = Jsoup.connect("http://cabinet.sut.ru/raspisanie")
                    .timeout(0)
                    .cookies(getCookies(login,pass))
                    .referrer("http://www.google.com")
                    .get();
            return doc;
        }

        private Map<String,String> getCookies(String login, String password) throws IOException{
            Connection.Response res = Jsoup.connect("http://cabinet.sut.ru/login")
                    .data("login", login)
                    .data("password", password)
                    .data("submit", "�����")
                    .referrer("http://www.google.com")
                    .method(Connection.Method.POST)
                    .timeout(0)
                    .execute();

            Map<String,String> loginCookies = res.cookies();

            return loginCookies;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            ScheduleActivity.this.parsing();
            ScheduleActivity.this.createLayout();
        }
    }

}
