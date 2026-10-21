package proctoring.assignment;

import proctoring.ExamType;

public class ProctorSelector {
  static final int LARGE_ROOM = 30;

  private final ProctorStrategy roundRobin = new RoundRobinStrategy();

  public ProctorStrategy select(ExamType type, int students) {
    if (type == ExamType.FINAL) {
      return new BestRatedStrategy();
    }
    if (students > LARGE_ROOM) {
      return new LeastLoadedStrategy();
    }
    return roundRobin;
  }
}
