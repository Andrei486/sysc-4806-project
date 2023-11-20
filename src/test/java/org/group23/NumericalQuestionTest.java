package org.group23;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericalQuestionTest {

    @Test
    public void setNumericalAnswer_validLength_shouldSetAnswer() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question");

        // Set a valid numerical answer
        numericalQuestion.setNumericalAnswer("12345");

        // Verify that the answer is set correctly
        assertEquals("12345", numericalQuestion.getNumericalAnswer());
    }

    @Test
    public void setNumericalAnswer_invalidLength_shouldThrowException() {
        NumericalQuestion numericalQuestion = new NumericalQuestion("Test Numerical Question");

        // Set an invalid numerical answer (too long)
        assertThrows(IllegalArgumentException.class, () -> {
            numericalQuestion.setNumericalAnswer("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        });
    }
}