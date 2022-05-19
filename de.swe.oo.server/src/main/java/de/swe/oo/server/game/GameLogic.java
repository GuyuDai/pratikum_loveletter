package de.swe.oo.server.game;

import de.swe.oo.server.player.Player;

public interface GameLogic {
  Player getPlayerInCurrentTurn();  //whose turn
  void setPlayerInCurrentTurn();  //who is in the next turn
  void handleTurn();  //what to do in a turn
  void turnEnd();  //what will happen when a turn ended
  int getRound();  //which round is now
  void ifRoundEnd();  //check if a round ends or not, and what will happen if a round ended
  void ifGameEnd();  //check if the whole game ends or not, and what will happen if the game ended
  void playerWin(Player player);  //what will happen if a player wins
  void playerKickedOff(Player targetPlayer);  //kick a player out of a round
  Player getPlayer(String targetName);  //get a certain player
  void roundInitialize();  //initialize a round
  void initializeDeck();
  void reorderPlayers();  //make a list of active players base on the last date of date
}
