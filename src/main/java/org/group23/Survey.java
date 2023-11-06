package org.group23;

import java.util.ArrayList;
import java.util.List;

public class Survey {

    private int id;
    private List<Question> questions;

    public Survey(){
        this.questions = new ArrayList<Question>();
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public void removeQuestion(Question question){
        this.questions.remove(question);
    }

    public int getId() {
        return id;
    }
}
