package proctoring.reaction;

public class WarnStrategy implements ReactStrategy {
  private final IncidentLog log;

  public WarnStrategy(IncidentLog log) {
    this.log = log;
  }

  @Override
  public String react(String roomId) {
    log.add("warning: room " + roomId);
    return "WARNING: room " + roomId + ", violation logged";
  }
}
