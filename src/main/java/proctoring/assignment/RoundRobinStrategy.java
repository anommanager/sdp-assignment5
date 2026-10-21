package proctoring.assignment;

import java.util.List;

public class RoundRobinStrategy implements ProctorStrategy {
  private int next;

  @Override
  public Proctor pick(List<Proctor> proctors) {
    if (proctors.isEmpty()) {
      throw new IllegalArgumentException("no proctors");
    }
    Proctor chosen = proctors.get(next % proctors.size());
    next++;
    return chosen;
  }
}
