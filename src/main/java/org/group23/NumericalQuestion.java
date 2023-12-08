package org.group23;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NumericalQuestion extends Question {

    @ElementCollection
    private List<Double> numericalAnswers;

    //Allow nullability for minBound and maxBound
    @Column
    private Double minBound;

    @Column
    private Double maxBound;

    public NumericalQuestion(String question, Double minBound, Double maxBound) {
        super(question);
        this.minBound = minBound;
        this.maxBound = maxBound;
        this.numericalAnswers = new ArrayList<>();
    }

    //Constructor without bounds
    public NumericalQuestion(String question) {
        super(question);
        this.numericalAnswers = new ArrayList<>();
        this.minBound = null;
        this.maxBound = null;
    }

    public NumericalQuestion() {
        super();
        this.numericalAnswers = new ArrayList<>();
    }

    public List<Double> getNumericalAnswers() {
        return numericalAnswers;
    }

    private void addNumericalAnswer(Double answer){
        //Check bounds only if they are specified
        if (minBound != null && maxBound != null) {
            validateNumericalAnswer(answer);
        }
        this.numericalAnswers.add(answer);
    }

    @Override
    public void addAnswer(String rawAnswer) {
        addNumericalAnswer(Double.parseDouble(rawAnswer));
    }

    //Validate bounds only if they are specified
    public void validateNumericalAnswer(Double numericalAnswer) {
        if (minBound != null && numericalAnswer < minBound) {
            throw new IllegalArgumentException("Numerical answer must be greater than or equal to the specified minimum bound.");
        }
        if (maxBound != null && numericalAnswer > maxBound) {
            throw new IllegalArgumentException("Numerical answer must be less than or equal to the specified maximum bound.");
        }
    }

    public Double getMinBound() {
        return minBound;
    }

    //Allow setting null for minBound
    public void setMinBound(Double minBound) {
        if (maxBound != null) {
            validateBounds(minBound, maxBound);
        }
        this.minBound = minBound;
    }

    public Double getMaxBound() {
        return maxBound;
    }

    //Allow setting null for maxBound
    public void setMaxBound(Double maxBound) {
        if (minBound != null) {
            validateBounds(minBound, maxBound);
        }
        this.maxBound = maxBound;
    }

    public List<Double> getAnswers() {
        return numericalAnswers;
    }

    public void setNumericalAnswers(List<Double> newAnswers) {
        for (Double answer : newAnswers) {
            validateNumericalAnswer(answer);
        }
        numericalAnswers.clear();
        numericalAnswers.addAll(newAnswers);
    }

    //Validate bounds only if they are specified
    private void validateBounds(Double minBound, Double maxBound) {
        if (minBound != null && maxBound != null && minBound >= maxBound) {
            throw new IllegalArgumentException("Minimum bound must be less than maximum bound.");
        }
    }
}
