package proctoring.reaction;

public class WarnStrategy implements ReactStrategy {
  @Override
  public String react(String roomId) {
    return "LOG: warning for room " + roomId;
  }
}
