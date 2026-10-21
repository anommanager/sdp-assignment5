package proctoring.sensor;

public class SimulatedNoiseSensor implements NoiseSensor {
    private static final int LEVEL_DB = 45;

    @Override
    public int levelDb(String roomId) {
        return LEVEL_DB;
    }
}
