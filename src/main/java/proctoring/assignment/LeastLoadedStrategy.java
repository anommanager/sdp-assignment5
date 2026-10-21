package proctoring.assignment;

import java.util.Comparator;
import java.util.List;

public class LeastLoadedStrategy implements ProctorStrategy {
  @Override
  public Proctor pick(List<Proctor> proctors) {
    return proctors.stream()
        .min(Comparator.comparingInt(Proctor::activeRooms))
        .orElseThrow(() -> new IllegalArgumentException("no proctors"));
  }
}
