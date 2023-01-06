package com.texoit.challenge.service;

import com.texoit.challenge.model.Movie;
import com.texoit.challenge.model.MovieProducerWinner;
import com.texoit.challenge.model.WinnerInterval;
import com.texoit.challenge.model.http.AwardsIntervalResponse;
import com.texoit.challenge.repository.MovieRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Lúcio José Beirão
 * @since : 05/01/2023
 **/
@Service
public class MovieService {

  MovieRepository movieRepository;

  @Autowired
  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public Iterable<Movie> getAll() {
    return movieRepository.findAll();
  }

  public Optional<Movie> getById(Long id) {
    return movieRepository.findById(id);
  }

  public Movie add(Movie movie) {
    return movieRepository.save(movie);
  }

  public void delete(Long id) {
    movieRepository.deleteById(id);
  }

  public Movie update(Movie movie) {
    return movieRepository.save(movie);
  }

  public AwardsIntervalResponse getWinnersIntervals() {
    var winners = movieRepository.findByWinnerEqualsOrderByYearAsc("yes");

    var prods = winners.stream()
        .map(m -> Arrays
            .stream(m.getProducers().replaceAll(" and ", ",").split(","))
            .filter(p -> p.trim().length() > 0)
            .map(prod -> new MovieProducerWinner(prod.trim(), m.getYear(), m.getTitle()))
        )
        .flatMap(Stream::distinct)
        .collect(Collectors.toList());
    List<WinnerInterval> maxIntervals = new ArrayList<>();
    List<WinnerInterval> minIntervals = new ArrayList<>();
    prods.stream().map(MovieProducerWinner::getProducer).distinct().forEach(p -> {
      var mvs = prods.stream().filter(m -> m.getProducer().equals(p)).collect(Collectors.toList());
      int biggestInterval = 0;
      int minInterval = 0;
      WinnerInterval maxWit = null;
      WinnerInterval minWit = null;
      int currInterval;
      if (mvs.size() > 1) {
        for (int i = 1; i < mvs.size(); i++) {
          currInterval = mvs.get(i).getYear() - mvs.get(i - 1).getYear();
          if (biggestInterval == 0 || currInterval > biggestInterval) {
            biggestInterval = currInterval;
            maxWit = new WinnerInterval(mvs.get(i).getProducer(), biggestInterval, mvs.get(i - 1).getYear(), mvs.get(i).getYear());
          }
          if (minInterval == 0 || currInterval < minInterval) {
            minInterval = currInterval;
            minWit = new WinnerInterval(mvs.get(i).getProducer(), minInterval, mvs.get(i - 1).getYear(), mvs.get(i).getYear());
          }
        }
        maxIntervals.add(maxWit);
        minIntervals.add(minWit);

      }
    });

    return new AwardsIntervalResponse(maxIntervals, minIntervals);
  }


}
