package org.group23;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TextQuestion extends Question {

    @ElementCollection
    private List<String> answers;

    public TextQuestion() {
        super();
    }

    public TextQuestion(String question) {
        super(question);
        this.answers = new ArrayList<>();
    }

    public void addAnswer(String answer){
        this.answers.add(answer);
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
