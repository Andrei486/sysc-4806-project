package org.group23;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
public class ControllerStructure {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    SurveyRepository surveyRepository;

    @GetMapping("/hello")
    public String sayHello(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "helloPage";
    }


    //These methods were for testing CrudRepositories and should definitely be changed ASAP :)


    @PostMapping("/createSurvey")
    public String createSurvey(Model model){
        Survey survey = new Survey("Survey 1");
        ArrayList<Question> questions = new ArrayList<>();
        TextQuestion question = new TextQuestion("What's your name?");
        question.addAnswer("Jeff");
        question.addAnswer("Jimmy");
        questions.add(question);
        questions.add(new TextQuestion("What's your favorite color?"));
        survey.setQuestions(questions);
        surveyRepository.save(survey);
        model.addAttribute("message", "Hello, World!");
        return "helloPage";
    }

    @GetMapping("/getSurvey")
    public String getSurvey(Model model){
        Survey survey = surveyRepository.findByName("Survey 1");
        //System.out.println(survey);
        model.addAttribute("message", "Hello, World!");
        return "helloPage";
    }

}
