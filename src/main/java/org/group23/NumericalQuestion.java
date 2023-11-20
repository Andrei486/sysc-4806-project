package org.group23;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NumericalQuestion extends Question {

    //single numerical answer with a restricted length
    @Column
    private Double numericalAnswer;

    public NumericalQuestion(String question) {
        super(question);
    }

    public NumericalQuestion() {
    }

    public Double getAnswer() {
        return numericalAnswer;
    }

    public void setAnswer(String numericalAnswer) {
        // Validate the numerical values
        try {
            this.numericalAnswer = Double.parseDouble(numericalAnswer);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numerical answer. Please enter a valid number.");
        }
    }
}
