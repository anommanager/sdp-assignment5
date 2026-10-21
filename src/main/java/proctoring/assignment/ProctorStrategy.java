package proctoring.assignment;

import java.util.List;

public interface ProctorStrategy {
  Proctor pick(List<Proctor> proctors);
}
