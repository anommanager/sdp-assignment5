package proctoring.reaction;

public class ViolationHandler {
    private ReactStrategy strategy;

    public ViolationHandler(ReactStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ReactStrategy strategy) {
        this.strategy = strategy;
    }

    public String handle(String roomId) {
        return strategy.react(roomId);
    }
}
