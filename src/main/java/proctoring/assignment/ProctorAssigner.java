package proctoring.assignment;

import java.util.List;

public class ProctorAssigner {
  private ProctorStrategy strategy;

  public ProctorAssigner(ProctorStrategy strategy) {
    this.strategy = strategy;
  }

  public void setStrategy(ProctorStrategy strategy) {
    this.strategy = strategy;
  }

  public Proctor assign(List<Proctor> proctors) {
    return strategy.pick(proctors);
  }
}
