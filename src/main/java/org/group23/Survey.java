package org.group23;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String author;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;

    public Survey(){
        this.questions = new ArrayList<>();
    }

    public Survey(String name, String author){
        this();
        this.name = name;
        this.author = author;
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public void removeQuestion(Question question){
        this.questions.remove(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
