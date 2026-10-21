package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import proctoring.sensor.FakeNoiseSensor;

class NoiseBasedReactionTest {
  private final IncidentLog log = new IncidentLog();
  private final FakeNoiseSensor sensor = new FakeNoiseSensor(0);
  private final NoiseBasedReaction reaction = new NoiseBasedReaction(
      sensor,
      new WarnStrategy(log),
      new PauseStrategy(log, Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC), 60),
      new TerminateStrategy(log));

  @Test
  void quietRoomGetsWarning() {
    sensor.setLevelDb(30);
    assertEquals("WARNING: room r1, violation logged", reaction.react("r1"));
  }

  @Test
  void mediumNoisePausesExam() {
    sensor.setLevelDb(60);
    assertEquals("exam paused in room r1 until 2026-01-01T10:01:00Z", reaction.react("r1"));
  }

  @Test
  void loudRoomTerminatesExam() {
    sensor.setLevelDb(90);
    assertEquals("exam terminated in room r1", reaction.react("r1"));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
      "0|WARNING: room r1, violation logged",
      "49|WARNING: room r1, violation logged",
      "50|exam paused in room r1 until 2026-01-01T10:01:00Z",
      "69|exam paused in room r1 until 2026-01-01T10:01:00Z",
      "70|exam terminated in room r1",
      "120|exam terminated in room r1"})
  void thresholdsAreInclusiveAtLowerEdge(int db, String expected) {
    sensor.setLevelDb(db);
    assertEquals(expected, reaction.react("r1"));
  }
}
