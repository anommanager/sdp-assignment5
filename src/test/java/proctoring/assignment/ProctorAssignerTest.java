package proctoring.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProctorAssignerTest {
  private final Proctor ann = new Proctor("Ann", 3, 4.2);
  private final Proctor bob = new Proctor("Bob", 1, 3.9);
  private final Proctor cid = new Proctor("Cid", 2, 4.8);
  private final List<Proctor> proctors = List.of(ann, bob, cid);

  @Test
  void leastLoadedPicksFewestRooms() {
    assertEquals(bob, Assigners.create("least-loaded").assign(proctors));
  }

  @Test
  void leastLoadedKeepsFirstOnTie() {
    List<Proctor> tied = List.of(new Proctor("A", 1, 1.0), new Proctor("B", 1, 5.0));
    assertEquals("A", Assigners.create("least-loaded").assign(tied).name());
  }

  @Test
  void bestRatedPicksHighestRating() {
    assertEquals(cid, Assigners.create("best-rated").assign(proctors));
  }

  @Test
  void roundRobinWalksThroughProctors() {
    ProctorAssigner assigner = Assigners.create("round-robin");
    assertEquals(ann, assigner.assign(proctors));
    assertEquals(bob, assigner.assign(proctors));
    assertEquals(cid, assigner.assign(proctors));
    assertEquals(ann, assigner.assign(proctors));
  }

  @Test
  void switchingChangesResult() {
    ProctorAssigner assigner = Assigners.create("least-loaded");
    assertEquals(bob, assigner.assign(proctors));
    Assigners.switchTo(assigner, "best-rated");
    assertEquals(cid, assigner.assign(proctors));
  }

  @ParameterizedTest
  @ValueSource(strings = {"least-loaded", "best-rated", "round-robin"})
  void everyStrategyReturnsProctorFromInputWithoutChangingIt(String name) {
    List<Proctor> input = List.of(ann, bob, cid);
    Proctor result = Assigners.create(name).assign(input);
    assertNotNull(result);
    assertEquals(true, input.contains(result));
    assertEquals(List.of(ann, bob, cid), input);
  }

  @ParameterizedTest
  @ValueSource(strings = {"least-loaded", "best-rated", "round-robin"})
  void everyStrategyRejectsEmptyList(String name) {
    assertThrows(IllegalArgumentException.class,
        () -> Assigners.create(name).assign(List.of()));
  }
}
