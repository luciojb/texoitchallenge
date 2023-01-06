package com.texoit.challenge.controller;

import com.texoit.challenge.model.Movie;
import com.texoit.challenge.model.http.AwardsIntervalResponse;
import com.texoit.challenge.service.MovieService;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

  private final MovieService movieService;
  private static final Logger LOG = LoggerFactory.getLogger(MovieController.class);

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping
  public @ResponseBody
  Iterable<Movie> findAll() {
    return movieService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
    try {
      var movie = movieService.getById(id);

      return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      LOG.error("Problem getting movie awards.", e);
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping
  public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
    try {
      var added = movieService.add(movie);
      return ResponseEntity.created(URI.create(String.format("/movies/%d", added.getId()))).body(added);
    } catch (Exception e) {
      LOG.error("Problem saving movie award.", e);
      return ResponseEntity.internalServerError().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
    try {
      var existing = movieService.getById(id);
      if (existing.isPresent()) {
        var old = existing.get();
        old.setProducers(movie.getProducers());
        old.setStudios(movie.getStudios());
        old.setTitle(movie.getTitle());
        old.setYear(movie.getYear());
        old.setWinner(movie.getWinner());
        var added = movieService.update(old);
        return ResponseEntity.created(URI.create(String.format("/movies/%d", added.getId()))).body(added);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      LOG.error("Problem updating movie award.", e);
      return ResponseEntity.internalServerError().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
    try {
      var existing = movieService.getById(id);
      if (existing.isPresent()) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      LOG.error("Problem deleting movie award.", e);
      return ResponseEntity.internalServerError().build();
    }
  }

  @GetMapping("/winners-intervals")
  public ResponseEntity<AwardsIntervalResponse> getAwardIntervals() {
    try {
      return ResponseEntity.ok(movieService.getWinnersIntervals());
    } catch (Exception e) {
      LOG.error("Problem getting winners intervals", e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
