package org.group23;

public class Question {

    private int  survey_id;
    private int id;
    private String question;

    public Question(String question){
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public int getSurvey_id() {
        return survey_id;
    }
}