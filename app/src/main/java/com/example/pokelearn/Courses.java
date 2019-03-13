package com.example.pokelearn;

import android.net.Uri;

public class Courses {

    String courseId;
    String courseName;
    String courseDesc;
    String courseCoverImgUrl;
    String courseInstructorName;
    String courseInstructorMail;
    String courseInstructorId;

    public Courses(){

    }

    public Courses(String courseId, String courseName, String courseDesc, String courseCoverImgUrl, String courseInstructorName, String courseInstructorMail, String courseInstructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDesc = courseDesc;
        this.courseCoverImgUrl = courseCoverImgUrl;
        this.courseInstructorName = courseInstructorName;
        this.courseInstructorMail = courseInstructorMail;
        this.courseInstructorId = courseInstructorId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public String getCourseCoverImgUrl() {
        return courseCoverImgUrl;
    }

    public String getCourseInstructorName() {
        return courseInstructorName;
    }

    public String getCourseInstructorMail() { return courseInstructorMail; }

    public String getCourseInstructorId() {return  courseInstructorId; }
}
