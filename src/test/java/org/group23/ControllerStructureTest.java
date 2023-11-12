package org.group23;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerStructureTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    public void createSurveyForm() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/createSurvey"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("createSurvey"));
    }

    @Test
    public void saveSurveyName() throws Exception {
        Survey survey = new Survey("SurveyMonkey");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/saveSurveyName")
                        .flashAttr("survey", survey))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/addRemoveQuestions/" + survey.getId()));
    }

    @Test
    public void addRemoveQuestionsForm() throws Exception {
        Survey survey = new Survey("SurveyMonkey");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/addRemoveQuestions/{id}", survey.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("addRemoveQuestions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("survey"));
    }

    @Test
    public void addQuestion() throws Exception {
        Survey survey = new Survey("SurveyMonkey");
        this.surveyRepository.save(survey);

        String textQuestion = "Who is Joe?";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addQuestion/{surveyId}", survey.getId())
                        .param("questionText", textQuestion))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/addRemoveQuestions/" + survey.getId()));
    }

    @Test
    public void deleteQuestion() throws Exception {
        Survey survey = new Survey("SurveyMonkey");
        this.surveyRepository.save(survey);

        Question question = new TextQuestion("Who is Joe?");
        survey.addQuestion(question);
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/deleteQuestion/{surveyId}/{questionId}", survey.getId(), question.getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/addRemoveQuestions/" + survey.getId()));
    }

    @Test
    public void saveSurvey() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/saveSurvey"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/surveyCreated"));
    }

    @Test
    public void surveyCreated() throws Exception {
        Survey survey = new Survey("SurveyMonkey");
        this.surveyRepository.save(survey);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/surveyCreated"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("surveyCreated"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("survey"));
    }
}
