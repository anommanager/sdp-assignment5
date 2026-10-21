package proctoring;

import java.util.List;
import proctoring.ordering.OrderSelector;
import proctoring.ordering.Question;
import proctoring.ordering.QuestionOrderer;
import proctoring.reaction.ReactSelector;
import proctoring.reaction.ViolationHandler;
import proctoring.reaction.WarnStrategy;
import proctoring.sensor.SimulatedNoiseSensor;

public class Main {
  public static void main(String[] args) {
    ReactSelector reactSelector = new ReactSelector();
    ViolationHandler handler = new ViolationHandler(new WarnStrategy());
    for (ExamType type : ExamType.values()) {
      System.out.println(type);
      for (int violation = 1; violation <= 3; violation++) {
        handler.setStrategy(reactSelector.select(violation, type));
        System.out.println("  violation " + violation + ": " + handler.handle("room-7"));
      }
    }

    List<Question> questions = List.of(
        new Question("Explain Strategy", 3),
        new Question("What is an interface?", 1),
        new Question("Compare State and Strategy", 5),
        new Question("What is a class?", 2));
    OrderSelector orderSelector = new OrderSelector();
    QuestionOrderer orderer = new QuestionOrderer(orderSelector.select(ExamType.PRACTICE));
    for (ExamType type : ExamType.values()) {
      orderer.setStrategy(orderSelector.select(type));
      System.out.println(type + " order:");
      for (Question q : orderer.arrange(questions)) {
        System.out.println("  " + q.difficulty() + " " + q.text());
      }
    }

    System.out.println("noise: " + new SimulatedNoiseSensor().levelDb("room-7") + " dB");
  }
}
