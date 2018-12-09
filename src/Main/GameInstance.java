package Main;

import Creatures.Creature;
import Creatures.Grunt;
import Creatures.SpeedDemon;
import Tiles.*;
import Towers.SharpShooter;
import Towers.Tower;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameInstance {

    private ArrayList<Tower> towers = new ArrayList<>();
    private CopyOnWriteArrayList<Creature> creatures = new CopyOnWriteArrayList<>();
    private ArrayList<Tile> tiles;
    private ArrayList<Laser> lasers = new ArrayList<>();
    private ArrayList<TowerTile> towerTiles = new ArrayList<>();
    private Position startPosition = null;
    private Direction startDirection;
    private String name;
    private int credits;

    public GameInstance(Map map) {
        this.tiles = map.getTiles();
        this.name = map.getName();
        this.credits = map.getStartCredit();
        findStart();
        findTowerTiles();
        addTower(1);
        addTower(1);
        addTower(1);
        addTower(1);
        update();
    }

    private void findStart() {
        for (Tile tile: tiles) {
            if(tile.getClass() == StartTile.class) {
                startDirection = tile.getDirection();
                startPosition = tile.getCenterPos();
            }
        }
    }

    private void findTowerTiles() {
        for (Tile tile: tiles) {
            if(tile.getClass() == TowerTile.class) {
                towerTiles.add((TowerTile) tile);
            }
        }
    }

    public void update() {
        //System.out.println(creatures.get(0).getCurrentSpeed());
        resetCreatureStats();
        affectCreatureOnTile();
        moveCreatures();
        handleCreaturesInGoal();
        damageCreaturesIfPossible();
    }

    private void resetCreatureStats() {
        for (Creature creature: creatures) {
            creature.setDefaultStats();
        }
    }


    public void addTower(int towerType) {
        ArrayList<TowerTile> emptyTowerTiles = new ArrayList<>();
        int random;
        Position pos;
        for (TowerTile tile: towerTiles) {
            if(!tile.isBuiltOn()) {
                emptyTowerTiles.add(tile);
            }
        }

        if (emptyTowerTiles.size() > 0) {
            random = (int) (Math.random() * (emptyTowerTiles.size()));
            TowerTile tt = emptyTowerTiles.get(random);
            pos = tt.getCenterPos();
            switch (towerType) {
                case 1:
                    towers.add(new SharpShooter(pos));
                    tt.setBuiltOn();
                    break;
                default:
                    System.err.println("No tower type of that int (addTower)");
            }
        }
    }

    public void addCreature(int creatureType) {
        Position pos = new Position(startPosition.getX(), startPosition.getY());
        switch (creatureType) {
            case 1:
                if(credits >= SpeedDemon.COST) {
                    creatures.add(new SpeedDemon(pos, startDirection));
                    credits -= SpeedDemon.COST;
                }
                break;
            case 2:
                if(credits >= Grunt.COST) {
                    creatures.add(new Grunt(pos, startDirection));
                    credits -= Grunt.COST;
                }
                break;
            default:
                System.err.println("No creature type of that int (addCreature)");
        }
    }

    private void moveCreatures() {
        for (Creature creature: creatures) {
            for (int i = 0; i < creature.getCurrentSpeed(); i++) {
                creature.move();
                //creature.getPosition().print();
                //WARNING ORDO WARNING jontor
                changeDirectionIfNeeded(creature);
            }
        }
    }

    private void changeDirectionIfNeeded(Creature creature) {
        for (Tile tile: tiles) {
            if (creature.getPosition().equals(tile.getCenterPos())) {
                creature.setDirection(tile.getDirection());
            }
        }
    }

    private void affectCreatureOnTile() {
        for (Creature creature : creatures) {
            for (Tile tile : tiles) {
                if (tile.positionOnTile(creature.getPosition())) {
                    try {
                        Method landOn = tile.getClass().getMethod("landOn",
                                Creature.class);
                        landOn.invoke(tile,creature);
                    } catch (NoSuchMethodException e) {
                        System.out.println("No method with landOn name found!");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }  // <-- parallelltrapets :D
        }
    }

    private void damageCreaturesIfPossible() {
        reduceLaserLifeSpan();
        for (Tower tower: towers) {
            tower.reduceCooldown();
            if(tower.readyToShoot()) {
                for (Creature creature : creatures) {
                    if(tower.positionInRange(creature.getPosition())) {
                        System.out.println("FIREING AT CREATURE: " + creatures.indexOf(creature));
                        creature.setCurrentHealth(creature.getCurrentHealth() - tower.shoot());
                        deleteCreatureIfDead(creature);
                        lasers.add(new Laser(tower.getPosition(),
                                creature.getPosition(), tower.getLaserColor()));
                        break;
                    }
                }
            }
        }
    }

    private void reduceLaserLifeSpan() {
        Laser laser;
        for (int i = 0; i < lasers.size(); i++) {
            laser = lasers.get(i);
            if (laser.getLifeTime() > 0)
                laser.reduceLifeTime();
            else
                lasers.remove(laser);
        }
    }

    private void handleCreaturesInGoal() {
        for (Creature creature: creatures){
            if(creature.inGoal()) {
                //TODO add score
                credits += 100;
                System.out.println("IN GOAL");
                creatures.remove(creature);
            }
        }
    }

    private void deleteCreatureIfDead(Creature creature) {
        if(creature.isDead())
            creatures.remove(creature);
    }

    public void printAll() {
        for (Creature creature: creatures) {
            creature.printStats();
            System.out.println();
        }
    }

    public synchronized ArrayList<GameObject> getGameObjectsToDraw() {
        ArrayList<GameObject> objectsToDraw = new ArrayList<>();
        objectsToDraw.addAll(tiles);
        objectsToDraw.addAll(towers);
        objectsToDraw.addAll(creatures);
        return objectsToDraw;
    }

    public synchronized ArrayList<Laser> getLaserPositionsToDraw() {
        return lasers;
    }

    public synchronized ArrayList<Healthbar> getHealthBarsToDraw() {
        ArrayList<Healthbar> healthbarsToDraw = new ArrayList<>();
        for (Creature creature: creatures) {
            healthbarsToDraw.add(creature.getHealthbar());
        }
        return healthbarsToDraw;
    }

    public synchronized int getCredits() {
        return credits;
    }
}