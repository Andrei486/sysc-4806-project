package org.group23;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class SurveyAnswerController {

    private final String ANSWER_ID_PREFIX = "question_";

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SurveyRepository surveyRepository;

    @GetMapping("/answerSurvey/{surveyId}")
    public String showAnswerQuestions(@PathVariable Long surveyId, Model model) {
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        if (survey.isOpen() && survey != null) {
            model.addAttribute("survey", survey);
            return "answerSurvey";
        } else if (!survey.isOpen() && survey != null) {
            model.addAttribute("message", "The survey is not currently accepting responses");
            return "error";
        } else {
            model.addAttribute("message", "The survey was not found.");
            return "error";
        }
    }

    @PostMapping("/answerSurvey/{surveyId}/submit")
    public String submitAnswers(
            @PathVariable Long surveyId,
            @RequestParam Map<String, String> allParameters,
            Model model
    ) {
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        if (survey == null) {
            model.addAttribute("message", "The survey was not found.");
            return "error";
        } else {
            // Loop over all parameters that correspond to question answers
            allParameters.keySet().stream().filter(s -> s.startsWith(ANSWER_ID_PREFIX)).forEach(questionIdString -> {
                long questionId = getQuestionIdFromString(questionIdString);
                Question question = survey.getQuestions().stream()
                        .filter(q -> q.getId() == questionId)
                        .findFirst()
                        .orElse(null);
                if (question == null) {
                    // Ignore if there is no question matching the ID in the survey
                    return; // Only ends the current iteration in foreach()
                }
                question.addAnswer(allParameters.get(questionIdString));
                questionRepository.save(question);
            });
            surveyRepository.save(survey);
        }
        model.addAttribute("survey", survey);
        return "redirect:/answerSurvey/" + surveyId;
    }

    private long getQuestionIdFromString(String questionIdString) {
        if (!questionIdString.startsWith(ANSWER_ID_PREFIX)) {
            throw new IllegalArgumentException(
                    String.format("String %s does not correspond to a question ID.", questionIdString));
        }
        String[] splits = questionIdString.split("_");
        return Long.parseLong(splits[splits.length-1]);
    }
}
