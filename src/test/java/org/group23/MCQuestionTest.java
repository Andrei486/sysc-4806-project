package org.group23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class MCQuestionTest {
    MCQuestion MCQuestion1;
    MCQuestion MCQuestion2;

    @BeforeEach
    public void setup(){
        MCQuestion1 = new MCQuestion("What's your favourite pet?", List.of(new String[]{"Cats", "Dogs"}));
        MCQuestion1.setAnswers(List.of(new String[]{"Cats"}));
        MCQuestion2 = new MCQuestion("What is the second biggest country?", List.of(new String[]{"Russia", "Canada", "US"}));
    }

    @Test
    public void testGetSetOptions(){
        MCQuestion1.setOptions(List.of(new String[]{"Cats", "Dogs", "Fish"}));
        assertEquals(List.of(new String[]{"Cats", "Dogs", "Fish"}), MCQuestion1.getOptions());
    }

    @Test
    public void testGetSetAnswers(){
        MCQuestion2.setAnswers(List.of(new String[]{"Canada"}));
        assertEquals(List.of(new String[]{"Canada"}), MCQuestion2.getAnswers());

    }

//    @Test
//    public void testAddAnswer(){
//        MCQuestion1.addAnswer("Dogs");
//        assertEquals(List.of(new String[]{"Cats", "Dogs"}), MCQuestion1.getAnswers());
//        MCQuestion1.addAnswer("Fish");
//        assertEquals(List.of(new String[]{"Cats", "Dogs", "Fish"}), MCQuestion1.getAnswers());
//    }
}
