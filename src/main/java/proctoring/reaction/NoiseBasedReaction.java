package proctoring.reaction;

import proctoring.sensor.NoiseSensor;

public class NoiseBasedReaction implements ReactStrategy {
  static final int MEDIUM_DB = 50;
  static final int LOUD_DB = 70;

  private final NoiseSensor sensor;
  private final ReactStrategy quiet;
  private final ReactStrategy medium;
  private final ReactStrategy loud;

  public NoiseBasedReaction(NoiseSensor sensor, ReactStrategy quiet, ReactStrategy medium,
      ReactStrategy loud) {
    this.sensor = sensor;
    this.quiet = quiet;
    this.medium = medium;
    this.loud = loud;
  }

  @Override
  public String react(String roomId) {
    int level = sensor.levelDb(roomId);
    if (level >= LOUD_DB) {
      return loud.react(roomId);
    }
    if (level >= MEDIUM_DB) {
      return medium.react(roomId);
    }
    return quiet.react(roomId);
  }
}
