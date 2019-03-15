package com.example.pokelearn;

public class Chapters {
    String chapterId;
    String chapterCourse;
    String chapterCourseId;
    String chapterTitle;
    Double chapterSequence;
    String chapterDesc;
    String chapterMaterialUrl;
    String chapterYoutubeVideoId;


    public Chapters(){

    }

    public Chapters(String chapterId, String chapterCourse, String chapterCourseId, String chapterTitle,
                    Double chapterSequence, String chapterDesc, String chapterMaterialUrl, String chapterYoutubeVideoId) {
        this.chapterId = chapterId;
        this.chapterCourse = chapterCourse;
        this.chapterCourseId = chapterCourseId;
        this.chapterTitle = chapterTitle;
        this.chapterSequence = chapterSequence;
        this.chapterDesc = chapterDesc;
        this.chapterMaterialUrl = chapterMaterialUrl;
        this.chapterYoutubeVideoId = chapterYoutubeVideoId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getChapterCourse() {
        return chapterCourse;
    }

    public String getChapterCourseId (){return chapterCourseId;}

    public String getChapterTitle() {
        return chapterTitle;
    }

    public Double  getChapterSequence() {
        return chapterSequence;
    }

    public String getChapterDesc(){return chapterDesc;}

    public String getChapterMaterialUrl() {
        return chapterMaterialUrl;
    }

    public String getChapterYoutubeVideoId() {
        return chapterYoutubeVideoId;
    }
}
