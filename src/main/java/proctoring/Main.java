package proctoring;

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

    System.out.println("noise: " + new SimulatedNoiseSensor().levelDb("room-7") + " dB");
  }
}
