package org.group23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurveyTest {

    Survey survey1;
    Question question1;
    Question question2;
    List<Question> questions;

    @BeforeEach
    public void setup(){
        survey1 = new Survey("Survey 1", "admin");
        questions = new ArrayList<>();
        question1 = new TextQuestion("What's your name?");
        question2 = new TextQuestion("What's your favorite color?");
        questions.add(question1);
        questions.add(question2);
    }

    @Test
    public void testGetSetQuestions(){
        survey1.setQuestions(questions);
        assertEquals(survey1.getQuestions(), questions);
    }

    @Test
    public void testGetSetId(){
        survey1.setId(5L);
        assertEquals(5, survey1.getId().longValue());
    }

    @Test
    public void testGetSetName(){
        assertEquals(survey1.getName(), "Survey 1");
        survey1.setName("Best Survey");
        assertEquals(survey1.getName(), "Best Survey");
    }
}
