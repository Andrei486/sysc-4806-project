package org.group23;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SurveyAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private final String ANSWER_ID_PREFIX = "question_";

    private long surveyId;
    private long textQuestionId;
    private long numericalQuestionId;

    @BeforeEach
    public void setup() {
        Survey survey = new Survey("SurveyMonkey", "user1");
        Question textQuestion = new TextQuestion("Test TextQuestion");
        Question numericalQuestion = new NumericalQuestion("Test NumericalQuestion", -0.1, 20.0);
        survey.addQuestion(textQuestion);
        survey.addQuestion(numericalQuestion);
        surveyRepository.save(survey); // Also saves questions
        surveyId = survey.getId();
        textQuestionId = textQuestion.getId();
        numericalQuestionId = numericalQuestion.getId();
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user2")
    public void showAnswerPage() throws Exception {
        String textQuestionIdString = ANSWER_ID_PREFIX + textQuestionId;
        String numericalQuestionIdString = ANSWER_ID_PREFIX + numericalQuestionId;
        this.mockMvc.perform(MockMvcRequestBuilders.get("/answerSurvey/{surveyId}", surveyId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("answerSurvey"))
                .andExpect(xpath(String.format("//input[@name=\"%s\" and @type=\"text\"]", textQuestionIdString)).exists())
                .andExpect(xpath(String.format("//input[@name=\"%s\" and @type=\"number\" and @min=\"-0.1\" and @max=\"20.0\"]", numericalQuestionIdString)).exists())
                .andExpect(xpath("//input[@type=\"submit\"]").exists());
    }

    @Test
    @DirtiesContext
    @Transactional
    @WithMockUser(username = "user2")
    public void submitAnswers() throws Exception {
        String textQuestionIdString = ANSWER_ID_PREFIX + textQuestionId;
        String numericalQuestionIdString = ANSWER_ID_PREFIX + numericalQuestionId;
        this.mockMvc.perform(MockMvcRequestBuilders.post("/answerSurvey/{surveyId}/submit", surveyId)
                        .param(textQuestionIdString, "Text Answer")
                        .param(numericalQuestionIdString, "10.23")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
        TextQuestion textQuestion = (TextQuestion) questionRepository.findById(textQuestionId).orElse(null);
        assertNotNull(textQuestion);
        NumericalQuestion numericalQuestion = (NumericalQuestion) questionRepository.findById(numericalQuestionId).orElse(null);
        assertNotNull(numericalQuestion);
        assertEquals(1, textQuestion.getAnswers().size());
        assertTrue(textQuestion.getAnswers().contains("Text Answer"));
        assertEquals(1, numericalQuestion.getAnswers().size());
        assertTrue(numericalQuestion.getAnswers().contains(10.23));
    }
}
