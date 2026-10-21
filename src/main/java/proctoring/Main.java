package proctoring;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import proctoring.assignment.BestRatedStrategy;
import proctoring.assignment.LeastLoadedStrategy;
import proctoring.assignment.Proctor;
import proctoring.assignment.ProctorAssigner;
import proctoring.assignment.ProctorSelector;
import proctoring.assignment.RoundRobinStrategy;
import proctoring.reaction.IncidentLog;
import proctoring.reaction.NoiseBasedReaction;
import proctoring.reaction.PauseStrategy;
import proctoring.reaction.ReactSelector;
import proctoring.reaction.ReactStrategy;
import proctoring.reaction.TerminateStrategy;
import proctoring.reaction.ViolationHandler;
import proctoring.reaction.WarnStrategy;
import proctoring.sensor.NoiseSensor;
import proctoring.sensor.SimulatedNoiseSensor;

public class Main {
  public static void main(String[] args) {
    String room = "room-7";
    Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    IncidentLog log = new IncidentLog();
    NoiseSensor sensor = new SimulatedNoiseSensor(85);

    ReactStrategy warn = new WarnStrategy(log);
    ReactStrategy pause = new PauseStrategy(log, clock, 60);
    ReactStrategy terminate = new TerminateStrategy(log);

    ViolationHandler handler = new ViolationHandler(warn);
    System.out.println(handler.handle(room));
    handler.setStrategy(pause);
    System.out.println(handler.handle(room));
    handler.setStrategy(new NoiseBasedReaction(sensor, warn, pause, terminate));
    System.out.println(handler.handle(room));

    ReactSelector selector = new ReactSelector(log, clock, sensor);
    for (ExamType type : ExamType.values()) {
      handler.setStrategy(selector.select(2, type));
      System.out.println(type + ": " + handler.handle(room));
    }

    List<Proctor> proctors = List.of(
        new Proctor("Ann", 3, 4.2), new Proctor("Bob", 1, 3.9), new Proctor("Cid", 2, 4.8));
    ProctorAssigner assigner = new ProctorAssigner(new LeastLoadedStrategy());
    System.out.println(assigner.assign(proctors).name());
    assigner.setStrategy(new BestRatedStrategy());
    System.out.println(assigner.assign(proctors).name());
    assigner.setStrategy(new RoundRobinStrategy());
    System.out.println(assigner.assign(proctors).name());
    assigner.setStrategy(new ProctorSelector().select(ExamType.FINAL, 20));
    System.out.println(assigner.assign(proctors).name());
  }
}
