package org.group23;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SurveyRepositoryTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DirtiesContext
    public void saveSurvey() {
        var s1 = new Survey("Test Survey 1");

        surveyRepository.save(s1);

        // No assertions: just expect no errors regarding constraints etc.
    }

    @Test
    @DirtiesContext
    public void getSurveyById() {
        var name1 = "Test Survey 1";
        var name2 = "Test Survey 2";
        var s1 = new Survey(name1);
        var s2 = new Survey(name2);
        surveyRepository.save(s1);
        surveyRepository.save(s2);

        Optional<Survey> result1 = surveyRepository.findById(1L);
        Optional<Survey> result2 = surveyRepository.findById(2L);

        assertTrue(result1.isPresent());
        assertEquals(name1, result1.get().getName());
        assertTrue(result2.isPresent());
        assertEquals(name2, result2.get().getName());
    }

    @Test
    @DirtiesContext
    public void getSurveyByName() {
        var name1 = "Test Survey 1";
        var name2 = "Test Survey 2";
        var s1 = new Survey(name1);
        var s2 = new Survey(name2);
        surveyRepository.save(s1);
        surveyRepository.save(s2);

        var result1 = surveyRepository.findByName(name1);
        var result2 = surveyRepository.findByName(name2);

        assertNotNull(result1);
        assertEquals(name1, result1.getName());
        assertNotNull(result2);
        assertEquals(name2, result2.getName());
    }

    @Test
    @DirtiesContext
    public void saveSurveyCascadeSavesQuestions() {
        var q1 = new TextQuestion("Test Question 1?");
        var q2 = new TextQuestion("Test Question 2?");
        var s1 = new Survey("Test Survey 1");
        s1.addQuestion(q1);
        s1.addQuestion(q2);

        surveyRepository.save(s1);
        var surveyResults = surveyRepository.findAll();
        var questionResults = questionRepository.findAll();

        assertEquals(1, StreamSupport.stream(surveyResults.spliterator(), false).count());
        assertEquals(2, StreamSupport.stream(questionResults.spliterator(), false).count());
    }

    @Test
    @DirtiesContext
    public void addQuestionToSurveyPropagates() {
        var q1 = new TextQuestion("Test Question 1?");
        var q2 = new TextQuestion("Test Question 2?");
        var s1 = new Survey("Test Survey 1");
        s1.addQuestion(q1);
        surveyRepository.save(s1);
        // Check that initialization was correct
        var surveyResults = surveyRepository.findAll();
        var questionResults = questionRepository.findAll();
        assertEquals(1, StreamSupport.stream(surveyResults.spliterator(), false).count());
        assertEquals(1, StreamSupport.stream(questionResults.spliterator(), false).count());

        Survey survey = surveyRepository.findByName("Test Survey 1");
        survey.addQuestion(q2);

        surveyResults = surveyRepository.findAll();
        questionResults = questionRepository.findAll();
        assertEquals(1, StreamSupport.stream(surveyResults.spliterator(), false).count());
        assertEquals(2, StreamSupport.stream(questionResults.spliterator(), false).count());
    }

    @Test
    @DirtiesContext
    public void removeQuestionFromSurveyDoesNotPropagate() {
        var q1 = new TextQuestion("Test Question 1?");
        var q2 = new TextQuestion("Test Question 2?");
        var s1 = new Survey("Test Survey 1");
        s1.addQuestion(q1);
        s1.addQuestion(q2);
        surveyRepository.save(s1);
        // Check that initialization was correct
        var surveyResults = surveyRepository.findAll();
        var questionResults = questionRepository.findAll();
        assertEquals(1, StreamSupport.stream(surveyResults.spliterator(), false).count());
        assertEquals(2, StreamSupport.stream(questionResults.spliterator(), false).count());

        Survey survey = surveyRepository.findByName("Test Survey 1");
        survey.removeQuestion(q2);

        surveyResults = surveyRepository.findAll();
        Question question = questionRepository.findByQuestion("Test Question 2?");
        assertEquals(1, StreamSupport.stream(surveyResults.spliterator(), false).count());
        assertNotNull(question);
    }
}
