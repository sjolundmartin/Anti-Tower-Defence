package gameLogic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO write the purpose of the class.
 */
public class GameManager {
    private ArrayList<Map> maps;
    private Timer timer;
    private int tickRate;
    private GameInstance currentGameInstance;
    int score;
    boolean paused = true;
    int mapIndex;
    boolean mapWon;
    boolean updatedScoreAtWin = false;

    /**
     * Constructor of the class.
     *
     * @param maps List of maps to manage.
     * @param tickRate How fast to update the game.
     */
    public GameManager(ArrayList<Map> maps, int tickRate) {
        this.maps = maps;
        //30
        this.tickRate = tickRate;
        this.mapIndex = 0;
        setNextMap();
    }

    public void setNextMap(){
        mapWon = false;
        if(currentGameInstance !=null) {
            score += currentGameInstance.getScore();
        }
        currentGameInstance = new GameInstance(maps.get(mapIndex));
        mapIndex++;
    }

    /**
     * Start the game.
     */
    public void startGame() {
        if(paused) {
            paused = false;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (currentGameInstance.mapWon()) {
                        mapWon = true;
                    }
                    updateGame();
                }
            }, 1000, 1000 / tickRate);
        }
    }

    public ArrayList<Position> getFlipperTilePositions(){
        return currentGameInstance.getFlipperTilePositions();
    }

    /**
     * Stops the game.
     */
    public void stopGame() {
        if(timer != null) {
            timer.cancel();
            paused = true;
        }
    }

    public Boolean getGameOver(){
        return currentGameInstance.gameOver();
    }

    /**
     *
     * @return A list of GameObject objects to draw.
     */
    public ArrayList<GameObject> getGameObjectsToDraw() {
        return currentGameInstance.getGameObjectsToDraw();
    }

    /**
     *
     * @return A list of positions to draw a laser between.
     */
    public ArrayList<Laser> getLaserPositionsToDraw() {
        return currentGameInstance.getLaserPositionsToDraw();
    }

    /**
     *
     * @return A list of healthbars to draw.
     */
    public ArrayList<Healthbar> getHealthbarsToDraw() {
        return currentGameInstance.getHealthbarsToDraw();
    }

    /**
     * Adds a creature to the game instance.
     * @param type Which type of creature to add.
     */
    public void addCreature(int type) {
        if(!paused) {
            switch (type) {
                case 1:
                    currentGameInstance.addCreature(1);
                    break;
                case 2:
                    currentGameInstance.addCreature(2);
                    break;
                case 3: currentGameInstance.addCreature(3);
            }
        }
    }

    /**
     *
     * @return The current credits of the game instance.
     */
    public int getCredits() {
        return currentGameInstance.getCredits();
    }

    /**
     * Updates all game objects.
     */
    private void updateGame() {
        currentGameInstance.update();
    }


    public void flipTile(Position flipTilePosition){
        currentGameInstance.flipTile(flipTilePosition);
    }

    public void placePortal(){
        currentGameInstance.placePortal();
    }

    public boolean isMapWon() {
        return mapWon;
    }

    public void restartGame() {
        mapIndex = 0;
        mapWon = false;
        setNextMap();
        score = 0;
    }

    public boolean allLevelsWon() {
        boolean allWon = mapWon && mapIndex == maps.size();
        if(allWon && !updatedScoreAtWin) {
            score += currentGameInstance.getScore();
            updatedScoreAtWin = true;
        }
        return mapWon && mapIndex == maps.size();
    }

    public String getCurrentMapName(){
        return currentGameInstance.getMapName();
    }

    public int getScore() {
        return score;
    }
}