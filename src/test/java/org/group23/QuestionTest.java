package org.group23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
public class QuestionTest {
    Question question1;

    Question question2;

    @BeforeEach
    public void setup(){
        question1 = new TextQuestion("What program are you in?");
        question1.setId(2L);
        question2 = new TextQuestion("What's your favorite animal?");
        question2.setId(3L);
    }

    @Test
    public void testGetId(){
        assertEquals(2, question1.getId().longValue());
    }

    @Test
    public void testSetId(){
        question1.setId(5L);
        assertEquals(5, question1.getId().longValue());
    }

    @Test
    public void testGetQuestion(){
        assertEquals(question1.getQuestion(), "What program are you in?");
        assertEquals(question2.getQuestion(), "What's your favorite animal?");
    }

    @Test
    public void testSetQuestion(){
        question1.setQuestion("Where do you live?");
        assertEquals(question1.getQuestion(), "Where do you live?");
        question2.setQuestion("How old are you?");
        assertEquals(question2.getQuestion(), "How old are you?");
    }
}
