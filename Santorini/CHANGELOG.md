# Changelog:

- Edited StandardSantoriniRulesEngine isTurnLegal() method to return true when making a legal move. Previously, this method returned whether or not the player would win on this move.
- Edited ViewModelBoard getPlayerWorker() method to return empty list when map.get doesn't find a key.
- Edited DiagonalPlacementStrategy getPlaceWorker() to remove extraneous increment statements.
- Changed a fieldname in IBoard from playerName to workerId to match the concrete class implementation field name.
- Edited CartesianDistancePlacementStrategy getPlaceWorker method to remove a bug.
- Refactored whole Santorini from maven to a normal intelliJ project.
- Added testme to run all unit tests.
- Added observer in Observer folder and xobserve in 10 folder and edited some files to accommodate this addition.
- Added tournament server protocol design to Design folder.
- Added TManager folder for tournament manager.
- Added 11 folder and xrun in it.
- Refined Referee and TManager and rebuilt some artifacts.