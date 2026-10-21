package proctoring.reaction;

public class TerminateStrategy implements ReactStrategy {
  @Override
  public String react(String roomId) {
    return "exam terminated in room " + roomId + ", teacher notified";
  }
}
