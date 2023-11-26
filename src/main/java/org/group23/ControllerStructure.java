package org.group23;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@org.springframework.stereotype.Controller
public class ControllerStructure {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SurveyRepository surveyRepository;

    @GetMapping("/createSurvey")
    public String createSurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "createSurvey";
    }

    @PostMapping("/saveSurveyName")
    public String saveSurveyName(@ModelAttribute("survey") Survey survey) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        survey.setAuthor(username);
        surveyRepository.save(survey);
        return "redirect:/addRemoveQuestions/" + survey.getId();
    }

    @GetMapping("/saveSurvey")
    public String saveSurvey() {
        return "redirect:/surveyCreated";
    }

    @GetMapping("/surveyCreated/{surveyId}")
    public String surveyCreated(@PathVariable Long surveyId, Model model) {
        // Retrieve the survey by the provided surveyId
        Survey survey = surveyRepository.findById(surveyId).orElse(null);

        if (survey != null) {
            model.addAttribute("survey", survey);
            return "surveyCreated";
        } else {
            // Handle the case where the survey is not found
            return "error";
        }
    }

}