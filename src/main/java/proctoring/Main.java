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
  private static final String ROOM = "room-7";

  public static void main(String[] args) {
    Clock clock = Clock.fixed(Instant.parse("2026-01-01T10:00:00Z"), ZoneOffset.UTC);
    IncidentLog log = new IncidentLog();
    ReactStrategy warn = new WarnStrategy(log);
    ReactStrategy pause = new PauseStrategy(log, clock, 60);
    ReactStrategy terminate = new TerminateStrategy(log);

    System.out.println("== A. one input through every reaction strategy ==");
    NoiseSensor loudRoom = new SimulatedNoiseSensor(85);
    System.out.println("noise in " + ROOM + ": " + loudRoom.levelDb(ROOM) + " dB");
    System.out.println("warn      -> " + warn.react(ROOM));
    System.out.println("pause     -> " + pause.react(ROOM));
    System.out.println("terminate -> " + terminate.react(ROOM));
    System.out.println("noise     -> "
        + new NoiseBasedReaction(loudRoom, warn, pause, terminate).react(ROOM));

    System.out.println();
    System.out.println("== B. manual choice vs selector, same situation ==");
    System.out.println("situation: second violation, " + ROOM);
    ViolationHandler handler = new ViolationHandler(pause);
    System.out.println("manual (pause)       -> " + handler.handle(ROOM));
    ReactSelector selector = new ReactSelector(log, clock, loudRoom);
    for (ExamType type : ExamType.values()) {
      handler.setStrategy(selector.select(2, type));
      System.out.println("selector " + type + " -> " + handler.handle(ROOM));
    }
    ReactSelector quietSelector = new ReactSelector(log, clock, new SimulatedNoiseSensor(30));
    handler.setStrategy(quietSelector.select(2, ExamType.QUIZ));
    System.out.println("selector QUIZ, 30 dB -> " + handler.handle(ROOM));

    System.out.println();
    System.out.println("== C. strategy swapped at runtime ==");
    handler.setStrategy(warn);
    System.out.println("1. " + handler.handle(ROOM));
    handler.setStrategy(pause);
    System.out.println("2. " + handler.handle(ROOM));
    handler.setStrategy(new NoiseBasedReaction(new SimulatedNoiseSensor(55), warn, pause, terminate));
    System.out.println("3. " + handler.handle(ROOM));
    handler.setStrategy(new NoiseBasedReaction(loudRoom, warn, pause, terminate));
    System.out.println("4. " + handler.handle(ROOM));
    System.out.println("incident log: " + log.entries().size() + " entries, last = "
        + log.entries().get(log.entries().size() - 1));

    System.out.println();
    System.out.println("== D. proctor assignment ==");
    List<Proctor> proctors = List.of(
        new Proctor("Ann", 3, 4.2), new Proctor("Bob", 1, 3.9), new Proctor("Cid", 2, 4.8));
    ProctorAssigner assigner = new ProctorAssigner(new LeastLoadedStrategy());
    System.out.println("least loaded -> " + assigner.assign(proctors).name());
    assigner.setStrategy(new BestRatedStrategy());
    System.out.println("best rated   -> " + assigner.assign(proctors).name());
    assigner.setStrategy(new RoundRobinStrategy());
    System.out.println("round robin  -> " + assigner.assign(proctors).name() + ", "
        + assigner.assign(proctors).name());
    ProctorSelector proctorSelector = new ProctorSelector();
    System.out.println("selector FINAL/20     -> "
        + new ProctorAssigner(proctorSelector.select(ExamType.FINAL, 20)).assign(proctors).name());
    System.out.println("selector PRACTICE/45  -> "
        + new ProctorAssigner(proctorSelector.select(ExamType.PRACTICE, 45)).assign(proctors).name());
    System.out.println("selector QUIZ/10      -> "
        + new ProctorAssigner(proctorSelector.select(ExamType.QUIZ, 10)).assign(proctors).name());
  }
}
