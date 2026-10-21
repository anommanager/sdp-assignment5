package proctoring.reaction;

import java.time.Clock;
import proctoring.ExamType;

public class ReactSelector {
  static final int PAUSE_SECONDS = 60;

  private final IncidentLog log;
  private final Clock clock;

  public ReactSelector(IncidentLog log, Clock clock) {
    this.log = log;
    this.clock = clock;
  }

  public ReactStrategy select(int violationNumber, ExamType type) {
    int level = type == ExamType.FINAL ? violationNumber + 1 : violationNumber;
    if (level <= 1) {
      return new WarnStrategy(log);
    }
    if (level == 2) {
      return new PauseStrategy(log, clock, PAUSE_SECONDS);
    }
    return new TerminateStrategy(log);
  }
}
