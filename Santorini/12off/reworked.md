#reworked list:

- Refined Referee and TManager and rebuilt some artifacts.
- Added JSONUtils class in Common/src/utils to convert some json values to certain objects and vice versa; Created a unit test for JSONUtils in UnitTests folder; Modified Action, Direction and ActionType class.
- Added a constructor for TManager to directly read configuration from STDIN. Added another pair of configuration file and result file in 11 to stress tournament manager.
- Adjusted some files for players to receive the assigned name in the tournament and Outcomes of games played; added some test  cases about this;
