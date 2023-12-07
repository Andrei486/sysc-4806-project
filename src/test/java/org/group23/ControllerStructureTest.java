package org.group23;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.LinkedList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerStructureTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurveyRepository surveyRepository;

    @BeforeEach
    public void setup() {
        surveyRepository.deleteAll();
        Survey survey = new Survey("SurveyMonkey", "user1");
        surveyRepository.save(survey);
    }

    @Test
    @DirtiesContext
    public void accessHomepageUnauthenticated() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void createSurveyForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/createSurvey"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("createSurvey"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void addQuestion() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        String textQuestion = "Who is Joe?";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion/{surveyId}/text", survey.getId())
                        .param("questionText", textQuestion).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/addRemoveQuestions/" + survey.getId()));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void deleteQuestion() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        Question question = new TextQuestion("Who is Joe?");
        survey.addQuestion(question);
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/deleteQuestion/{surveyId}/{questionId}", survey.getId(), question.getId())
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/addRemoveQuestions/" + survey.getId()));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void addRemoveQuestionsForm() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/addRemoveQuestions/{id}", survey.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("addRemoveQuestions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("survey"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void saveSurvey() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/saveSurvey"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/surveyCreated"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void saveSurveyName() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/saveSurveyName")
                        .flashAttr("survey", survey).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/addRemoveQuestions/" + survey.getId()));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void surveyCreated() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/surveyCreated/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("surveyCreated"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("survey"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void deleteSurvey() throws Exception {
        Survey survey = new Survey("SurveyMonkey", "user1");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/deleteSurvey/{surveyId}", survey.getId())
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/createSurvey"))
                .andExpect(MockMvcResultMatchers.flash().attributeCount(0));
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user2")
    public void viewSurveysShowsAllForUser() throws Exception {
        Survey s1 = new Survey("Test Survey 1", "user2");
        Survey s2 = new Survey("Test Survey 2", "user2");
        surveyRepository.save(s1);
        surveyRepository.save(s2);
        var id = s1.getId();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/viewSurveys"))
                // Check that request was successful
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("viewSurveys"))
                // Check that (only) the correct surveys are present
                .andExpect(xpath("//h2[contains(text(), \"Test Survey 1\")]").exists())
                .andExpect(xpath("//h2[contains(text(), \"Test Survey 2\")]").exists())
                .andExpect(xpath("//h2[contains(text(), \"SurveyMonkey\")]").doesNotExist())
                // Check that each kind of link exists for a survey
                .andExpect(xpath(String.format("//a[@href=\"/addRemoveQuestions/%d\"]", id)).exists())
                .andExpect(xpath(String.format("//a[@href=\"/viewResults/%d\"]", id)).exists())
                .andExpect(xpath(String.format("//a[@href=\"/answerSurvey/%d\"]", id)).exists());
    }
}
