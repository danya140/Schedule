package com.danya140.schedule;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


/**
 * Created by Данил on 19.02.2016.
 */
public class ScheduleActivity extends AppCompatActivity{

    protected static Parser parser = new Parser();
    protected static Info [][] schedule = new Info[6][6];
    protected static DayWeek dw = new DayWeek();
    protected static int WEEK=0;
    protected static String[] days;
    protected static String[] day = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
    protected static String[] dayDate = new String[6];
    public static Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    openFileInput(Constants.INFO_FILE)));

            if(dw.isMonday()){
                getDayDate();
                GetShedule gts = new GetShedule();
                gts.execute();
            }
            readInfo();
            createLayout();

        }catch (FileNotFoundException ex){
            getDayDate();
            GetShedule gts = new GetShedule();
            gts.execute();
        }

        /*getDayDate();
        GetShedule gts = new GetShedule();
        gts.execute();*/
    }


    protected static void getDayDate(){
        for (int i = 0; i < dayDate.length; i++) {
            dayDate[i]=dw.getDate(false,i);
        }
    }

    public static void parsing(){
        days = parser.getDays(doc, day, dayDate);
        schedule=parser.parse(doc);
        WEEK = parser.getWEEK();
    }

    public void createLayout(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView week = new TextView(this);
        week.setText("Неделя № "+WEEK);
        layout.addView(week);
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
            daysLayout.setMinimumHeight(Constants.MIN_DAYS_HEIGHT);
            daysLayout.setPadding(0, 0, 0, Constants.PADDING * 10);

            TextView day = new TextView(this);
            day.setText(days[d]);
            day.setPadding(Constants.PADDING, Constants.PADDING, Constants.PADDING, Constants.PADDING * 2);
            day.setLayoutParams(new ViewGroup.LayoutParams(Constants.DAY_WIDTH, ViewGroup.LayoutParams.FILL_PARENT));
            day.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            daysLayout.addView(day);

            LinearLayout infosLayout = new LinearLayout(this);
            infosLayout.setOrientation(LinearLayout.VERTICAL);
            infosLayout.setMinimumHeight(Constants.MIN_DAYS_HEIGHT);
            infosLayout.setGravity(Gravity.CENTER_VERTICAL);

            for (int i = 0; i <schedule[0].length; i++) {
                if(schedule[d][i]==null){continue;}
                TableRow infoLayout = new TableRow(this);
                infoLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));



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

        @Override
        protected Document doInBackground(Document... params) {
            try{

                ScheduleActivity.this.doc = getHtml(readAuthLogin(),readAuthPass());

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
            if(ScheduleActivity.doc==null){
                connectionErr();
            } else{
                ScheduleActivity.this.parsing();
                ScheduleActivity.this.createLayout();
            }

        }
    }

    protected void connectionErr(){
        TextView errText = new TextView(this);
        errText.setText("Нет подключения к интернету");
        errText.setTextSize(30f);
        setContentView(errText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(doc!=null){
            saveInfo();
        }
    }

    //File Worker

    protected String readAuthLogin(){
        String login = "";
        try{
            InputStream inputStream = openFileInput(Constants.FILE);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            login=reader.readLine();
        } catch (IOException e){

        }

        return login;
    }
    protected String readAuthPass(){
        String pass = "";
        try{
            InputStream inputStream = openFileInput(Constants.FILE);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            reader.readLine();
            pass=reader.readLine();
        } catch (IOException e){

        }

        return pass;
    }

    private void saveInfo(){
        try {
            FileOutputStream writer = openFileOutput(Constants.INFO_FILE, Context.MODE_PRIVATE);

            writer.write(String.valueOf(WEEK).getBytes());
            writer.write("\n".getBytes());

            for (int d = 0; d < days.length; d++) {
                writer.write(days[d].replaceAll("\n"," ").getBytes());
                writer.write("!\n".getBytes());
                if (schedule[d]==null){d++;}

                for (int i = 0; i <schedule[0].length; i++) {
                    if(schedule[d][i]==null){continue;}
                    writer.write(schedule[d][i].saveString().getBytes());
                }
            }
            writer.close();

        } catch (IOException e){
        }
    }

    private void readInfo(){
        days = new String[6];
        schedule = new Info[6][6];
        try{
            InputStream inputStream = openFileInput(Constants.INFO_FILE);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String line;
            int d = -1;
            int i = 0;

            WEEK = Integer.parseInt(reader.readLine().trim());

            while ((line=reader.readLine()) !=null){
                if(line.contains("!")){
                    //parse for days
                    d++;
                    i=0;
                    days[d]=line.substring(0,line.indexOf('!'));
                } else {
                    //parse for info
                    parseSavedInfo(line,d,i);
                    i++;
                }
            }

        } catch (IOException e){

        }
    }

    private void parseSavedInfo(String line, int d, int i){
        int idx;

        idx = line.indexOf(";");
        String time = line.substring(0,idx);
        line = line.substring(idx+1, line.length());

        idx = line.indexOf(";");
        String name = line.substring(0, idx);
        line = line.substring(idx+1, line.length());

        idx = line.indexOf(";");
        String tName = line.substring(0, idx);
        line = line.substring(idx+1, line.length());

        schedule[d][i]=new Info(time,name,tName,line);
    }
}
