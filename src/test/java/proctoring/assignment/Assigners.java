package proctoring.assignment;

final class Assigners {
  private Assigners() {
  }

  static ProctorAssigner create(String name) {
    return new ProctorAssigner(name);
  }

  static void switchTo(ProctorAssigner assigner, String name) {
    assigner.setMode(name);
  }
}
