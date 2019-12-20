# Santorini

This includes all the necessary source code for our version of [santorini](http://www.ccs.neu.edu/home/matthias/4500-f18/santorini.html). We have written it in Java 8 and have been utilizing `maven` for our package management and build system. 

## Project Structure

### `Admin`

this is where referee Interface and class are located. you can also find unit test for referee there.

### `Player`

This is where all code related to Player classes belongs. This includes the IPlayer interface which details the behavior of a player, and
several "Strategy" classes. A Strategy is how an AIPlayer (a version of IPlayer where a computer makes the decisions) evaluates
it's moves and picks one. Unit test is there too.

### `Common`

As the name suggests, here is where all the common data definitions lie. This includes any dependant exceptions, our data model, board implementation, or rule implementation. 

### `Design`

All specific design documentation goes here. You will find our design planning session's end up here, as well as mocking out initial classes and interfaces for each weekly assignment.

### `Lib`

This is where all the library code we may be dealing with such as specific test harnesses for each week, or our json parsing utility classes. This allows us to package are larger more involved items in one place without clutterly our core classes. 

### `Observer`

This is where all the observer code are. its unit test is in UnitTests folder.

### `TManager`

this is where all the tournament manager code is. its unit test is in UnitTests folder.

### `Remote`

this is the folder holding tournament server and client code.

## Testing

./testme(need execution permission first) will run all the unit tests in the UnitTests folder. test harnesses are located in number folders such as 6, 7, 8, 10, 11 and 13.
