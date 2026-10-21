package proctoring.sensor;

public class FakeNoiseSensor implements NoiseSensor {
  private int levelDb;

  public FakeNoiseSensor(int levelDb) {
    this.levelDb = levelDb;
  }

  public void setLevelDb(int levelDb) {
    this.levelDb = levelDb;
  }

  @Override
  public int levelDb(String roomId) {
    return levelDb;
  }
}
