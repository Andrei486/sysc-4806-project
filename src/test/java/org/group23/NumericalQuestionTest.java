package org.group23;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NumericalQuestionTest {

    @Test
    public void setNumericalAnswer_validLength_shouldSetAnswer() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question");

        // Set a valid numerical answer
        numericalQuestion.setAnswer("123.45");

        // Verify that the answer is set correctly
        assertEquals(123.45, numericalQuestion.getAnswer(), 0.001); // Using a delta for double comparison
    }

    @Test
    public void setNumericalAnswer_invalidLength_shouldThrowException() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question");

        // Set an invalid numerical answer (non-numeric)
        assertThrows(IllegalArgumentException.class, () -> {
            numericalQuestion.setAnswer("invalid");
        });
    }
}