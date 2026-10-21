package proctoring.ordering;

import java.util.List;

public class QuestionOrderer {
  private OrderStrategy strategy;

  public QuestionOrderer(OrderStrategy strategy) {
    this.strategy = strategy;
  }

  public void setStrategy(OrderStrategy strategy) {
    this.strategy = strategy;
  }

  public List<Question> arrange(List<Question> questions) {
    return strategy.order(questions);
  }
}
