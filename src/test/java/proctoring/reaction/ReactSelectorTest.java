package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import proctoring.ExamType;
import proctoring.sensor.FakeNoiseSensor;

class ReactSelectorTest {
  private final FakeNoiseSensor sensor = new FakeNoiseSensor(0);
  private final ReactSelector selector = new ReactSelector(
      new IncidentLog(), Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC), sensor);

  @Test
  void practiceEscalatesWithViolationNumber() {
    assertInstanceOf(WarnStrategy.class, selector.select(1, ExamType.PRACTICE));
    assertInstanceOf(PauseStrategy.class, selector.select(2, ExamType.PRACTICE));
    assertInstanceOf(TerminateStrategy.class, selector.select(3, ExamType.PRACTICE));
    assertInstanceOf(TerminateStrategy.class, selector.select(10, ExamType.PRACTICE));
  }

  @Test
  void zeroViolationsStillWarns() {
    assertInstanceOf(WarnStrategy.class, selector.select(0, ExamType.PRACTICE));
    assertInstanceOf(WarnStrategy.class, selector.select(0, ExamType.FINAL));
  }

  @Test
  void finalIsOneStepStricter() {
    assertInstanceOf(PauseStrategy.class, selector.select(1, ExamType.FINAL));
    assertInstanceOf(TerminateStrategy.class, selector.select(2, ExamType.FINAL));
    assertInstanceOf(TerminateStrategy.class, selector.select(3, ExamType.FINAL));
  }

  @Test
  void quizIgnoresViolationNumberAndFollowsNoise() {
    sensor.setLevelDb(30);
    assertEquals("WARNING: room r1, violation logged",
        selector.select(1, ExamType.QUIZ).react("r1"));
    assertEquals("WARNING: room r1, violation logged",
        selector.select(5, ExamType.QUIZ).react("r1"));
    sensor.setLevelDb(60);
    assertEquals("exam paused in room r1 until 2026-01-01T10:01:00Z",
        selector.select(1, ExamType.QUIZ).react("r1"));
    sensor.setLevelDb(80);
    assertEquals("exam terminated in room r1", selector.select(1, ExamType.QUIZ).react("r1"));
  }

  @Test
  void quizGetsNoiseBasedStrategy() {
    assertInstanceOf(NoiseBasedReaction.class, selector.select(1, ExamType.QUIZ));
  }
}
