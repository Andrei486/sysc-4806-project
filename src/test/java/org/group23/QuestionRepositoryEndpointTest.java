package org.group23;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionRepositoryEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    public void setup() {
        Question q1 = new Question("Test?");
        Question q2 = new Question("Test again?");
        Question q3 = new Question("Not in a survey yet?");
        Survey s1 = new Survey();
        s1.addQuestion(q1);
        s1.addQuestion(q2);
        surveyRepository.save(s1); // Cascade saves questions in the survey
        questionRepository.save(q3);
    }

    @Test
    @DirtiesContext
    public void shouldReturnQuestions() throws Exception {
        this.mockMvc.perform(get("/questions")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.questions").isArray());
    }

    @Test
    @DirtiesContext
    public void shouldAddQuestion() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/questions")).andDo(print()).andReturn();
        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        int questionCount = jsonObject.getJSONObject("_embedded").getJSONArray("questions").length();
        this.mockMvc.perform(post("/questions")
                        .accept("application/json")
                        .content("{\"question\": \"Who are you?\"}"))
                .andDo(print()).andExpect(status().isCreated());
        result = this.mockMvc.perform(get("/questions")).andDo(print()).andReturn();
        jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(questionCount + 1,
                jsonObject.getJSONObject("_embedded").getJSONArray("questions").length());
    }

    @Test
    @DirtiesContext
    public void shouldRemoveQuestion() throws Exception {
        MvcResult result = this.mockMvc
                .perform(get("/questions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        int questionCount = jsonObject.getJSONObject("_embedded").getJSONArray("questions").length();
        this.mockMvc.perform(delete("/questions/3")
                        .accept("application/json"))
                .andDo(print()).andExpect(status().is2xxSuccessful());
        result = this.mockMvc.perform(get("/questions")).andDo(print()).andReturn();
        jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(
                questionCount - 1,
                jsonObject.getJSONObject("_embedded").getJSONArray("questions").length());
    }

    @Test
    @DirtiesContext
    public void shouldRemoveQuestionFromSurvey() throws Exception {
        MvcResult result = this.mockMvc
                .perform(get("/surveys/1/questions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        int questionCount = jsonObject.getJSONObject("_embedded").getJSONArray("questions").length();
        this.mockMvc.perform(delete("/surveys/1/questions/2")
                        .accept("application/json"))
                .andDo(print()).andExpect(status().is2xxSuccessful());
        result = this.mockMvc.perform(get("/surveys/1/questions")).andDo(print()).andReturn();
        jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(
                questionCount - 1,
                jsonObject.getJSONObject("_embedded").getJSONArray("questions").length());
    }
}
