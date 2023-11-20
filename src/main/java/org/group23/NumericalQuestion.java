package org.group23;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class NumericalQuestion extends Question {

    //single numerical answer with a restricted length
    @Column
    private String numericalAnswer;

    public NumericalQuestion(String question, String numericalAnswer) {
        super(question);
        this.numericalAnswer = numericalAnswer;
    }

    public NumericalQuestion(String question) {
        super(question);
    }

    public NumericalQuestion() {

    }

    public String getNumericalAnswer() {
        return numericalAnswer;
    }

    public void setNumericalAnswer(String numericalAnswer) {
        // Validate length before setting the answer
        if (numericalAnswer.length() <= 255) {
            this.numericalAnswer = numericalAnswer;
        } else {
            throw new IllegalArgumentException("Numerical answer is too long. Please enter a shorter answer.");
        }
    }
}
