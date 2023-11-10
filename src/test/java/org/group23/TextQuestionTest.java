package org.group23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextQuestionTest {

    TextQuestion textQuestion1;
    TextQuestion textQuestion2;

    @BeforeEach
    public void setup(){
        textQuestion1 = new TextQuestion("What's your name?");
        textQuestion2 = new TextQuestion("What's your favorite color?");
    }

    @Test
    public void testGetSetId(){
        textQuestion1.setId(5L);
        assertEquals(5, textQuestion1.getId().longValue());
    }

    @Test
    public void testGetSetQuestion(){
        textQuestion1.setQuestion("How tall are you?");
        assertEquals(textQuestion1.getQuestion(), "How tall are you?");
        textQuestion2.setQuestion("How old are you?");
        assertEquals(textQuestion2.getQuestion(), "How old are you?");
    }

    @Test
    public void testGetSetAddAnswers(){
        ArrayList<String> answers = new ArrayList<>();
        answers.add("Jeff");
        answers.add("Jimmy");
        textQuestion1.setAnswers(answers);
        assertEquals(textQuestion1.getAnswers(), answers);
        textQuestion1.addAnswer("John");
        assertTrue(textQuestion1.getAnswers().contains("John"));
    }
}
