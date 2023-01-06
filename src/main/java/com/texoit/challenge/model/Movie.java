package com.texoit.challenge.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity(name = "movie")
@Table(name = "movie")
public class Movie {

  public Movie(int year, String title, String studios, String producers, String winner) {
    super();
    this.year = year;
    this.title = title;
    this.studios = studios;
    this.producers = producers;
    this.winner = winner;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "movie_year")
  private Integer year;

  private String title;

  private String studios;

  private String producers;

  private String winner;

  public Movie() {

  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer movieYear) {
    this.year = movieYear;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStudios() {
    return studios;
  }

  public void setStudios(String studios) {
    this.studios = studios;
  }

  public String getProducers() {
    return producers;
  }

  public void setProducers(String producers) {
    this.producers = producers;
  }

  public String getWinner() {
    return winner;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }
}
