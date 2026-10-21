package proctoring.reaction;

import java.time.Clock;
import proctoring.ExamType;
import proctoring.sensor.NoiseSensor;

public class ReactSelector {
  static final int PAUSE_SECONDS = 60;

  private final IncidentLog log;
  private final Clock clock;
  private final NoiseSensor sensor;

  public ReactSelector(IncidentLog log, Clock clock, NoiseSensor sensor) {
    this.log = log;
    this.clock = clock;
    this.sensor = sensor;
  }

  public ReactStrategy select(int violationNumber, ExamType type) {
    if (type == ExamType.QUIZ) {
      return new NoiseBasedReaction(sensor, warn(), pause(), terminate());
    }
    int level = type == ExamType.FINAL ? violationNumber + 1 : violationNumber;
    if (level <= 1) {
      return warn();
    }
    if (level == 2) {
      return pause();
    }
    return terminate();
  }

  private ReactStrategy warn() {
    return new WarnStrategy(log);
  }

  private ReactStrategy pause() {
    return new PauseStrategy(log, clock, PAUSE_SECONDS);
  }

  private ReactStrategy terminate() {
    return new TerminateStrategy(log);
  }
}
