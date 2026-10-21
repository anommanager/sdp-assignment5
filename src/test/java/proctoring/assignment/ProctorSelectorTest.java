package proctoring.assignment;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import proctoring.ExamType;

class ProctorSelectorTest {
  private final ProctorSelector selector = new ProctorSelector();

  @Test
  void finalAlwaysGetsBestRated() {
    assertInstanceOf(BestRatedStrategy.class, selector.select(ExamType.FINAL, 5));
    assertInstanceOf(BestRatedStrategy.class, selector.select(ExamType.FINAL, 200));
  }

  @Test
  void largePracticeGetsLeastLoaded() {
    assertInstanceOf(LeastLoadedStrategy.class, selector.select(ExamType.PRACTICE, 31));
  }

  @Test
  void largeQuizGetsLeastLoaded() {
    assertInstanceOf(LeastLoadedStrategy.class, selector.select(ExamType.QUIZ, 31));
  }

  @Test
  void roomOfThirtyIsStillSmall() {
    assertInstanceOf(RoundRobinStrategy.class, selector.select(ExamType.PRACTICE, 30));
    assertInstanceOf(RoundRobinStrategy.class, selector.select(ExamType.QUIZ, 30));
  }

  @Test
  void emptyRoomIsSmall() {
    assertInstanceOf(RoundRobinStrategy.class, selector.select(ExamType.QUIZ, 0));
  }

  @Test
  void roundRobinKeepsItsPositionBetweenCalls() {
    assertSame(selector.select(ExamType.PRACTICE, 10), selector.select(ExamType.QUIZ, 20));
  }
}
