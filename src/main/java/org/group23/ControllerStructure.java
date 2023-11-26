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

    @GetMapping("/addRemoveQuestions/{surveyId}")
    public String addRemoveQuestionsForm(@PathVariable Long surveyId, Model model) {
        // Get the survey by ID
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (survey != null && Objects.equals(username, survey.getAuthor())) {
            model.addAttribute("survey", survey);
            return "addRemoveQuestions";
        } else {
            // Handle survey not found
            model.addAttribute(
                    "message",
                    "The survey was not found, or you do not have permission to edit it.");
            return "error";
        }
    }

    @PostMapping("/addQuestion/{surveyId}")
    public String addQuestion(
            @PathVariable Long surveyId,
            @ModelAttribute("survey") Survey survey,
            @RequestParam String questionText,
            Model model
    ) {
        // Validate length before adding to the database
        if (questionText.length() > 255) {
            // Handle the case where the question text is too long
            model.addAttribute("error", "Question text is too long. Please enter a shorter question.");
            return "error";
        }
        // Get the survey by ID to ensure we have the latest data
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Survey updatedSurvey = surveyRepository.findById(surveyId).orElse(null);
        if (updatedSurvey != null && Objects.equals(username, updatedSurvey.getAuthor())) {
            // Create a text question and add it to the survey
            TextQuestion textQuestion = new TextQuestion(questionText);
            updatedSurvey.addQuestion(textQuestion);
            surveyRepository.save(updatedSurvey);

            model.addAttribute("survey", updatedSurvey);

            return "redirect:/addRemoveQuestions/" + surveyId;
        } else {
            // Handle survey not found
            model.addAttribute(
                    "message",
                    "The survey was not found, or you do not have permission to edit it.");
            return "error";
        }
    }

    @PostMapping("/deleteQuestion/{surveyId}/{questionId}")
    public String deleteQuestion(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            Model model
    ) {
        // Get the survey by ID
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (survey != null && Objects.equals(username, survey.getAuthor())) {
            // Find and remove the question from the survey
            Question questionToRemove = survey.getQuestions().stream()
                    .filter(question -> question.getId().equals(questionId))
                    .findFirst()
                    .orElse(null);

            if (questionToRemove != null) {
                survey.removeQuestion(questionToRemove);
                surveyRepository.save(survey);
            }
            return "redirect:/addRemoveQuestions/" + survey.getId();
        } else {
            // Handle survey not found
            model.addAttribute(
                    "message",
                    "The survey was not found, or you do not have permission to edit it.");
            return "error";
        }
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

    @DeleteMapping("/deleteSurvey/{surveyId}")
    public String deleteSurvey(@PathVariable Long surveyId, Model model) {
        // Get the survey by ID
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (survey != null && Objects.equals(username, survey.getAuthor())) {
            // Delete the survey from the repository
            surveyRepository.delete(survey);
            // Redirect to the addRemoveQuestions page
            return "redirect:/createSurvey";
        } else {
            // Handle survey not found or permission issue
            model.addAttribute("message", "The survey was not found, or you do not have permission to delete it.");
            return "error";
        }
    }

    @GetMapping("/viewResults/{surveyId}")
    public String viewResults(@PathVariable Long surveyId, Model model){
        Survey survey = surveyRepository.findById(surveyId).orElse(null);

        if (survey != null) {
            model.addAttribute("survey", survey);
            return "viewResults";
        } else {
            // Handle the case where the survey is not found
            return "error";
        }
    }

}