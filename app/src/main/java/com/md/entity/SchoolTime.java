package com.md.entity;

public class SchoolTime {


    //周次
    private String week_of_semester;
    //星期
    private String day_of_week;
    //节次
    private String part;
    //节数
    private String partLength;
    //校区
    private String campus;
    //教学楼
    private String building;
    //教室
    private String classRoom;

    //课程号
    private String courseNumber;

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }


    public String getWeek_of_semester() {
        return week_of_semester;
    }

    public void setWeek_of_semester(String week_of_semester) {
        this.week_of_semester = week_of_semester;
    }

    public String getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPartLength() {
        return partLength;
    }

    public void setPartLength(String partLength) {
        this.partLength = partLength;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }


}
