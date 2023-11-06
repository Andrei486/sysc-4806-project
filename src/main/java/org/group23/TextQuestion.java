package org.group23;

public class TextQuestion extends Question{

    private String textResponse;

    public TextQuestion(String question) {
        super(question);
    }

    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    public String getTextResponse(){
        return this.textResponse;
    }
}
