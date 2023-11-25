package org.group23;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class NumericalQuestionTest {
    @Test
    public void setNumericalAnswers_validNumericalAnswers_shouldSetAnswers() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question", 0.0, 100.0);

        // Set a valid numerical answer within the specified range
        Double validAnswer = 50.0;
        numericalQuestion.setNumericalAnswers(Collections.singletonList(validAnswer));

        // Verify that the answer is set correctly
        assertEquals(Collections.singletonList(validAnswer), numericalQuestion.getNumericalAnswers());
    }

    @Test
    public void setNumericalAnswers_invalidNumericalAnswers_shouldThrowException() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question", 0.0, 100.0);
        // Set an invalid numerical answer (outside the specified range)
        Double invalidAnswer = 150.0;
        assertThrows(IllegalArgumentException.class, () -> {
            numericalQuestion.setNumericalAnswers(Collections.singletonList(invalidAnswer));
        });
        // Verify that the answer list remains empty
        assertTrue(numericalQuestion.getNumericalAnswers().isEmpty());
    }

    @Test
    public void addNumericalAnswer_withinRange_shouldAddAnswerToList() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question", 0.0, 100.0);
        numericalQuestion.setNumericalAnswers(Collections.singletonList(50.0));
        assertEquals(Arrays.asList(50.0), numericalQuestion.getNumericalAnswers());
    }

    @Test
    public void addNumericalAnswer_outsideRange_shouldThrowException() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question", 0.0, 100.0);
        assertThrows(IllegalArgumentException.class, () -> numericalQuestion.setNumericalAnswers(Collections.singletonList(150.0)));
        assertTrue(numericalQuestion.getNumericalAnswers().isEmpty());
    }

    @Test
    public void setAndGetMinMaxBounds_validBounds_shouldSetAndGetBounds() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question", 0.0, 100.0);

        // Set valid bounds
        numericalQuestion.setMinBound(10.0);
        numericalQuestion.setMaxBound(90.0);

        // Verify that the bounds are set correctly
        assertEquals(10.0, numericalQuestion.getMinBound(), 0.00001);
        assertEquals(90.0, numericalQuestion.getMaxBound(), 0.00001);

        // Set another valid bounds
        numericalQuestion.setMinBound(20.0);
        numericalQuestion.setMaxBound(80.0);

        // Verify that the bounds are updated correctly
        assertEquals(20.0, numericalQuestion.getMinBound(), 0.00001);
        assertEquals(80.0, numericalQuestion.getMaxBound(), 0.00001);
    }

    @Test
    public void setMinMaxBounds_invalidBounds_shouldThrowException() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question", 0.0, 100.0);

        // Set invalid bounds (minBound > maxBound)
        assertThrows(IllegalArgumentException.class, () -> {
            numericalQuestion.setMinBound(110.0);
            numericalQuestion.setMaxBound(90.0);
        });

        // Ensure that the bounds remain unchanged
        assertEquals(0.0, numericalQuestion.getMinBound(), 0.00001);
        assertEquals(100.0, numericalQuestion.getMaxBound(), 0.00001);
    }



}