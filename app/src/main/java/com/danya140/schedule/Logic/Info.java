package com.danya140.schedule.Logic;

/**
 * Created by Данил on 20.02.2016.
 */
public class Info {
    private String TIME;
    private String NAME;
    private String TEACHER_NAME;
    private String CLASSROOM;
    private String NUMBER;

    public Info(){
        TIME = "";
        NAME = "Name";
        TEACHER_NAME = "Teacher name";
        CLASSROOM = "0/0";
        NUMBER="";
    }

    public Info(String time,String name,String teacher,String classroom){
        TIME = cleanTime(time.trim());
        NAME = name;
        TEACHER_NAME = cleanTeacherName(teacher);
        CLASSROOM =normalizeRoom(classroom);
        NUMBER="";
    }

    public void setTIME(String time){
        TIME=time;
    }

    public void setNAME(String name){
        NAME=name;
    }

    public void setTeacherName(String teacherName){
        TEACHER_NAME=teacherName;
    }

    public void setCLASSROOM(String classroom){
        CLASSROOM=classroom;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getTIME() {
        return TIME;
    }

    public String getNAME() {
        return NAME;
    }

    public String getTeacherName() {
        return TEACHER_NAME;
    }

    public String getCLASSROOM() {
        return CLASSROOM;
    }

    public String getNUMBER() {
        findNumber();
        return NUMBER;
    }

    private String normalizeRoom(String classroom){
        String str="";

        if(!classroom.isEmpty() && !classroom.contains("/")){
            char[] ch = classroom.toCharArray();

            char corp = ch[ch.length-1];
            ch[ch.length-1]='/';

            for (char aCh : ch) {
                str += aCh;
            }
            str+=corp;
            return str;
        } else {return classroom;}

    }

    private void findNumber(){
        NUMBER= String.valueOf(TIME.charAt(0));
        TIME=TIME.replaceFirst("\\d+\\s", "");
    }

    private String cleanTime(String time){
        time=time.replaceAll("\\(+\\w*+:+\\w*+-+\\w*+:+\\w*+\\)+.", "");
        return time;
    }

    private String cleanTeacherName(String teacher){
        if(teacher.contains("("))
        return teacher.substring(0,teacher.indexOf("("));
        else return teacher;
    }

    public void abbrev(){
        if(NAME.length()>30){
            String tmp ="";
            tmp+=NAME.charAt(0);
            while(NAME.contains(" ") && (NAME.charAt(NAME.indexOf(" ")+1)!='(')){
                tmp+=NAME.charAt(NAME.indexOf(" ")+1);
                NAME=NAME.substring(NAME.indexOf(" ")+1);
            }
            String type=NAME.substring(NAME.indexOf("("),NAME.indexOf(")")+1);
            NAME = tmp+type;
        }
        String t="";
        if (NAME.contains("(")) {
            t = NAME.substring(NAME.indexOf("("), NAME.indexOf(")"));
        }

        if(NAME.contains(")") && !(t.contains("Л") || t.contains("П"))){
            String type = NAME.substring(NAME.indexOf("("),NAME.indexOf(")"));
            NAME = NAME.substring(0,NAME.indexOf("("));
            if(type.contains("лекция")){
                NAME+="(ЛК)";
            }
            else if(type.contains("практика")){
                NAME+="(ПР)";
            }
            else if(type.contains("лабораторная работа")){
                NAME+="(ЛАБ)";
            }
        }
    }

    public String toString() {
        return ("time: " + TIME + "\nName: " + NAME + "\nteacher name: " + TEACHER_NAME + "\naudit: " + CLASSROOM);
    }

    public String outString(){
        findNumber();
        return (TIME+ " "+NAME+" "+"\n"+TEACHER_NAME+" "+CLASSROOM);
    }

    public String saveString(){
        return TIME+";"+NAME+";"+TEACHER_NAME+" ;"+CLASSROOM+"\n";
    }
}
