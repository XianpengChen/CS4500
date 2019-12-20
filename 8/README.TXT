This README was created to document the changes between our last week's assignment and the current one.
We made some significant changes to our code base this week.

We moved the logic for checking if a worker is out of bounds out of the board component.
We also refactored directions into an enum, instead of just passing around a string. We made this
decision as the instructors have made hints that maybe a notion of moving might change in the future.

Our Referee component design for the week can be found in the Design directory.
Our components for the implementation portion of the assignment all live in the Player directory.
This consists of DiagonalStrategy, GeometricDistanceStrategy, and the abstract class that they both
inherit, NotLoseStrategy.

This week we also created a unit testing script, which can be found under the Tests directory. The
file can be run directly from the command line with the command "./testme".