package org.group23;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyRepositoryEndpointTest {

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
        Survey s1 = new Survey("Test Survey 1", "user1");
        Survey s2 = new Survey("Test Survey 2", "user1");
        s1.addQuestion(q1);
        s1.addQuestion(q2);
        surveyRepository.save(s1); // Cascade saves questions in the survey
        surveyRepository.save(s2);
        questionRepository.save(q3);
    }

    @Test
    @WithMockUser(username = "user1")
    public void shouldReturnSurveys() throws Exception {
        this.mockMvc.perform(get("/surveys")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.surveys").isArray());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void shouldAddSurvey() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/surveys")).andDo(print()).andReturn();
        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        int addressBookCount = jsonObject.getJSONObject("_embedded").getJSONArray("surveys").length();
        this.mockMvc.perform(post("/surveys")
                        .with(csrf())
                        .accept("application/json")
                        .content("{\"id\": 5}"))
                .andDo(print()).andExpect(status().isCreated());
        result = this.mockMvc.perform(get("/surveys")).andDo(print()).andReturn();
        jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(
                addressBookCount + 1,
                jsonObject.getJSONObject("_embedded").getJSONArray("surveys").length());
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "user1")
    public void shouldRemoveSurvey() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/surveys")).andDo(print()).andReturn();
        JSONObject jsonObject = new JSONObject(result.getResponse().getContentAsString());
        int addressBookCount = jsonObject.getJSONObject("_embedded").getJSONArray("surveys").length();
        this.mockMvc.perform(delete("/surveys/2")
                        .with(csrf())
                        .accept("application/json"))
                .andDo(print()).andExpect(status().is2xxSuccessful());
        result = this.mockMvc.perform(get("/surveys")).andDo(print()).andReturn();
        jsonObject = new JSONObject(result.getResponse().getContentAsString());
        Assertions.assertEquals(
                addressBookCount - 1,
                jsonObject.getJSONObject("_embedded").getJSONArray("surveys").length());
        this.mockMvc.perform(get("/surveys/2")).andDo(print()).andExpect(status().isNotFound());
    }
}
