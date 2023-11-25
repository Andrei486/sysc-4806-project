package org.group23;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A question type that allows selecting one option from a choice of many
 */

@Entity
public class MCQuestion extends Question {

    @ElementCollection
    private Collection<String> options;
    @ElementCollection
    private Collection<String> answers = new ArrayList<>();

    /**
     * Default constructor
     */
    public MCQuestion() {
        super();
    }

    /**
     * Constructor with specified question
     * @param question the question text
     * @param options the possible options to choose from
     */
    public MCQuestion(String question, Collection<String> options) {

        super(question);
        this.options = options;

    }

    public Collection<String> getOptions() {
        return options;
    }

    public void setOptions(Collection<String> options) {
        this.options = options;
    }

    public Collection<String> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<String> answers) {
        for (String answer: answers) {
            validateAnswer(answer);
        }
        this.answers = answers;
    }

    /**
     * Adds a new answer
     * @param rawAnswer {String}
     */
    @Override
    public void addAnswer(String rawAnswer) {
        validateAnswer(rawAnswer);
        this.answers.add(rawAnswer);
    }

    private void validateAnswer(String answer) {
        if (!options.contains(answer)) {
            throw new IllegalArgumentException("Answer must be one of the given options.");
        }
    }
}
