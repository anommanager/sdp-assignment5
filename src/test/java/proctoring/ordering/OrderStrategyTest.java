package proctoring.ordering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import proctoring.ExamType;

class OrderStrategyTest {
  private final List<Question> questions = List.of(
      new Question("a", 3), new Question("b", 1), new Question("c", 2), new Question("d", 5));

  @Test
  void inOrderKeepsBankOrder() {
    assertEquals(questions, new InOrderStrategy().order(questions));
  }

  @Test
  void easyFirstSortsByDifficulty() {
    List<Integer> levels = new EasyFirstStrategy().order(questions).stream().map(Question::difficulty).toList();
    assertEquals(List.of(1, 2, 3, 5), levels);
  }

  @Test
  void shuffledKeepsAllQuestions() {
    List<Question> result = new ShuffledStrategy(new Random(1)).order(questions);
    assertEquals(new HashSet<>(questions), new HashSet<>(result));
    assertEquals(questions.size(), result.size());
  }

  @Test
  void shufflingDoesNotChangeInput() {
    new ShuffledStrategy(new Random(1)).order(questions);
    assertEquals("a", questions.get(0).text());
  }

  @Test
  void selectorPicksByExamType() {
    OrderSelector selector = new OrderSelector();
    assertInstanceOf(ShuffledStrategy.class, selector.select(ExamType.FINAL));
    assertInstanceOf(InOrderStrategy.class, selector.select(ExamType.PRACTICE));
    assertInstanceOf(EasyFirstStrategy.class, selector.select(ExamType.QUIZ));
  }

  @Test
  void setStrategyChangesResult() {
    QuestionOrderer orderer = new QuestionOrderer(new InOrderStrategy());
    assertEquals(questions, orderer.arrange(questions));
    orderer.setStrategy(new EasyFirstStrategy());
    assertEquals("b", orderer.arrange(questions).get(0).text());
  }
}
