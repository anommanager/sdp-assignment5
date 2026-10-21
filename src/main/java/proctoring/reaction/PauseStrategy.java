package proctoring.reaction;

import java.time.Clock;
import java.time.Instant;

public class PauseStrategy implements ReactStrategy {
  private final IncidentLog log;
  private final Clock clock;
  private final int seconds;

  public PauseStrategy(IncidentLog log, Clock clock, int seconds) {
    this.log = log;
    this.clock = clock;
    this.seconds = seconds;
  }

  @Override
  public String react(String roomId) {
    Instant resumeAt = clock.instant().plusSeconds(seconds);
    log.add("paused: room " + roomId + " until " + resumeAt);
    return "exam paused in room " + roomId + " until " + resumeAt;
  }
}
