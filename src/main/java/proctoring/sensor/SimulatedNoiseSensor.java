package proctoring.sensor;

public class SimulatedNoiseSensor implements NoiseSensor {
  private static final int DEFAULT_DB = 45;

  private final int levelDb;

  public SimulatedNoiseSensor() {
    this(DEFAULT_DB);
  }

  public SimulatedNoiseSensor(int levelDb) {
    this.levelDb = levelDb;
  }

  @Override
  public int levelDb(String roomId) {
    return levelDb;
  }
}
