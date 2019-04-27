package com.example.pokelearn;

public class Quiz {
    String quizTitle;
    String quizId;
    String quizCreator;
    String quizQuestions;

    public Quiz(String quizTitle, String quizId, String quizCreator, String quizQuestions) {
        this.quizTitle = quizTitle;
        this.quizId = quizId;
        this.quizCreator = quizCreator;
        this.quizQuestions = quizQuestions;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public String getQuizId() {
        return quizId;
    }

    public String getQuizCreator() {
        return quizCreator;
    }

    public String getQuizQuestions() {
        return quizQuestions;
    }
}
