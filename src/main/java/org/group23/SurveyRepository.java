package org.group23;

import org.springframework.data.repository.CrudRepository;

public interface SurveyRepository extends CrudRepository<Survey, Long> {

    Survey findByName(String name);
}
