package org.group23;

import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    Question findByQuestion(String question);
}
