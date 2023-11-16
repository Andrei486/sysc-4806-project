package org.group23;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NumericalQuestion extends Question{

    public NumericalQuestion() {
        super();
    }

    public NumericalQuestion(String question){
        super(question);
    }
}
