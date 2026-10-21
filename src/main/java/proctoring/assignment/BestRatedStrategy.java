package proctoring.assignment;

import java.util.Comparator;
import java.util.List;

public class BestRatedStrategy implements ProctorStrategy {
  @Override
  public Proctor pick(List<Proctor> proctors) {
    return proctors.stream()
        .max(Comparator.comparingDouble(Proctor::rating))
        .orElseThrow(() -> new IllegalArgumentException("no proctors"));
  }
}
