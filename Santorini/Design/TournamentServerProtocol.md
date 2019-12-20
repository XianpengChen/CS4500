a tournament server will run a round-robin between players.
here is how:
<pre>
tournament server                                 Player <br/>
|  waiting for connection                               |<br/>
| ----------------------------------------------------->|<br/>
|                                              connected|<br/>
| <-----------------------------------------------------|<br/>
|                                             playerName|<br/>
| <-----------------------------------------------------|<br/>
|  uniqueName and tournament statements                 |<br/>
| ----------------------------------------------------->|<br/>
|  series I, VS opponent uniqueName                     |<br/>
| ----------------------------------------------------->|<br/>
|  game I of this series                                |<br/>
| ----------------------------------------------------->|<br/>
|  gameboard,need placementAction                       |<br/>
| ----------------------------------------------------->|<br/>
|                                        placementAction|<br/>
| <-----------------------------------------------------|<br/>
|  gameboard,need placementAction                       |<br/>
| ----------------------------------------------------->|<br/>
|                                        placementAction|<br/>
| <-----------------------------------------------------|<br/>
|  gameboard,need turnACtion                            |<br/>
| ----------------------------------------------------->|<br/>
|                                             turnAction|<br/>
| <-----------------------------------------------------|<br/>
    ......(a number of turns after)                     |<br/>
|  game I ended,winner loser gameboard                  |<br/>
| ----------------------------------------------------->|<br/>
    ......(a number of games after)                      <br/>
|  series I ended, winner loser                         |<br/>
| ----------------------------------------------------->|<br/>
    ......(a number of series after)                     <br/>
|  all series ended, tournament summary                 |<br/>
| ----------------------------------------------------->|<br/>
|                                                       |<br/>
|                                           disconnected|<br/>
| <-----------------------------------------------------|<br/>
</pre>
First the tournament manager is waiting for players to connect. aAter a player connected to the tournament server, the player send over his name
(JSON String) first, then the server will send back a unique name(JSON String) refering this player and a tournament statement(JSON String) of 
how this tournament is running and the rules here in this tournament.

Then it starts a series between this player and an opponent player(uniqueName (JSON String)). 
It starts the first game of this series; send over a gameboard(Board, see http://www.ccs.neu.edu/home/matthias/4500-f18/6.html), waiting for a
placementAction, here we define a placementAction is: [workerName, x, y], workerName is JSON String like: playerUniqueName + "1" or "2", x, y are JSON numbers(0-5); then the
player sends out a placementAction; the placement phase finishs after 4 workers of 2 players have been placed;

Then the server sends over the current gameboard(Board) and waiting for a turnAction(Move and build or just move, see 
http://www.ccs.neu.edu/home/matthias/4500-f18/6.html). the player should send over a turnAction. After a number of turns, this game ended and
the server will inform both player the winner(JSON String) and the loser(JSON String) and final gameboard(Board).

After a number of games, this series ended. Also the server will inform both player the winner(JSON String) and the loser(JSON String) of this series

After a number of series, this tournament ended. The server will inform every player a summary(some JSON values, unspecified yet) of this 
tournament.

