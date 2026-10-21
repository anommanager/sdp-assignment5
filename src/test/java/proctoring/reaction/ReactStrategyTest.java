package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReactStrategyTest {
  private static final Instant NOW = Instant.parse("2026-01-01T10:00:00Z");

  private final IncidentLog log = new IncidentLog();
  private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);

  @Test
  void warnReturnsWarningAndWritesToLog() {
    assertEquals("WARNING: room r1, violation logged", new WarnStrategy(log).react("r1"));
    assertEquals(List.of("warning: room r1"), log.entries());
  }

  @Test
  void pauseReturnsResumeTime() {
    String result = new PauseStrategy(log, clock, 90).react("r1");
    assertEquals("exam paused in room r1 until 2026-01-01T10:01:30Z", result);
    assertEquals(List.of("paused: room r1 until 2026-01-01T10:01:30Z"), log.entries());
  }

  @Test
  void pauseLengthComesFromConstructor() {
    String result = new PauseStrategy(log, clock, 5).react("r1");
    assertEquals("exam paused in room r1 until 2026-01-01T10:00:05Z", result);
  }

  @Test
  void terminateEndsExamAndNotifiesTeacher() {
    assertEquals("exam terminated in room r1", new TerminateStrategy(log).react("r1"));
    assertEquals(List.of("teacher notified: room r1"), log.entries());
  }

  @Test
  void setStrategyChangesResult() {
    ViolationHandler handler = new ViolationHandler(new WarnStrategy(log));
    String before = handler.handle("r1");
    handler.setStrategy(new TerminateStrategy(log));
    assertNotEquals(before, handler.handle("r1"));
    assertEquals("exam terminated in room r1", handler.handle("r1"));
  }
}
