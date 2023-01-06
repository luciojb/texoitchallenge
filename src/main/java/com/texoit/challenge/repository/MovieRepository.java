package com.texoit.challenge.repository;

import com.texoit.challenge.model.Movie;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


public interface MovieRepository extends CrudRepository<Movie, Long> {

  List<Movie> findByWinnerEqualsOrderByYearAsc(String winner);
}
