package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import proctoring.ExamType;

class ReactSelectorTest {
  private final ReactSelector selector = new ReactSelector(
      new IncidentLog(), Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC));

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
}
