package me.leofontes.driversed;

/**
 * Created by Leo on 3/22/16.
 */
public class DriveInfo {

    private String hours;
    private String dayOfTheWeek;
    private String date;
    private String condition;
    private String lesson;
    private String weather;

    public DriveInfo() {

    }

    public DriveInfo(String date, String dayOfTheWeek, String hours, String condition, String lesson, String weather) {
        this.hours = hours;
        this.dayOfTheWeek = dayOfTheWeek;
        this.date = date;
        this.condition = condition;
        this.lesson = lesson;
        this.weather = weather;
    }

    public String getHours() {return hours;}
    public String getDayOfTheWeek() {return dayOfTheWeek;}
    public String getDate() {return date;}
    public String getCondition() {return condition;}
    public String getLesson() {return lesson;}
    public String getWeather() {return weather;}
}