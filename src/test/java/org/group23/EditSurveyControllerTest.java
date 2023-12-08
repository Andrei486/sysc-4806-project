package org.group23;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditSurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private long surveyId;
    private Survey survey;
    private long textQuestionId;

    @BeforeEach
    public void setup() {
        surveyRepository.deleteAll();
        questionRepository.deleteAll();
        survey = new Survey("SurveyMonkey", "user1");
        Question textQuestion = new TextQuestion("Test TextQuestion");
        Question numericalQuestion = new NumericalQuestion("Test NumericalQuestion", -0.1, 20.0);
        LinkedList<String> options = new LinkedList<>();
        options.add("Test Option 1");
        options.add("Test Option 2");
        Question mcQuestion = new MCQuestion("Test MCQuestion", options);
        survey.addQuestion(textQuestion);
        survey.addQuestion(numericalQuestion);
        survey.addQuestion(mcQuestion);
        surveyRepository.save(survey);
        surveyId = survey.getId();
        textQuestionId = textQuestion.getId();
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    @Transactional
    public void testAddTextQuestion() throws Exception {
        String questionText = "Sample text question";
        mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion/{surveyId}/text", surveyId)
                        .param("questionText", questionText)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/addRemoveQuestions/" + surveyId));

        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        assertNotNull(updatedSurvey);
        assertEquals(4, updatedSurvey.getQuestions().size());
        assertTrue(updatedSurvey.getQuestions().stream().anyMatch(q -> q instanceof TextQuestion && q.getQuestion().equals(questionText)));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    @Transactional
    public void testAddMCQuestion() throws Exception {
        String questionText = "Sample MC question";
        String option1 = "Option 1";
        String option2 = "Option 2";
        String option3 = "Option 3";

        mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion/{surveyId}/mc", surveyId)
                        .param("questionText", questionText)
                        .param("mcOption", option1, option2, option3)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/addRemoveQuestions/" + surveyId));

        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        assertNotNull(updatedSurvey);
        assertEquals(4, updatedSurvey.getQuestions().size());
        assertTrue(updatedSurvey.getQuestions().stream().anyMatch(q -> q instanceof MCQuestion && q.getQuestion().equals(questionText)
                && ((MCQuestion) q).getOptions().containsAll(List.of(option1, option2, option3))));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    @Transactional
    public void testAddNumericalQuestion() throws Exception {
        String questionText = "Sample numerical question";
        double minBound = 0.0;
        double maxBound = 10.0;

        mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion/{surveyId}/numerical", surveyId)
                        .param("questionText", questionText)
                        .param("minBound", String.valueOf(minBound))
                        .param("maxBound", String.valueOf(maxBound))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/addRemoveQuestions/" + surveyId));

        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        assertNotNull(updatedSurvey);
        assertEquals(4, updatedSurvey.getQuestions().size());
        assertTrue(updatedSurvey.getQuestions().stream().anyMatch(q -> q instanceof NumericalQuestion && q.getQuestion().equals(questionText)
                && ((NumericalQuestion) q).getMinBound() == minBound && ((NumericalQuestion) q).getMaxBound() == maxBound));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    @Transactional
    public void testDeleteQuestion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteQuestion/{surveyId}/{questionId}", surveyId, textQuestionId)
                    .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/addRemoveQuestions/" + surveyId));

        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        assertNotNull(updatedSurvey);
        assertEquals(2, updatedSurvey.getQuestions().size());
        assertTrue(updatedSurvey.getQuestions().stream().noneMatch(q -> q.getId().equals(textQuestionId)));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void testCloseSurvey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/closeSurvey/{surveyId}", surveyId)
                    .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/addRemoveQuestions/" + surveyId));

        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        assertNotNull(updatedSurvey);
        assertFalse(updatedSurvey.isOpen());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void testOpenSurvey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/openSurvey/{surveyId}", surveyId)
                    .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/addRemoveQuestions/" + surveyId));

        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        assertNotNull(updatedSurvey);
        assertTrue(updatedSurvey.isOpen());
    }
}

