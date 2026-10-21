package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import proctoring.sensor.FakeNoiseSensor;

class ReactFamilyTest {
  private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);

  static Stream<ReactStrategy> allStrategies() {
    IncidentLog log = new IncidentLog();
    ReactStrategy warn = new WarnStrategy(log);
    ReactStrategy pause = new PauseStrategy(log, CLOCK, 60);
    ReactStrategy terminate = new TerminateStrategy(log);
    return Stream.of(warn, pause, terminate,
        new NoiseBasedReaction(new FakeNoiseSensor(60), warn, pause, terminate));
  }

  @ParameterizedTest
  @MethodSource("allStrategies")
  void everyStrategyReturnsTextAndKeepsInputIntact(ReactStrategy strategy) {
    String room = new String("room-1");
    String result = strategy.react(room);
    assertNotNull(result);
    assertFalse(result.isBlank());
    assertEquals("room-1", room);
  }

  @ParameterizedTest
  @MethodSource("allStrategies")
  void everyStrategyMentionsTheRoom(ReactStrategy strategy) {
    assertTrue(strategy.react("room-9").contains("room-9"));
  }
}
