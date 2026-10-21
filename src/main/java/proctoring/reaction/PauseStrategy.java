package proctoring.reaction;

public class PauseStrategy implements ReactStrategy {
    @Override
    public String react(String roomId) {
        return "exam paused in room " + roomId;
    }
}
