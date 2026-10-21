package proctoring.ordering;

import java.util.Random;
import proctoring.ExamType;

public class OrderSelector {
  public OrderStrategy select(ExamType type) {
    switch (type) {
      case FINAL:
        return new ShuffledStrategy(new Random());
      case QUIZ:
        return new EasyFirstStrategy();
      default:
        return new InOrderStrategy();
    }
  }
}
