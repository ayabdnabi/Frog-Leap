# Frog-Leap
A Java implementation of Freddy the Frog's pathfinding algorithm through hexagonal ponds with varying cell types (lilypads, alligators, mud, etc.). Uses priority queues and stacks to determine optimal paths while avoiding hazards and collecting flies.

Key Features
- Hexagonal grid navigation with 6-directional movement
- Priority-based pathfinding accounting for cell types and leap mechanics
- Custom ADT implementations:
- ArrayUniquePriorityQueue (array-based)
- FrogPath (stack-based traversal)
- Graphical visualization of the frog's path

## How to run?
Prerequisites
- Java JDK 8+
- Pond configuration files (provided)

### Running the program
Download all files
Compile all Java files:
- javac *.java
Run with a pond file:
- java FrogPath pond1.txt

There are 9 pond paths, so just replace pond1 with pond2, pond3, and so on. Watch Freddy as he tries to reach his partner!
