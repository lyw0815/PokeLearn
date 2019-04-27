package com.example.pokelearn;

public class Progress {
    String chapterId;
    String progressChapter;
    Double chapterSequence;

    public Progress(){

    }

    public Progress(String chapterId, String progressChapter, Double chapterSequence) {
        this.chapterId = chapterId;
        this.progressChapter = progressChapter;
        this.chapterSequence = chapterSequence;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getProgressChapter() {
        return progressChapter;
    }

    public Double getChapterSequence() { return chapterSequence; }
}

