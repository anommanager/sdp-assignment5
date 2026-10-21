# Exam Proctoring: Strategy Pattern

The program supervises online exams. Two jobs can be done in several ways, so each job has its own family of strategies:

- **Reaction** to a violation: warn, pause, terminate, or decide by room noise.
- **Proctor assignment**: least loaded, best rated, round robin.

## UML
![diagram](docs/uml.svg)

## Classes

| Class | What it does |
|---|---|
| `ReactStrategy` | Interface with one method that reacts to a violation in a room. |
| `WarnStrategy` | Writes the warning into the incident log, then returns the warning text. |
| `PauseStrategy` | Pauses the exam for a set number of seconds, then returns the resume time. |
| `TerminateStrategy` | Ends the exam, adds a teacher notification to the incident log. |
| `NoiseBasedReaction` | Reads the noise level from a sensor, then delegates to one of three reactions. |
| `ViolationHandler` | Context that holds one react strategy, can swap it with `setStrategy()`. |
| `ReactSelector` | Picks a react strategy from violation number plus exam type; quiz exams follow the noise. |
| `IncidentLog` | Stores incident entries written by the reaction strategies. |
| `ProctorStrategy` | Interface with one method that picks a proctor from a list. |
| `LeastLoadedStrategy` | Picks the proctor with the fewest active rooms. |
| `BestRatedStrategy` | Picks the proctor with the highest rating. |
| `RoundRobinStrategy` | Walks through the proctors in turn, remembering its position between calls. |
| `ProctorAssigner` | Context that holds one proctor strategy, can swap it with `setStrategy()`. |
| `ProctorSelector` | Picks a proctor strategy: final exams best rated, rooms over 30 students least loaded, others round robin. |
| `Proctor` | Data of one proctor: name, active rooms, rating. |
| `ExamType` | Lists the exam types. |
| `NoiseSensor` | Interface that hides the source of the noise level. |
| `SimulatedNoiseSensor` | Gives a fixed noise level chosen at construction. |
| `Main` | Demo: all strategies directly, manual versus selector choice, swap at runtime. |

## Why Strategy

Both families answer the same question in several ways: given the same input, produce a result with different logic. A warning, a pause and a termination differ in what they change in the system, not in what they are for. The caller wants one thing, "react to this violation", and does not care how it happens. That is the shape of Strategy: one small interface, many interchangeable implementations, one context that holds the chosen one.

It is not **State**. In State the object moves itself from one behaviour to another, and the states know about each other. Here nothing inside a strategy decides that the next violation needs a different strategy. The outside world decides: the teacher through `setStrategy()`, or `ReactSelector` from the exam type and the violation number. Strategies do not know their neighbours, and one can be removed without touching the rest.

It is not **Template Method**. Template Method fixes the skeleton of an algorithm in a base class and lets subclasses fill in steps, so the choice is made at compile time through inheritance. Our algorithms share no steps: warn writes a log line, pause computes a time, terminate notifies a teacher, the noise reaction reads a sensor. There is no common skeleton to put in a base class. With Strategy the choice is an object, so it can change while the program runs, as the demo does in part C.

Strategy also keeps the decision code in one place. The only `if` and `switch` logic that picks an algorithm lives in the two selectors. The contexts have none, and each strategy can be tested alone with fake data, such as `FakeNoiseSensor` in the tests.

## Refactoring

The proctor assignment family was first written as one class with a `switch` over a mode name, then moved to Strategy. The same `ProctorAssignerTest` passes on both versions; only the small test helper `Assigners` changed, because the constructor differs.

- Before (switch in one class): [ba9cc4cd9a71c0d6e9908ba5e2ea3d85f2cc5e37](https://github.com/anommanager/sdp-assignment5/commit/ba9cc4cd9a71c0d6e9908ba5e2ea3d85f2cc5e37)
- After (Strategy): [f976b24dc2d42e62b9f12371c04fbadf277b8c1d](https://github.com/anommanager/sdp-assignment5/commit/f976b24dc2d42e62b9f12371c04fbadf277b8c1d)

## Run

```
./gradlew test
./gradlew run
```
