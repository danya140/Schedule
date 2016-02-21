/*
package com.danya140.schedule.Logic;

import android.content.Intent;
import android.os.AsyncTask;

import com.danya140.schedule.ScheduleActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

*/
/**
 * Created by Данил on 20.02.2016.
 *//*

public class GetShedule extends AsyncTask<Document,Document,Document> {

    public Document documents;
    public String string;
    public Intent schedule;

    @Override
    protected Document doInBackground(Document... params) {
        Document doc=null;
        try{
            ScheduleActivity.doc = getHtml("daniilhacker@mail.ru","199617");

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
        ScheduleActivity.doc = document;
    }
}
*/
