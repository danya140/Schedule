package com.danya140.schedule.Logic;


import com.danya140.schedule.ScheduleActivity;

import org.jsoup.nodes.Document;

import java.util.Scanner;

/**
 * Created by Данил on 20.02.2016.
 */
public class Parser {

    private static String document;
    private static String finaldoc="";
    private static int DAY =0;
    private static Info[][] schedule = new Info[6][6];
    private static int WEEK=0;

    public static Info[][] parse(Document doc){

        renew();

        document = doc.outerHtml();

        document=getFrame(document);

        WEEK = getWeek(document);

        document=getFrame(document);

        getFinalDoc();

        parseForDays();

        return schedule;
    }

    protected static String getFrame(String document){
        int startIndex=document.indexOf('Н')-10;
        document=document.substring(startIndex);
        return document;
    }

    protected static int getWeek(String document){
        int week;
        String str;
        Scanner scnr = new Scanner(document);
        str=scnr.nextLine();
        str=str.substring(str.indexOf('№') + 1, str.indexOf('№') + 3);
        week = Integer.parseInt(str.trim());
        return week;
    }

    protected static void clean(){
        document=document.replaceAll("[a-zA-Z]*\"","");// del all words starting with = and end with "
        document=document.replaceAll("(style=font-size: 12px;|align|width|small|style|\\|)","");
        document=document.replaceAll("href+=*+\\w*+\\?+=?+\\w*","");
        document=document.replaceAll("(100|= =|\\s+=+\\s)","");
        document=document.replaceAll("(\\s|\\w)+<(/|)+[bpdhas]+(\\w*|\\s*)+>","");
        document=document.replaceAll("<b>|</b>", "");
        document=document.replaceAll("=>|\\s+=+>", ">");
        document=document.replaceAll("<a =\\w*>$*\\s*", "");
        document=document.replaceAll("table|tbody", "");
        document=document.replaceAll("rowspan", "");
    }

    protected static void getFinalDoc(){

        clean();


        int endIndex=document.lastIndexOf('%');
        int startIndex=document.indexOf('%');
        document=document.substring(startIndex, endIndex);

        Scanner scnr = new Scanner(document);
        String str;

        while (scnr.hasNext()){
            str=scnr.nextLine();
            findInString(str);
        }
    }

    protected static void findInString(String str){
        str=str.replaceAll("\\s*+<","");
        str=str.replaceAll("t|>","");
        str=str.replaceAll("//","");
        str=str.replaceAll("/+(d|r)","");
        str=str.replaceAll("/", "");
        str=str.replaceAll("r =background: #FF9933 !imporan","");
        str=str.replaceAll("r", "");
        str=str.trim();

        if(!str.isEmpty()){
            if (str.charAt(0)=='d'){
                finaldoc+=str+"\n";
            }
        }
    }

    protected static void parseForDays(){
        Scanner scnr = new Scanner(finaldoc);

        String str="";

        String leso;
        int les;
        while (scnr.hasNext()){
            leso = String.valueOf(scnr.nextLine().charAt(3));
            if (!leso.trim().isEmpty()){
                les = Integer.parseInt(leso);
                for (int lessons = les*4; lessons >0; lessons--) {
                    str+=scnr.nextLine().trim()+"\n";
                }
                parseForInfo(str,les);
                str="";
                DAY++;
            }
        }
    }

    protected static void parseForInfo(String str, int numLessons){
        str=str.replaceAll("d","");
        Scanner scnr = new Scanner(str);

        for (int i=0; i<numLessons;i++){
            String time=scnr.nextLine();
            String name = scnr.nextLine();
            String teacher = scnr.nextLine();
            String classroom = scnr.nextLine();
            schedule[DAY][i]= new Info(time,name,teacher,classroom);
        }
    }

    protected static void renew(){
        DAY=0;
        document = "";
        finaldoc = "";
        schedule = new Info[6][6];
    }

    public static int getWEEK() {
        return WEEK;
    }

    public static String[][] getDays(Document doc, String[] day, String[] dayDate){
        document = doc.outerHtml();
        String[][] days = new String[2][6];
        int c = 0;

        for (int i = 0; i < day.length; i++) {
            if (document.contains(day[i])){
                days[0][c]=day[i];
                days[1][c]=dayDate[i];//+" \n"+dayDate[i]
                c++;
            }
        }
        return days;
    }

    public static int nextWeek(Document doc){
        int week;
        String tmp;
        tmp= String.valueOf(doc.outerHtml().charAt(doc.outerHtml().indexOf("?") + 6))+String.valueOf(doc.outerHtml().charAt(doc.outerHtml().indexOf("?") + 7));
        week = Integer.parseInt(tmp);

        return week;
    }
}
