package com.texoit.challenge.model;

public class MovieProducerWinner {

  private String producer;

  private Integer year;

  private String title;

  public MovieProducerWinner(String producer, Integer year, String title) {
    this.producer = producer;
    this.year = year;
    this.title = title;
  }

  public String getProducer() {
    return producer;
  }

  public void setProducer(String producer) {
    this.producer = producer;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MovieProducerWinner that = (MovieProducerWinner) o;

    if (!getProducer().equals(that.getProducer())) {
      return false;
    }
    if (!getYear().equals(that.getYear())) {
      return false;
    }
    return getTitle().equals(that.getTitle());
  }

  @Override
  public int hashCode() {
    int result = getProducer().hashCode();
    result = 31 * result + getYear().hashCode();
    result = 31 * result + getTitle().hashCode();
    return result;
  }
}
