package proctoring.reaction;

public class TerminateStrategy implements ReactStrategy {
  private final IncidentLog log;

  public TerminateStrategy(IncidentLog log) {
    this.log = log;
  }

  @Override
  public String react(String roomId) {
    log.add("teacher notified: room " + roomId);
    return "exam terminated in room " + roomId;
  }
}
