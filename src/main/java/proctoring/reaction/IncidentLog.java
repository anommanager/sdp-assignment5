package proctoring.reaction;

import java.util.ArrayList;
import java.util.List;

public class IncidentLog {
  private final List<String> entries = new ArrayList<>();

  public void add(String entry) {
    entries.add(entry);
  }

  public List<String> entries() {
    return List.copyOf(entries);
  }
}
