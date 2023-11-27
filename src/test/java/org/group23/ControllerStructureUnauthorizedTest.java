package org.group23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerStructureUnauthorizedTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurveyRepository surveyRepository;

    @BeforeEach
    public void setup() {
        Survey survey = new Survey("SurveyMonkey", "user1");
        surveyRepository.save(survey);
    }

    @Test
    @DirtiesContext
    public void createSurveyUnauthenticated() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/createSurvey"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("*://*/login"));
    }

    @Test
    @DirtiesContext
    public void surveyEndpointUnauthenticated() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/survey/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("*://*/login"));
    }

    @Test
    @DirtiesContext
    public void questionEndpointUnauthenticated() throws Exception {
        this.mockMvc.perform(post("/questions")
                .with(csrf())
                .accept("application/json")
                .content("{\"question\": \"Who are you?\"}"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("*://*/login"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user2")
    public void addQuestionWrongUser() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        String textQuestion = "Who is Joe?";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion/{surveyId}/text", survey.getId())
                        .param("questionText", textQuestion).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user2")
    public void deleteQuestionWrongUser() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        Question question = new TextQuestion("Who is Joe?");
        survey.addQuestion(question);
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/deleteQuestion/{surveyId}/{questionId}", survey.getId(), question.getId())
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user2")
    public void addRemoveQuestionsWrongUser() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/addRemoveQuestions/{id}", survey.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user2")
    public void deleteSurveyWrongUser() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/deleteSurvey/{surveyId}", survey.getId())
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }
}
