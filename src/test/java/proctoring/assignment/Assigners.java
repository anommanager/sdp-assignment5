package proctoring.assignment;

final class Assigners {
  private Assigners() {
  }

  static ProctorAssigner create(String name) {
    return new ProctorAssigner(strategy(name));
  }

  static void switchTo(ProctorAssigner assigner, String name) {
    assigner.setStrategy(strategy(name));
  }

  private static ProctorStrategy strategy(String name) {
    return switch (name) {
      case "least-loaded" -> new LeastLoadedStrategy();
      case "best-rated" -> new BestRatedStrategy();
      case "round-robin" -> new RoundRobinStrategy();
      default -> throw new IllegalArgumentException("unknown " + name);
    };
  }
}
