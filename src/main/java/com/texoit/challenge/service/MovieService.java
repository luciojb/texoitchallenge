package com.texoit.challenge.service;

import com.texoit.challenge.model.Movie;
import com.texoit.challenge.model.MovieProducerWinner;
import com.texoit.challenge.model.WinnerInterval;
import com.texoit.challenge.model.http.AwardsIntervalResponse;
import com.texoit.challenge.repository.MovieRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
    List<WinnerInterval> intervals = new ArrayList<>();
    prods.stream().map(MovieProducerWinner::getProducer).distinct().forEach(p -> {
      var mvs = prods.stream().filter(m -> m.getProducer().equals(p)).collect(Collectors.toList());

      if (mvs.size() > 1) {
        for (int i = 1; i < mvs.size(); i++) {
          intervals.add(new WinnerInterval(
              mvs.get(i).getProducer(),
              mvs.get(i).getYear() - mvs.get(i - 1).getYear(),
              mvs.get(i - 1).getYear(),
              mvs.get(i).getYear()
          ));
        }
      }
    });

    AtomicReference<Integer> maxInterval = new AtomicReference<>(0);
    intervals.stream().max(Comparator.comparingInt(WinnerInterval::getInterval)).ifPresent(i -> maxInterval.set(i.getInterval()));
    AtomicReference<Integer> minInterval = new AtomicReference<>(0);
    intervals.stream().min(Comparator.comparingInt(WinnerInterval::getInterval)).ifPresent(i -> minInterval.set(i.getInterval()));
    List<WinnerInterval> max = intervals
        .stream()
        .filter(x -> Objects.equals(x.getInterval(), maxInterval.get()))
        .collect(Collectors.toList());
    List<WinnerInterval> min = intervals
        .stream()
        .filter(x -> Objects.equals(x.getInterval(), minInterval.get()))
        .collect(Collectors.toList());
    return new AwardsIntervalResponse(max, min);
  }


}
