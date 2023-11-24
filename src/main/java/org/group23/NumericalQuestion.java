package org.group23;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NumericalQuestion extends Question {

    //single numerical answer with a restricted length
    @ElementCollection
    private List<Double> numericalAnswers;
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

    public NumericalQuestion() {
        super();
        this.numericalAnswers = new ArrayList<>();
    }

    public List<Double> getNumericalAnswers() {
        return numericalAnswers;
    }

    private void addNumericalAnswer(Double answer){
        validateNumericalAnswer(answer);
        this.numericalAnswers.add(answer);
    }

    @Override
    public void addAnswer(String rawAnswer) {
        addNumericalAnswer(Double.parseDouble(rawAnswer));
    }

    public Double getMinBound() {
        return minBound;
    }

    public void setMinBound(Double minBound) {
        validateBounds(minBound, maxBound);
        this.minBound = minBound;
    }

    public Double getMaxBound() {
        return maxBound;
    }

    public void setMaxBound(Double maxBound) {
        validateBounds(minBound, maxBound);
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

    private void validateNumericalAnswer(Double numericalAnswer) {
        if (numericalAnswer < minBound || numericalAnswer > maxBound) {
            throw new IllegalArgumentException("Numerical answer must be within the specified range.");
        }
    }

    private void validateBounds(Double minBound, Double maxBound) {
        if (minBound >= maxBound) {
            throw new IllegalArgumentException("Minimum bound must be less than maximum bound.");
        }
    }
}
