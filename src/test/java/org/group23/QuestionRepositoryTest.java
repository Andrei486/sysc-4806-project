package org.group23;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DirtiesContext
    public void saveQuestion() {
        String question1 = "Test Question 1?";
        String question2 = "Test Question 2?";
        Question q1 = new TextQuestion(question1);
        Question q2 = new Question(question2);

        questionRepository.save(q1);
        questionRepository.save(q2);

        // No assertions: just expect no errors regarding constraints etc.
    }

    @Test
    @DirtiesContext
    public void getQuestionById() {
        String question1 = "Test Question 1?";
        String question2 = "Test Question 2?";
        Question q1 = new TextQuestion(question1);
        Question q2 = new Question(question2);
        questionRepository.save(q1);
        questionRepository.save(q2);

        Optional<Question> result1 = questionRepository.findById(1L);
        Optional<Question> result2 = questionRepository.findById(2L);

        assertTrue(result1.isPresent());
        assertEquals(question1, result1.get().getQuestion());
        assertTrue(result2.isPresent());
        assertEquals(question2, result2.get().getQuestion());
    }

    @Test
    @DirtiesContext
    public void getQuestionByQuestion() {
        String question1 = "Test Question 1?";
        String question2 = "Test Question 2?";
        Question q1 = new TextQuestion(question1);
        Question q2 = new Question(question2);
        questionRepository.save(q1);
        questionRepository.save(q2);

        var result1 = questionRepository.findByQuestion(question1);
        var result2 = questionRepository.findByQuestion(question2);

        assertNotNull(result1);
        assertEquals(question1, result1.getQuestion());
        assertNotNull(result2);
        assertEquals(question2, result2.getQuestion());
    }

    @Test
    @DirtiesContext
    public void deleteQuestion() {
        String question1 = "Test Question 1?";
        String question2 = "Test Question 2?";
        Question q1 = new TextQuestion(question1);
        Question q2 = new Question(question2);
        questionRepository.save(q1);
        questionRepository.save(q2);

        questionRepository.delete(q1);
        Optional<Question> result1 = questionRepository.findById(1L);
        Optional<Question> result2 = questionRepository.findById(2L);

        assertFalse(result1.isPresent());
        assertTrue(result2.isPresent());
    }
}
