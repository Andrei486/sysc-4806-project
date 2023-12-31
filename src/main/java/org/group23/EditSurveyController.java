package org.group23;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@org.springframework.stereotype.Controller
public class EditSurveyController {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    SurveyRepository surveyRepository;

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

    @PostMapping("/addQuestion/{surveyId}/text")
    public String addTextQuestion(
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

    @PostMapping("/addQuestion/{surveyId}/numerical")
    public String addNumericalQuestion(
            @PathVariable Long surveyId,
            @ModelAttribute("survey") Survey survey,
            @RequestParam String questionText,
            @RequestParam(required = false) Double minBound,  // Updated: Make bounds optional
            @RequestParam(required = false) Double maxBound,  // Updated: Make bounds optional
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
            // Create a numerical question and add it to the survey
            NumericalQuestion numericalQuestion;

            // Updated: Check if both minBound and maxBound are null, indicating no bounds specified
            if (minBound == null && maxBound == null) {
                numericalQuestion = new NumericalQuestion(questionText);
            } else {
                numericalQuestion = new NumericalQuestion(questionText, minBound, maxBound);
            }

            updatedSurvey.addQuestion(numericalQuestion);
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

    @PostMapping("/addQuestion/{surveyId}/mc")
    public String addMCQuestion(
            @PathVariable Long surveyId,
            @ModelAttribute("survey") Survey survey,
            @RequestParam String questionText,
            @RequestParam(name="mcOption") List<String> mcOptions,
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
            List<String> options = new LinkedList<>();
            // Find all MC options in parameters
            for (String option : mcOptions) {
                // Don't allow empty options
                if (!option.isBlank()) {
                    options.add(option);
                }
            }
            // TODO: should we allow MC questions with no option?
            MCQuestion mcQuestion = new MCQuestion(questionText, options);

            if (options.isEmpty()) {
                model.addAttribute("error", "Multiple-choice questions must have at least one option.");
                return "error";
            }
            updatedSurvey.addQuestion(mcQuestion);
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

    @PostMapping("/closeSurvey/{surveyId}")
    public String closeSurvey(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (survey != null && Objects.equals(username, survey.getAuthor())){
            survey.setOpen(false);
            surveyRepository.save(survey);
            return "redirect:/addRemoveQuestions/" + survey.getId();
        } else {
            // Handle survey not found
            model.addAttribute(
                    "message",
                    "The survey was not found, or you do not have permission to edit it.");
            return "error";
        }
    }

    @PostMapping("/openSurvey/{surveyId}")
    public String openSurvey(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (survey != null && Objects.equals(username, survey.getAuthor())){
            survey.setOpen(true);
            surveyRepository.save(survey);
            return "redirect:/addRemoveQuestions/" + survey.getId();
        } else {
            // Handle survey not found
            model.addAttribute(
                    "message",
                    "The survey was not found, or you do not have permission to edit it.");
            return "error";
        }
    }
}
