package com.example.pokelearn;

public class Chapters {
    String chapterId;
    String chapterCourse;
    String chapterCourseId;
    String chapterTitle;
    Integer chapterSequence;
    String chapterMaterialUrl;
    String chapterYoutubeVideoId;


    public Chapters(){

    }

    public Chapters(String chapterId, String chapterCourse, String chapterCourseId, String chapterTitle,
                    Integer chapterSequence, String chapterMaterialUrl, String chapterYoutubeVideoId) {
        this.chapterId = chapterId;
        this.chapterCourse = chapterCourse;
        this.chapterCourseId = chapterCourseId;
        this.chapterTitle = chapterTitle;
        this.chapterSequence = chapterSequence;
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

    public Integer  getChapterSequence() {
        return chapterSequence;
    }

    public String getChapterMaterialUrl() {
        return chapterMaterialUrl;
    }

    public String getChapterYoutubeVideoId() {
        return chapterYoutubeVideoId;
    }
}
