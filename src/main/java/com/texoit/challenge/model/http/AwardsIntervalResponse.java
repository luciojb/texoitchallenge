package com.texoit.challenge.model.http;

import com.texoit.challenge.model.WinnerInterval;
import java.util.List;

public class AwardsIntervalResponse {

  private List<WinnerInterval> max;

  private List<WinnerInterval> min;


  public AwardsIntervalResponse() {

  }

  public AwardsIntervalResponse(List<WinnerInterval> max, List<WinnerInterval> min) {
    this.max = max;
    this.min = min;
  }

  public List<WinnerInterval> getMax() {
    return max;
  }

  public void setMax(List<WinnerInterval> max) {
    this.max = max;
  }

  public List<WinnerInterval> getMin() {
    return min;
  }

  public void setMin(List<WinnerInterval> min) {
    this.min = min;
  }
}
