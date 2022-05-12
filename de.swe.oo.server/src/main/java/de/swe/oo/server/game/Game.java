package de.swe.oo.server.game;

public class Game {
    private static volatile Game gameInstance;
    private Game(){

    }

    /**
     * Singleton pattern mostly copied from the Model class of the JavaFxDemo repo.
     * @return gameInstance
     */
    public static Game getGame(){
        if (gameInstance == null){
            synchronized (Game.class){
                if (gameInstance == null){
                    gameInstance = new Game();
                }
            }
        }
        return gameInstance;
    }

    public static boolean wasAlreadyCreated(){
        synchronized(Game.class){
            return (gameInstance != null);
        }
    }
}
