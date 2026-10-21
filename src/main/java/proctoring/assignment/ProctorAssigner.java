package proctoring.assignment;

import java.util.List;

public class ProctorAssigner {
  private String mode;
  private int next;

  public ProctorAssigner(String mode) {
    this.mode = mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public Proctor assign(List<Proctor> proctors) {
    if (proctors.isEmpty()) {
      throw new IllegalArgumentException("no proctors");
    }
    switch (mode) {
      case "least-loaded": {
        Proctor best = proctors.get(0);
        for (Proctor p : proctors) {
          if (p.activeRooms() < best.activeRooms()) {
            best = p;
          }
        }
        return best;
      }
      case "best-rated": {
        Proctor best = proctors.get(0);
        for (Proctor p : proctors) {
          if (p.rating() > best.rating()) {
            best = p;
          }
        }
        return best;
      }
      case "round-robin": {
        Proctor chosen = proctors.get(next % proctors.size());
        next++;
        return chosen;
      }
      default:
        throw new IllegalArgumentException("unknown mode " + mode);
    }
  }
}
